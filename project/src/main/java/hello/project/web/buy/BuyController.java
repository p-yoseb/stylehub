package hello.project.web.buy;

import hello.project.dataInit.DataInit;
import hello.project.dataInit.Gender;
import hello.project.dataInit.OrderPayment;
import hello.project.domain.basket.Basket;
import hello.project.domain.basket.ItemBasket;
import hello.project.domain.buy.CTR;
import hello.project.domain.buy.orderDetail.OrderDetail;
import hello.project.domain.buy.orderList.OrderList;
import hello.project.domain.category.CategoryList;
import hello.project.domain.category.ageCategory.AgeCategory;
import hello.project.domain.category.categoryTitle.CategoryTitle;
import hello.project.domain.item.item.ItemFindAll;
import hello.project.domain.item.itemOption.ItemOption;
import hello.project.domain.user.user.User;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/buy")
@RequiredArgsConstructor
public class BuyController {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BasketRepository basketRepository;
    private final BuyRepository buyRepository;
    private final LoginCheck loginCheckComplete;


    // 장바구니 페이지
    @GetMapping("/basket")
    public String basket(@Login User loginUser, Model model){
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

        model.addAttribute("user",user);

        model.addAttribute("priceTotal",priceTotal);
        model.addAttribute("priceTotalDisCount",priceTotalDisCount);
        model.addAttribute("itemBasketList",itemBasketList);
        return "buy/basket";
    }



    @PostMapping("/basket")
    public String basketPost(@Login User loginUser, @RequestParam(value="itemOptionId",required=false) List<Long> itemOptionId,
                             @RequestParam(value="quantity",required=false) List<Long> quantity, Model model){
        User user = loginCheckComplete.Check(loginUser,model);

        double priceTotal = 0;
        double priceTotalDisCount = 0;
        double priceTotalDisCountDelivery = 0;

        List<ItemBasket> itemBaskets = new ArrayList<>();

        for(int i = 0;i<quantity.size();i++){
            if(quantity.get(i)>0){
                ItemBasket itemBasket = new ItemBasket();
                itemBasket.setItemOptionId(itemOptionId.get(i));
                itemBasket.setQuantity(quantity.get(i));
                itemBaskets.add(itemBasket);
            }
        }

        for(ItemBasket itemBasket : itemBaskets){
            if(itemBasket.getItemOptionId() != null){
                basketRepository.save(new Basket(user.getId(),itemBasket.getItemOptionId(),itemBasket.getQuantity()));

                ItemOption itemOption = itemRepository.findOptionById(itemBasket.getItemOptionId()).get();
                Long itemOptionQuantity = itemOption.getQuantity() - itemBasket.getQuantity();
                itemOption.setQuantity(itemOptionQuantity);
                itemRepository.updateOption(itemOption.getId(),itemOption);
            }
        }

        List<ItemBasket> itemBasketList = createBasketList(user);
        for(ItemBasket itemBasket : itemBasketList){
            priceTotal += itemBasket.getTotalPrice() * itemBasket.getQuantity();
            priceTotalDisCount += itemBasket.getTotalPriceDiscount() * itemBasket.getQuantity();
        }

        priceTotalDisCountDelivery += priceTotalDisCount + DataInit.deliveryFee;
        model.addAttribute("priceTotalDisCountDelivery",priceTotalDisCountDelivery);

        model.addAttribute("user",user);

        model.addAttribute("priceTotal",priceTotal);
        model.addAttribute("priceTotalDisCount",priceTotalDisCount);
        model.addAttribute("itemBasketList",itemBasketList);

        return "buy/basket";
    }

    // 장바구니 삭제
    @GetMapping("/basket/delete/{basketId}")
    public String deleteBasket(@Login User loginUser, @PathVariable Long basketId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        Basket basket = basketRepository.findById(basketId).get();
        ItemOption itemOption = itemRepository.findOptionById(basket.getItemOptionId()).get();
        itemOption.setQuantity(itemOption.getQuantity() + basket.getQuantity());
        itemRepository.updateOption(basket.getItemOptionId(), itemOption);
        basketRepository.deleteBasket(basketId);

        return "redirect:/buy/basket";
    }

    // 구매 페이지
    @GetMapping("/payment")
    public String payment(@Login User loginUser, Model model){
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
        OrderList orderList = new OrderList();
        orderList.setPayment("예치금");

        model.addAttribute("user",user);
        model.addAttribute("priceTotal",priceTotal);
        model.addAttribute("priceTotalDisCount",priceTotalDisCount);
        model.addAttribute("DisCount",priceTotal-priceTotalDisCount);
        model.addAttribute("itemBasketList",itemBasketList);
        model.addAttribute("orderList",orderList);

        return "buy/payment";
    }

