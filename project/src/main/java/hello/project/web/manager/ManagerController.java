package hello.project.web.manager;

import hello.project.dataInit.DataInit;
import hello.project.dataInit.Gender;
import hello.project.domain.buy.CTR;
import hello.project.domain.category.CategoryList;
import hello.project.domain.category.ageCategory.AgeCategory;
import hello.project.domain.category.categoryTitle.CategoryTitle;
import hello.project.domain.item.ItemSearch;
import hello.project.domain.item.item.Item;
import hello.project.domain.item.item.ItemFindAll;
import hello.project.domain.user.user.User;
import hello.project.domain.user.user.UserSearch;
import hello.project.repository.buy.BuyRepository;
import hello.project.repository.category.CategoryRepository;
import hello.project.repository.item.ItemRepository;
import hello.project.repository.user.UserRepository;
import hello.project.web.LoginCheck;
import hello.project.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LoginCheck loginCheckComplete;
    private final BuyRepository buyRepository;
    private final ItemRepository itemRepository;

    @GetMapping
    public String managerHome(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);

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
        CTR ctr = buyRepository.countCTR();
        List<Item> itemList = itemRepository.findAll(new ItemSearch());
        Long views = 0L;
        for(Item item : itemList){
            if(item.getViews() != null){
                views += item.getViews();
            }
        }
        double a = ctr.getCtr() / 1;
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
        model.addAttribute("user",user);

        return "manager/managerHome";
    }

    @GetMapping("/false")
    public String managerHomeFalse(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        return "manager/managerFalse";
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
}
