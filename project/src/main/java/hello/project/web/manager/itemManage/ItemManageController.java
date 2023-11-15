package hello.project.web.manager.itemManage;

import hello.project.dataInit.DataInit;
import hello.project.domain.category.CategoryList;
import hello.project.domain.category.ageCategory.AgeCategory;
import hello.project.domain.category.category.Category;
import hello.project.domain.category.category.CategorySecond;
import hello.project.domain.category.categoryTitle.CategoryTitle;
import hello.project.domain.item.ItemSearch;
import hello.project.domain.item.item.*;
import hello.project.domain.item.itemOption.ItemOption;
import hello.project.domain.item.itemOption.ItemOptionSaveForm;
import hello.project.domain.item.itemOption.ItemOptionUpdateForm;
import hello.project.domain.user.user.User;
import hello.project.repository.category.CategoryRepository;
import hello.project.repository.item.ItemRepository;
import hello.project.repository.user.UserRepository;
import hello.project.web.LoginCheck;
import hello.project.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.attoparser.trace.MarkupTraceEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/manager/item")
@RequiredArgsConstructor
public class ItemManageController {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final LoginCheck loginCheckComplete;

    // #18. 상품 관리 페이지
    @GetMapping
    public String items(@Login User loginUser, @ModelAttribute("itemSearch") ItemSearch itemSearch, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        List<Item> items = itemRepository.findAll(itemSearch);
        List<ItemCategory> itemCategory = new ArrayList<>();
        for(Item item : items){
            itemCategory.add(new ItemCategory(findCategoryName(item),item));
        }
        model.addAttribute("itemCategory",itemCategory);
        return "manager/itemManage/itemManage";
    }

    // #19. 상품 추가 페이지
    @GetMapping("/add")
    public String add(@Login User loginUser, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        model.addAttribute("item",new Item());
        model.addAttribute("itemOption", new ItemOption());
        return "manager/itemManage/addItemManage";
    }

    @PostMapping("/add")
    public String addItem(@Login User loginUser, @Validated @ModelAttribute("item") ItemSaveform form, BindingResult bindingResultItem, @Validated @ModelAttribute("itemOption") ItemOptionSaveForm formOption, BindingResult bindingResultItemOption, RedirectAttributes redirectAttributes, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        if (bindingResultItem.hasErrors()) {
            return "manager/itemManage/addItemManage";
        }

        if (bindingResultItemOption.hasErrors()) {
            return "manager/itemManage/addItemManage";
        }

        Item item = new Item();
        item.setCategoryId(form.getCategoryId());
        item.setTitle(form.getTitle());
        item.setImageName1(form.getImageName1());
        item.setImageName2(form.getImageName2());
        item.setExplanation(form.getExplanation());

        Item itemSave = itemRepository.save(item);

        ItemOption itemOption = new ItemOption();
        itemOption.setName(formOption.getName());
        itemOption.setPrice(formOption.getPrice());
        itemOption.setQuantity(formOption.getQuantity());

        itemOption.setItemId(itemSave.getId());
        itemRepository.saveOption(itemOption);

        redirectAttributes.addAttribute("itemId",item.getId());
        return "redirect:/manager/item/detail/{itemId}";
    }

    // #19-1. 상품 옵션 추가 페이지
    @GetMapping("/add/option/{itemId}")
    public String addItemOption(@Login User loginUser, @PathVariable Long itemId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        model.addAttribute("itemOption",new ItemOption());
        model.addAttribute("itemId", itemId);
        return "manager/itemManage/addItemOptionManage";
    }

    @PostMapping("/add/option/{itemId}")
    public String addItemOptionPost(@Login User loginUser, @PathVariable Long itemId,@Validated @ModelAttribute("itemOption") ItemOptionSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        if (bindingResult.hasErrors()) {
            return "manager/itemManage/addItemOptionManage";
        }

        ItemOption itemOption = new ItemOption();
        itemOption.setName(form.getName());
        itemOption.setPrice(form.getPrice());
        itemOption.setQuantity(form.getQuantity());
        itemOption.setItemId(itemId);
        itemRepository.saveOption(itemOption);
        redirectAttributes.addAttribute("itemId",itemId);
        return "redirect:/manager/item/detail/{itemId}";
    }