    @PostMapping("/payment")
    public String paymentPost(@Login User loginUser, @RequestParam(value="itemOptionId",required=false) List<Long> itemOptionId,
                              @RequestParam(value="quantity",required=false) List<Long> quantity,
                              Model model){
        User user = loginCheckComplete.Check(loginUser,model);

        double priceTotal = 0;
        double priceTotalDisCount = 0;
        double priceTotalDisCountDelivery = 0;

        List<ItemBasket> itemBaskets = new ArrayList<>();

        for(int i = 0;i<quantity.size();i++){
            if(quantity.get(i)>0){
                ItemBasket itemBasket = new ItemBasket();
                itemBasket.setItemOptionId(itemOptionId.get(i));
                itemBasket.setQuantity(quantity.get(i));
                itemBaskets.add(itemBasket);
            }
        }

        for(ItemBasket itemBasket : itemBaskets){
            if(itemBasket.getItemOptionId() != null){
                basketRepository.save(new Basket(user.getId(),itemBasket.getItemOptionId(),itemBasket.getQuantity()));

                ItemOption itemOption = itemRepository.findOptionById(itemBasket.getItemOptionId()).get();
                Long itemOptionQuantity = itemOption.getQuantity() - itemBasket.getQuantity();
                itemOption.setQuantity(itemOptionQuantity);
                itemRepository.updateOption(itemOption.getId(),itemOption);
            }
        }

        List<ItemBasket> itemBasketList = createBasketList(user);
        for(ItemBasket itemBasket : itemBasketList){
            priceTotal += itemBasket.getTotalPrice() * itemBasket.getQuantity();
            priceTotalDisCount += itemBasket.getTotalPriceDiscount() * itemBasket.getQuantity();
        }

        priceTotalDisCountDelivery += priceTotalDisCount + DataInit.deliveryFee;
        model.addAttribute("priceTotalDisCountDelivery",priceTotalDisCountDelivery);
        OrderList orderList = new OrderList();
        orderList.setPayment("예치금");

        model.addAttribute("user",user);
        model.addAttribute("priceTotal",priceTotal);
        model.addAttribute("priceTotalDisCount",priceTotalDisCount);
        model.addAttribute("DisCount",priceTotal-priceTotalDisCount);
        model.addAttribute("itemBasketList",itemBasketList);
        model.addAttribute("orderList",orderList);

        return "buy/payment";
    }

    // 주문 동의서 체크
    @GetMapping("/payment/redirect")
    public String paymentRedirect(@Login User loginUser, RedirectAttributes redirectAttributes, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        redirectAttributes.addAttribute("status",true);
        return "redirect:/buy/payment";
    }

    // 결제 삭제
    @GetMapping("/payment/delete/{basketId}")
    public String deletePay(@Login User loginUser, @PathVariable Long basketId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        Basket basket = basketRepository.findById(basketId).get();
        ItemOption itemOption = itemRepository.findOptionById(basket.getItemOptionId()).get();
        itemOption.setQuantity(itemOption.getQuantity() + basket.getQuantity());
        itemRepository.updateOption(basket.getItemOptionId(), itemOption);
        basketRepository.deleteBasket(basketId);

        return "redirect:/buy/payment";
    }

    // 결제 완료
    @GetMapping("/paymentCompleted")
    public String paymentCompletedGet(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        return "buy/paymentCompleted";
    }

    @PostMapping("/paymentCompleted")
    public String paymentCompleted(@Login User loginUser, @ModelAttribute("payment") String payment, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        
        // 총 금액과 사용자 예치금 비교 후 orderList 저장
        // 예치금 부족시 결제 x 후 다른페이지
        OrderList orderList = new OrderList();
        orderList.setUserId(user.getId());
        orderList.setPayment(OrderPayment.deposit.getDescription());

        double priceTotalDisCount = 0;
        double priceTotalDisCountDelivery = 0;

        List<ItemBasket> itemBasketList = createBasketList(user);
        for(ItemBasket itemBasket : itemBasketList){
            priceTotalDisCount += itemBasket.getTotalPriceDiscount() * itemBasket.getQuantity();
        }
        priceTotalDisCountDelivery += priceTotalDisCount + DataInit.deliveryFee;

        // 사용자 예치금과 총 금액 비교
        if(user.getDeposit() < priceTotalDisCountDelivery){
            model.addAttribute("status",true);
            return "buy/paymentCompleted";
        }

        // orderList 저장
        orderList.setTotalPrice(priceTotalDisCountDelivery);
        buyRepository.ListSave(orderList);
        List<OrderList> orderListList = buyRepository.ListfindByUserId(user.getId());
        OrderList orderSave = orderListList.get(orderListList.size()-1);

        // orderDetail 저장
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(ItemBasket itemBasket : itemBasketList){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderListId(orderSave.getId());
            orderDetail.setItemOptionId(itemBasket.getItemOptionId());
            orderDetail.setQuantity(itemBasket.getQuantity());
            orderDetail.setTotalPrice(itemBasket.getTotalPriceDiscount() * itemBasket.getQuantity());
            buyRepository.DetailSave(orderDetail);
        }
        
        // 장바구니 항목 삭제
        basketRepository.deleteByUserId(user.getId());

        // 사용자 적립금액, 예치금액 수정, 사용자 등급 검사 및 수정
        user.setAmount(user.getAmount()+priceTotalDisCountDelivery);
        user.setDeposit(user.getDeposit()-priceTotalDisCountDelivery);

        if(user.getGrade().equals(DataInit.NORMAL)){
            if(user.getAmount() > DataInit.VIPLimitedAmount){
                user.setGrade(DataInit.VIP);
                user.setRatio(DataInit.VIPRATIO);
                model.addAttribute("vip",true);
            }
        }

        userRepository.update(user.getId(),user);

        model.addAttribute("user",user);
        return "buy/paymentCompleted";
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

    @ModelAttribute("OrderPayment")
    public OrderPayment[] OrderPayment(){
        return OrderPayment.values();
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
}
