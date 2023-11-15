package hello.project.web.item;

import hello.project.dataInit.DataInit;
import hello.project.domain.basket.ItemBasket;
import hello.project.domain.buy.CTR;
import hello.project.domain.buy.orderDetail.ItemOrderDetail;
import hello.project.domain.buy.orderDetail.OrderDetail;
import hello.project.domain.category.CategoryList;
import hello.project.domain.category.ageCategory.AgeCategory;
import hello.project.domain.category.category.Category;
import hello.project.domain.category.categoryTitle.CategoryTitle;
import hello.project.domain.item.item.Item;
import hello.project.domain.item.item.ItemFindAll;
import hello.project.domain.item.itemOption.ItemOption;
import hello.project.domain.user.user.User;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final LoginCheck loginCheckComplete;
    private final BuyRepository buyRepository;

    // 상품 기본 검색 페이지
    @GetMapping
    public String provisionItem(@Login User loginUser, @ModelAttribute("itemFindAll") ItemFindAll itemFindAll, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        model.addAttribute("user",user);

        List<ItemFindAll> itemFindAllList = itemRepository.itemFindAll(itemFindAll);
        List<ItemFindAll> itemFind = FindOption(itemFindAllList);

        model.addAttribute("itemFind",itemFind);
        return "item/provisionItem";
    }

    // 상품 값을 받으면 검색하는 페이지
    @PostMapping
    public String provisionItemSearch(@Login User loginUser, @ModelAttribute("itemFindAll") ItemFindAll itemFindAll, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        List<ItemFindAll> itemFindAllList = itemRepository.itemFindAll(itemFindAll);
        List<ItemFindAll> itemFind = FindOption(itemFindAllList);

        if(itemFind.isEmpty()){
            model.addAttribute("status",true);
            return "item/provisionItem";
        }

        String itemCategory = itemFind.get(0).getCategoryTitle() +"-"+itemFind.get(0).getCategory();

        model.addAttribute("itemFind",itemFind);
        model.addAttribute("itemCategory",itemCategory);
        return "item/provisionItem";
    }

    // 카테고리별 상품 탭
    @GetMapping("/{categoryId}")
    public String provisionItemSearchCategory(@Login User loginUser, @PathVariable Long categoryId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        model.addAttribute("user",user);

        List<ItemFindAll> itemFindAllList = itemRepository.itemFindAllByCategoryId(categoryId);
        List<ItemFindAll> itemFind = FindOption(itemFindAllList);
        String itemCategory = "";
        if(!itemFind.isEmpty()){
            itemCategory = itemFind.get(0).getCategoryTitle() +"-"+itemFind.get(0).getCategory();
        }else{
            model.addAttribute("status",true);
        }

        model.addAttribute("itemFind",itemFind);
        model.addAttribute("itemCategory",itemCategory);
        return "item/provisionItem";
    }

    // 카테고리 대주제별 상품 탭
    @GetMapping("/title/{categoryTitleId}")
    public String provisionItemSearchTitle(@Login User loginUser, @PathVariable Long categoryTitleId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        model.addAttribute("user",user);

        List<Category> categoryList = categoryRepository.CategoryFindByTitleId(categoryTitleId);
        List<ItemFindAll> itemFindAllList = new ArrayList<>();
        for(Category category : categoryList){
            for(ItemFindAll itemFindAll : itemRepository.itemFindAllByCategoryId(category.getId())){
                itemFindAllList.add(itemFindAll);
            }
        }

        List<ItemFindAll> itemFind = FindOption(itemFindAllList);
        String itemCategory = itemFind.get(0).getCategoryTitle() +"-"+itemFind.get(0).getCategory();

        model.addAttribute("itemFind",itemFind);
        model.addAttribute("itemCategory",itemCategory);
        return "item/provisionItem";
    }

    // 상품 상세 페이지
    @GetMapping("/detail/{itemId}")
    public String detailItem(@Login User loginUser, @PathVariable Long itemId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        Item item = itemRepository.findById(itemId).get();
        Long categoryTitleId = categoryRepository.CategoryFindById(itemRepository.findById(itemId).get().getCategoryId()).get(0).getTitleId();

        List<ItemOption> itemOptionList = itemRepository.findOptionByItemId(itemId);
        double price = itemOptionList.get(0).getPrice();
        double priceDiscount = Math.floor(itemOptionList.get(0).getPrice()*0.9/100)*100;
        double priceVIPDiscount = Math.floor(Math.floor(itemOptionList.get(0).getPrice()*0.9/100)*100*(1-DataInit.VIPRATIO)/100)*100;

        if(item.getViews() == null){
            item.setViews(0L);
        }
        item.setViews(item.getViews()+1);
        itemRepository.update(itemId,item);

        model.addAttribute("item",item);
        model.addAttribute("itemOptionList",itemOptionList);
        model.addAttribute("price",price);
        model.addAttribute("priceDiscount",priceDiscount);
        model.addAttribute("priceVIPDiscount",priceVIPDiscount);


        List<Category> categoryList = categoryRepository.CategoryFindByTitleId(categoryTitleId);
        List<ItemFindAll> itemFindAllList = new ArrayList<>();
        for(Category category : categoryList){
            for(ItemFindAll itemFindAll : itemRepository.itemFindAllByCategoryId(category.getId())){
                itemFindAllList.add(itemFindAll);
            }
        }
        List<ItemFindAll> itemFind = FindOption(itemFindAllList);
        String itemCategory = itemFind.get(0).getCategoryTitle() +"-"+itemFind.get(0).getCategory();

        model.addAttribute("itemFind",itemFind);
        model.addAttribute("itemCategory",itemCategory);

        List<ItemBasket> itemBasketList = new ArrayList<>();
        List<ItemOption> itemOptions = new ArrayList<>();
        for(ItemOption itemOption : itemOptionList){
            itemOptions.add(itemOption);
        }

        for(int i=0;i<5;i++){
            ItemBasket itemBasket = new ItemBasket();
            if(itemOptions.size()>=i){
                itemOptions.add(new ItemOption());
            }

            if(itemOptions.get(i).getItemId() != null ){
                itemBasket.setItemOptionId(itemOptions.get(i).getId());
                itemBasket.setName(itemOptions.get(i).getName());
                itemBasket.setQuantity(0L);
                itemBasket.setTotalPrice(Math.floor(itemOptions.get(i).getPrice()*0.9/100)*100);
                if(user.getRatio() == null){
                    user.setRatio(0.0);
                }
                itemBasket.setTotalPriceDiscount(Math.floor(Math.floor(itemOptions.get(i).getPrice()*0.9/100)*100*(1-user.getRatio())/100)*100);
                itemBasket.setStatus(true);
                itemBasketList.add(itemBasket);
            }else{
                itemBasket.setStatus(false);
                itemBasketList.add(itemBasket);
            }

        }
        model.addAttribute("user",user);

        model.addAttribute("itemBasket1", itemBasketList.get(0));
        model.addAttribute("itemBasket2", itemBasketList.get(1));
        model.addAttribute("itemBasket3", itemBasketList.get(2));
        model.addAttribute("itemBasket4", itemBasketList.get(3));
        model.addAttribute("itemBasket5", itemBasketList.get(4));

        return "item/detailItem";
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
