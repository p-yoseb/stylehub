package hello.project.repository.category.mybatisCategory;

import hello.project.domain.category.ageCategory.AgeCategory;
import hello.project.domain.category.category.Category;
import hello.project.domain.category.category.CategorySecond;
import hello.project.domain.category.categoryTitle.CategoryTitle;
import hello.project.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisCategoryRepository implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategorySecond> findAll() {
        return categoryMapper.findAll();
    }

    @Override
    public Optional<CategorySecond> findById(Long id) {
        return categoryMapper.findById(id);
    }

    @Override
    public AgeCategory AgeCategoryFindByAge(Long age) {
        return categoryMapper.AgeCategoryFindByAge(age);
    }

    @Override
    public CategoryTitle CategoryTitleFindById(Long titleId) {
        return categoryMapper.CategoryTitleFindById(titleId);
    }

    @Override
    public List<Category> CategoryFindByTitleId(Long titleId) {
        return categoryMapper.CategoryFindByTitleId(titleId);
    }

    @Override
    public List<Category> CategoryFindById(Long categoryId) {
        return categoryMapper.CategoryFindById(categoryId);
    }

    @Override
    public List<CategoryTitle> categoryTitleFindAll() {
        return categoryMapper.categoryTitleFindAll();
    }
}
