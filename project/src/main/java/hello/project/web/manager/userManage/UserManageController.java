package hello.project.web.manager.userManage;

import hello.project.dataInit.DataInit;
import hello.project.dataInit.Gender;
import hello.project.domain.category.CategoryList;
import hello.project.domain.category.ageCategory.AgeCategory;
import hello.project.domain.category.category.CategorySecond;
import hello.project.domain.category.categoryTitle.CategoryTitle;
import hello.project.domain.item.ItemSearch;
import hello.project.domain.item.item.Item;
import hello.project.domain.item.item.ItemCategory;
import hello.project.domain.item.item.ItemFindAll;
import hello.project.domain.item.itemOption.ItemOption;
import hello.project.domain.user.user.User;
import hello.project.domain.user.user.UserSearch;
import hello.project.domain.user.user.UserUpdateForm;
import hello.project.repository.category.CategoryRepository;
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
@RequestMapping("/manager/user")
@RequiredArgsConstructor
public class UserManageController {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LoginCheck loginCheckComplete;

    // 회원 관리 페이지
    @GetMapping
    public String users(@Login User loginUser, @ModelAttribute("userSearch") UserSearch userSearch, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        List<User> users = userRepository.findAll(userSearch);
        model.addAttribute("users",users);
        return "manager/userManage/userManage";
    }

    // 회원 상세 페이지
    @GetMapping("/detail/{userId}")
    public String detailUser(@Login User loginUser, @PathVariable Long userId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        User findUser = userRepository.findById(userId).get();

        ItemCategory itemCategory = new ItemCategory(findCategoryName(findUser));
        categoryRepository.findById(findUser.getId());

        model.addAttribute("findUser",findUser);
        model.addAttribute("itemCategory",itemCategory);
        return "manager/userManage/detailUserManage";
    }

    // 회원 삭제
    @GetMapping("/delete/{userId}")
    public String delete(@Login User loginUser, @PathVariable Long userId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        userRepository.deleteUser(userId);

        return "redirect:/manager/user/";
    }

    // 회원 수정 페이지
    @GetMapping("/edit/{userId}")
    public String edit(@Login User loginUser, @PathVariable Long userId, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        User findUser = userRepository.findById(userId).get();

        model.addAttribute("findUser",findUser);
        return "manager/userManage/editUserManage";
    }

    @PostMapping("/edit/{userId}")
    public String editPost(@Login User loginUser, @PathVariable Long userId, @Validated @ModelAttribute("user") UserUpdateForm form, BindingResult bindingResult, Model model){
        User user = loginCheckComplete.Check(loginUser,model);
        model.addAttribute("user",user);

        if (bindingResult.hasErrors()) {
            return "manager/userManage/editUserManage";
        }

        User findUser = userRepository.findById(userId).get();
        findUser.setName(form.getName());
        findUser.setPassword(form.getPassword());
        findUser.setYear(form.getYear());
        findUser.setMonth(form.getMonth());
        findUser.setDay(form.getDay());
        findUser.setGender(form.getGender());
        findUser.setAddress(form.getAddress());
        findUser.setPostal(form.getPostal());
        findUser.setEmail(form.getEmail());
        findUser.setPhone(form.getPhone());
        findUser.setAdvertising(form.getAdvertising());
        findUser.setDeposit(form.getDeposit());
        findUser.setAmount(form.getAmount());
        findUser.setCategoryId(form.getCategoryId());

        userRepository.update(userId, findUser);

        return "redirect:/manager/user/detail/{userId}";
    }


    private String findCategoryName(User user) {
        return categoryRepository.findById(user.getCategoryId()).get().getCategoryTitle()+"-"+categoryRepository.findById(user.getCategoryId()).get().getCategory();
    }

    @ModelAttribute("genderList")
    public Gender[] gender(){
        return Gender.values();
    }

    @ModelAttribute("itemFindAll")
    public ItemFindAll itemFindAll(){
        return new ItemFindAll();
    }

    @ModelAttribute("categories")
    public List<CategorySecond> categories(){
        return categoryRepository.findAll();
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
