package hello.project.domain.category;

import hello.project.domain.category.category.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryList {
    private Long titleId;
    private String categoryTitle;
    private List<Category> categories;

    public CategoryList() {
    }

    public CategoryList(Long titleId, String categoryTitle, List<Category> categories) {
        this.titleId = titleId;
        this.categoryTitle = categoryTitle;
        this.categories = categories;
    }

}
