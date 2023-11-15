package hello.project.repository.category;

import hello.project.domain.category.ageCategory.AgeCategory;
import hello.project.domain.category.category.Category;
import hello.project.domain.category.category.CategorySecond;
import hello.project.domain.category.categoryTitle.CategoryTitle;
import hello.project.domain.item.item.Item;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<CategorySecond> findAll();

    Optional<CategorySecond> findById(Long id);

    AgeCategory AgeCategoryFindByAge(Long age);

    CategoryTitle CategoryTitleFindById(Long titleId);

    List<Category> CategoryFindByTitleId(Long titleId);

    List<Category> CategoryFindById(Long categoryId);

    List<CategoryTitle> categoryTitleFindAll();
}