    // #20. 상품 상세 페이지
    @GetMapping("/detail/{itemId}")
    public String detail(@Login User loginUser, @PathVariable Long itemId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        Item item = itemRepository.findById(itemId).get();
        List<ItemOption> itemOptions = itemRepository.findOptionByItemId(itemId);

        ItemCategory itemCategory = new ItemCategory(findCategoryName(item),item);
        categoryRepository.findById(item.getId());
        log.info("categoryName={}",categoryRepository.findById(item.getId()));

        model.addAttribute("item",item);
        model.addAttribute("itemOptions",itemOptions);
        model.addAttribute("itemCategory",itemCategory);

        return "manager/itemManage/detailItemManage";
    }

    // #21. 상품 수정 페이지
    @GetMapping("/edit/{itemId}")
    public String edit(@Login User loginUser, @PathVariable Long itemId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        Item item = itemRepository.findById(itemId).get();
        List<ItemOption> itemOptions = itemRepository.findOptionByItemId(itemId);

        model.addAttribute("item",item);
        model.addAttribute("itemOptions",itemOptions);
        return "manager/itemManage/editItemManage";
    }

    @PostMapping("/edit/{itemId}")
    public String editPost(@Login User loginUser, @PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateform form, BindingResult bindingResult, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        if (bindingResult.hasErrors()) {
            return "manager/itemManage/editItemManage";
        }

        Item item = new Item();
        item.setTitle(form.getTitle());
        item.setCategoryId(form.getCategoryId());
        item.setImageName1(form.getImageName1());
        item.setImageName2(form.getImageName2());
        item.setExplanation(form.getExplanation());

        itemRepository.update(itemId, item);
        return "redirect:/manager/item/detail/{itemId}";
    }

    // #21-1. 상품 옵션 수정 페이지
    @GetMapping("/edit/option/{itemOptionId}")
    public String editOption(@Login User loginUser, @PathVariable("itemOptionId") Long itemOptionId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        ItemOption itemOption = itemRepository.findOptionById(itemOptionId).get();
        model.addAttribute("itemOption",itemOption);
        model.addAttribute("itemId",itemOption.getItemId());
        return "manager/itemManage/editItemOptionManage";
    }

    @PostMapping("/edit/option/{itemOptionId}")
    public String editOptionPost(@Login User loginUser, @PathVariable("itemOptionId") Long itemOptionId, @Validated @ModelAttribute("itemOption") ItemOptionUpdateForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        if (bindingResult.hasErrors()) {
            Long itemId = itemRepository.findOptionById(itemOptionId).get().getItemId();
            model.addAttribute("itemId",itemId);
            return "manager/itemManage/editItemOptionManage";
        }
        
        ItemOption itemOption = new ItemOption();
        itemOption.setName(form.getName());
        itemOption.setPrice(form.getPrice());
        itemOption.setQuantity(form.getQuantity());

        itemRepository.updateOption(itemOptionId,itemOption);
        ItemOption itemOptionFind = itemRepository.findOptionById(itemOptionId).get();
        redirectAttributes.addAttribute("itemId",itemOptionFind.getItemId());
        return "redirect:/manager/item/detail/{itemId}";
    }

    // 상품 삭제
    @GetMapping("/delete/{itemId}")
    public String delete(@Login User loginUser, @PathVariable Long itemId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        itemRepository.deleteItemOption(itemId);
        itemRepository.deleteItem(itemId);
        return "redirect:/manager/item/";
    }

    // 상품 옵션 삭제
    @GetMapping("/delete/option/{itemOptionId}")
    public String deleteOption(@Login User loginUser, @PathVariable Long itemOptionId, RedirectAttributes redirectAttributes, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        Long itemId = itemRepository.findOptionById(itemOptionId).get().getItemId();
        itemRepository.deleteItemOptionId(itemOptionId);
        redirectAttributes.addAttribute("itemId",itemId);
        return "redirect:/manager/item/detail/{itemId}";
    }

    // 전체 카테고리 관련
    @ModelAttribute("categories")
    public List<CategorySecond> categories(){
        return categoryRepository.findAll();
    }

    @ModelAttribute("itemFindAll")
    public ItemFindAll itemFindAll(){
        return new ItemFindAll();
    }

    private String findCategoryName(Item item) {
        return categoryRepository.findById(item.getCategoryId()).get().getCategoryTitle()+"-"+categoryRepository.findById(item.getCategoryId()).get().getCategory();
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
