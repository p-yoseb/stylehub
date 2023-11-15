package hello.project.web.mypage;

import hello.project.dataInit.DataInit;
import hello.project.dataInit.Gender;
import hello.project.domain.basket.Basket;
import hello.project.domain.basket.ItemBasket;
import hello.project.domain.buy.CTR;
import hello.project.domain.buy.orderDetail.OrderDetail;
import hello.project.domain.buy.orderList.OrderList;
import hello.project.domain.category.CategoryList;
import hello.project.domain.category.ageCategory.AgeCategory;
import hello.project.domain.category.category.CategorySecond;
import hello.project.domain.category.categoryTitle.CategoryTitle;
import hello.project.domain.item.item.ItemCategory;
import hello.project.domain.item.item.ItemFindAll;
import hello.project.domain.item.itemOption.ItemOption;
import hello.project.domain.user.user.User;
import hello.project.domain.user.user.UserUpdateForm;
import hello.project.repository.basket.BasketRepository;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BuyRepository buyRepository;
    private final BasketRepository basketRepository;
    private final LoginCheck loginCheckComplete;


    @GetMapping
    public String mypage(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        model.addAttribute("user",user);
        return "mypage/mypage";
    }

    // 주문 내역
    @GetMapping("/order")
    public String mypageOrder(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        double priceTotal = 0;
        double priceTotalDisCount = 0;
        double priceTotalDisCountDelivery = 0;

        List<ItemBasket> itemBasketList = createOrderList(user);
        for(ItemBasket itemBasket : itemBasketList){
            priceTotal += itemBasket.getTotalPrice() * itemBasket.getQuantity();
            priceTotalDisCount += itemBasket.getTotalPriceDiscount() * itemBasket.getQuantity();
        }

        priceTotalDisCountDelivery += priceTotalDisCount + DataInit.deliveryFee;
        model.addAttribute("priceTotalDisCountDelivery",priceTotalDisCountDelivery);

        model.addAttribute("priceTotal",priceTotal);
        model.addAttribute("priceTotalDisCount",priceTotalDisCount);
        model.addAttribute("itemBasketList",itemBasketList);

        model.addAttribute("user",user);
        return "mypage/orderDetail";
    }

    // 주문 내역 삭제
    @GetMapping("/deleteOrder/{orderDetailId}")
    public String mypageOrderDelete(@Login User loginUser, @PathVariable Long orderDetailId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        
        double priceTotal = 0;
        double priceTotalDisCount = 0;
        double priceTotalDisCountDelivery = 0;

        // 상품 수량 수정
        OrderDetail orderDetail = buyRepository.DetailfindById(orderDetailId).get();
        ItemOption itemOption = itemRepository.findOptionById(orderDetail.getItemOptionId()).get();
        itemOption.setQuantity(itemOption.getQuantity()+orderDetail.getQuantity());
        itemRepository.updateOption(orderDetail.getItemOptionId(),itemOption);

        // 사용자 예치금, 적립금 수정
        user.setAmount(user.getAmount()-orderDetail.getTotalPrice());
        user.setDeposit(user.getDeposit()+orderDetail.getTotalPrice());
        userRepository.update(user.getId(),user);


        // orderList 토탈 금액 수정
        OrderList orderList = buyRepository.ListfindById(orderDetail.getOrderListId()).get();
        orderList.setTotalPrice(orderList.getTotalPrice()-orderDetail.getTotalPrice());
        buyRepository.ListUpdate(orderDetail.getOrderListId(),orderList);

        // orderDetail 삭제
        buyRepository.DetailDelete(orderDetailId);

        List<ItemBasket> itemBasketList = createOrderList(user);
        for(ItemBasket itemBasket : itemBasketList){
            priceTotal += itemBasket.getTotalPrice() * itemBasket.getQuantity();
            priceTotalDisCount += itemBasket.getTotalPriceDiscount() * itemBasket.getQuantity();

        }

        priceTotalDisCountDelivery += priceTotalDisCount + DataInit.deliveryFee;
        model.addAttribute("priceTotalDisCountDelivery",priceTotalDisCountDelivery);

        model.addAttribute("priceTotal",priceTotal);
        model.addAttribute("priceTotalDisCount",priceTotalDisCount);
        model.addAttribute("itemBasketList",itemBasketList);

        model.addAttribute("user",user);
        return "mypage/orderDetail";
    }


    // 쿠폰 내역
    @GetMapping("/coupon")
    public String mypageCoupon(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        model.addAttribute("user",user);
        return "mypage/couponDetail";
    }

    // 예치금 내역
    @GetMapping("/deposit")
    public String mypageDeposit(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        model.addAttribute("user",user);
        return "mypage/depositDetail";
    }

    // 관심상품
    @GetMapping("/interest")
    public String mypageInterest(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        double priceTotal = 0;
        double priceTotalDisCount = 0;
        double priceTotalDisCountDelivery = 0;

        List<ItemBasket> itemBasketList = createBasketList(user);
        for(ItemBasket itemBasket : itemBasketList){
            priceTotal += itemBasket.getTotalPrice() * itemBasket.getQuantity();
            priceTotalDisCount += itemBasket.getTotalPriceDiscount() * itemBasket.getQuantity();
        }

        priceTotalDisCountDelivery += priceTotalDisCount + DataInit.deliveryFee;
        model.addAttribute("priceTotalDisCountDelivery",priceTotalDisCountDelivery);

        model.addAttribute("priceTotal",priceTotal);
        model.addAttribute("priceTotalDisCount",priceTotalDisCount);
        model.addAttribute("itemBasketList",itemBasketList);
        model.addAttribute("user",user);
        return "mypage/interestItem";
    }

    // 사용자 정보
    @GetMapping("/detail")
    public String mypageDetail(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        categoryRepository.findById(user.getId());

        ItemCategory itemCategory = new ItemCategory(findCategoryName(user));
        model.addAttribute("itemCategory",itemCategory);

        model.addAttribute("user",user);
        return "mypage/detailUser";
    }

    // 사용자 정보 수정
    @GetMapping("/detail/edit")
    public String userEdit(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        CTR ctr = buyRepository.countCTR();
        ctr.setCtr(ctr.getCtr()+1);
        buyRepository.updateCTR(ctr.getCtr());

        model.addAttribute("user",user);

        return "mypage/editUser";
    }

    @PostMapping("/detail/edit")
    public String userEditPost(@Login User loginUser, @Validated @ModelAttribute("user") UserUpdateForm form, BindingResult bindingResult, Model model){
        User user = loginCheckComplete.Check(loginUser,model);

        log.info("bindingResult={}",bindingResult);

        if (bindingResult.hasErrors()) {
            return "mypage/editUser";
        }

        user.setName(form.getName());
        user.setPassword(form.getPassword());
        user.setYear(form.getYear());
        user.setMonth(form.getMonth());
        user.setDay(form.getDay());
        user.setGender(form.getGender());
        user.setAddress(form.getAddress());
        user.setPostal(form.getPostal());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setAdvertising(form.getAdvertising());
        user.setDeposit(form.getDeposit());
        user.setCategoryId(form.getCategoryId());

        userRepository.update(user.getId(), user);


        model.addAttribute("user",user);

        return "redirect:/mypage/detail";
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

    private List<ItemBasket> createBasketList(User user) {
        List<Basket> basketList = basketRepository.findByUserId(user.getId());
        List<ItemBasket> itemBasketList = new ArrayList<>();
        for(Basket basket : basketList){
            ItemOption itemOption = itemRepository.findOptionById(basket.getItemOptionId()).get();
            ItemBasket itemBasket = new ItemBasket();
            itemBasket.setItemImage(itemRepository.findById(itemOption.getItemId()).get().getImageName1());
            itemBasket.setItemOptionId(itemOption.getId());
            itemBasket.setName(itemOption.getName());
            itemBasket.setUserId(user.getId());
            itemBasket.setQuantity(basket.getQuantity());
            itemBasket.setTotalPrice(Math.floor(itemOption.getPrice()*0.9/100)*100);
            itemBasket.setTotalPriceDiscount(Math.floor(Math.floor(itemOption.getPrice()*0.9/100)*100*(1-user.getRatio())/100)*100);
            itemBasket.setBasketId(basket.getId());
            itemBasket.setItemId(itemOption.getItemId());
            itemBasketList.add(itemBasket);
        }
        return itemBasketList;
    }

    private List<ItemBasket> createOrderList(User user) {
        List<OrderList> orderListList = buyRepository.ListfindByUserId(user.getId());
        List<ItemBasket> itemBasketList = new ArrayList<>();

        for(OrderList orderList : orderListList){
            List<OrderDetail> basketList = buyRepository.DetailfindByListId(orderList.getId());
            for(OrderDetail order : basketList){
                ItemOption itemOption = itemRepository.findOptionById(order.getItemOptionId()).get();
                ItemBasket itemBasket = new ItemBasket();
                itemBasket.setItemImage(itemRepository.findById(itemOption.getItemId()).get().getImageName1());
                itemBasket.setItemOptionId(itemOption.getId());
                itemBasket.setName(itemOption.getName());
                itemBasket.setUserId(user.getId());
                itemBasket.setQuantity(order.getQuantity());
                itemBasket.setTotalPrice(Math.floor(itemOption.getPrice()*0.9/100)*100);
                itemBasket.setTotalPriceDiscount(Math.floor(Math.floor(itemOption.getPrice()*0.9/100)*100*(1-user.getRatio())/100)*100);
                itemBasket.setOrderDetailId(order.getId());
                itemBasket.setItemId(itemOption.getItemId());
                itemBasketList.add(itemBasket);
            }
        }

        return itemBasketList;
    }

    @ModelAttribute("categories")
    public List<CategorySecond> categories(){
        return categoryRepository.findAll();
    }

    @ModelAttribute("genderList")
    public Gender[] gender(){
        return Gender.values();
    }

    private String findCategoryName(User user) {
        return categoryRepository.findById(user.getCategoryId()).get().getCategoryTitle()+"-"+categoryRepository.findById(user.getCategoryId()).get().getCategory();
    }
}
