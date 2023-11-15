package hello.project.web.manager.advertiseAnalysis;

import hello.project.dataInit.DataInit;
import hello.project.domain.category.CategoryList;
import hello.project.domain.category.ageCategory.AgeCategory;
import hello.project.domain.category.categoryTitle.CategoryTitle;
import hello.project.domain.item.item.ItemFindAll;
import hello.project.domain.user.user.User;
import hello.project.repository.category.CategoryRepository;
import hello.project.repository.user.UserRepository;
import hello.project.web.LoginCheck;
import hello.project.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AdvertiseController {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LoginCheck loginCheckComplete;

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
