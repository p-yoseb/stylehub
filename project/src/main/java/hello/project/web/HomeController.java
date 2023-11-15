package hello.project.web;

import hello.project.dataInit.DataInit;
import hello.project.dataInit.Gender;
import hello.project.domain.buy.CTR;
import hello.project.domain.category.CategoryList;
import hello.project.domain.category.ageCategory.AgeCategory;
import hello.project.domain.category.category.Category;
import hello.project.domain.category.categoryTitle.CategoryTitle;
import hello.project.domain.item.ItemSearch;
import hello.project.domain.item.item.Item;
import hello.project.domain.item.item.ItemFindAll;
import hello.project.domain.item.itemOption.ItemOption;
import hello.project.domain.user.user.User;
import hello.project.domain.user.user.UserSearch;
import hello.project.repository.buy.BuyRepository;
import hello.project.repository.category.CategoryRepository;
import hello.project.repository.item.ItemRepository;
import hello.project.repository.user.UserRepository;
import hello.project.web.argumentresolver.Login;
import hello.project.web.login.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BuyRepository buyRepository;

    @GetMapping("/")
    public String homeV1(@Login User loginUser, @ModelAttribute("itemFindAll") ItemFindAll itemFindAll, Model model)
    {
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        if (loginUser != null) {
            model.addAttribute("myPageCheck",true);
            User user = userRepository.findById(loginUser.getId()).get();
            if(user.getAdmin()){
                model.addAttribute("managerCheck",true);

                // 남여 비율
                int maleRatio = userRepository.countGender(Gender.male.getDescription());
                int femaleRatio = userRepository.countGender(Gender.female.getDescription());

                // 나이대 비율
                List<User> userList = userRepository.findAll(new UserSearch());
                int age10 = 0;
                int age20 = 0;
                int age30 = 0;
                int age40 = 0;
                int age50 = 0;

                for(User users : userList){
                    int age = LocalDate.now().getYear() - users.getYear();
                    if(age<20){
                        age10 += 1;
                    } else if (age<30) {
                        age20 += 1;
                    } else if (age<40) {
                        age30 += 1;
                    } else if (age<50) {
                        age40 += 1;
                    }else{
                        age50 += 1;
                    }
                }

                // 결제 건수,자수,금액
                int countOrderList = buyRepository.countId();
                int countOrderUser = buyRepository.countUserId();
                int sumPrice = buyRepository.sumPrice();

                // CTR, CVR
                CTR ctrRA = buyRepository.countCTR();
                List<Item> itemList = itemRepository.findAll(new ItemSearch());
                Long views = 0L;
                for(Item item : itemList){
                    if(item.getViews() != null){
                        views += item.getViews();
                    }
                }
                double a = ctrRA.getCtr() / 1;
                double b = views / 1 ;
                double c = countOrderList / 1 ;

                double ctrRatio =  b / a;
                log.info("ctrRatio={}",ctrRatio);
                double cvrRatio =  c / b;
                log.info("cvrRatio={}",cvrRatio);


                model.addAttribute("ctrRatio",ctrRatio);
                model.addAttribute("cvrRatio",cvrRatio);
                model.addAttribute("countOrderList",countOrderList);
                model.addAttribute("countOrderUser",countOrderUser);
                model.addAttribute("sumPrice",sumPrice);
                model.addAttribute("age10",age10);
                model.addAttribute("age20",age20);
                model.addAttribute("age30",age30);
                model.addAttribute("age40",age40);
                model.addAttribute("age50",age50);
                model.addAttribute("maleRatio",maleRatio);
                model.addAttribute("femaleRatio",femaleRatio);

                return "manager/managerHome";
            }
            Long categoryTitleId = categoryRepository.CategoryFindById(user.getCategoryId()).get(0).getTitleId();
            List<Category> categoryList = categoryRepository.CategoryFindByTitleId(categoryTitleId);
            List<ItemFindAll> itemFindAllList = new ArrayList<>();
            for(Category category : categoryList){
                for(ItemFindAll itemFind : itemRepository.itemFindAllByCategoryId(category.getId())){
                    itemFindAllList.add(itemFind);
                }
            }
            List<ItemFindAll> itemFind = FindOption(itemFindAllList);
            String itemCategory = itemFind.get(0).getCategoryTitle() +"-"+itemFind.get(0).getCategory();

            model.addAttribute("user", user);
            model.addAttribute("itemFind",itemFind);
            return "home/home";
        }

        List<ItemFindAll> itemFindAllList = itemRepository.itemFindAll(itemFindAll);
        List<ItemFindAll> itemFind = FindOption(itemFindAllList);

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("itemFind",itemFind);
        model.addAttribute("user", loginUser);
        return "home/home";
    }


    @ModelAttribute("itemFindAll")
    public ItemFindAll itemFindAll(){
        return new ItemFindAll();
    }

    @ModelAttribute("categoryList")
    public List<CategoryList> categoryList(){
        List<CategoryList> categoryList = new ArrayList<>();
        List<CategoryTitle> categoryTitles = categoryRepository.categoryTitleFindAll();

        for(CategoryTitle categoryTitle : categoryTitles){
            categoryList.add(new CategoryList(categoryTitle.getId(), categoryTitle.getCategoryTitle(), categoryRepository.CategoryFindByTitleId(categoryTitle.getId())));
        }
        return categoryList;
    }

    @ModelAttribute("ageCategory")
    public List<CategoryList> ageCategoryList(@Login User loginUser){
        LoginCheck loginCheck = new LoginCheck(userRepository);
        Long age;
        if(loginUser == null){
            age = DataInit.AGE;
        }else{
            User user = userRepository.findById(loginUser.getId()).get();
            age = loginCheck.age(user.getYear());
        }

        AgeCategory ageCategory = categoryRepository.AgeCategoryFindByAge(age);
        List<CategoryList> categoryList = new ArrayList<>();
        List<CategoryTitle> categoryTitles = new ArrayList<>();
        categoryTitles.add(categoryRepository.CategoryTitleFindById(ageCategory.getTitleId1()));
        categoryTitles.add(categoryRepository.CategoryTitleFindById(ageCategory.getTitleId2()));
        categoryTitles.add(categoryRepository.CategoryTitleFindById(ageCategory.getTitleId3()));
        categoryTitles.add(categoryRepository.CategoryTitleFindById(ageCategory.getTitleId4()));
        categoryTitles.add(categoryRepository.CategoryTitleFindById(ageCategory.getTitleId5()));
        categoryTitles.add(categoryRepository.CategoryTitleFindById(ageCategory.getTitleId6()));
        categoryTitles.add(categoryRepository.CategoryTitleFindById(ageCategory.getTitleId7()));
        categoryTitles.add(categoryRepository.CategoryTitleFindById(ageCategory.getTitleId8()));

        for(CategoryTitle categoryTitle : categoryTitles){
            categoryList.add(new CategoryList(categoryTitle.getId(), categoryTitle.getCategoryTitle(), categoryRepository.CategoryFindByTitleId(categoryTitle.getId())));
        }
        categoryList.get(0);
        return categoryList;
    }

    private List<ItemFindAll> FindOption(List<ItemFindAll> itemFindAllList) {
        List<ItemFindAll> itemFind = new ArrayList<>();
        for(ItemFindAll itemFinds : itemFindAllList){
            List<ItemOption> itemOptions = itemRepository.findPrice(itemFinds.getItemId());
            for(int i = 0;i<itemOptions.size();i++){
                if(i==0){
                    itemFinds.setPrice(itemOptions.get(i).getPrice());
                    itemFinds.setPriceDiscount(Math.floor(itemOptions.get(i).getPrice()*0.9/100)*100);
                    itemFinds.setItemOptionId(itemOptions.get(i).getId());
                    itemFind.add(itemFinds);
                }
            }
        }
        return itemFind;
    }
}