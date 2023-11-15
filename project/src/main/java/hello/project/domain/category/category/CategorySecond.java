package hello.project.domain.category.category;

import lombok.Data;

@Data
public class CategorySecond {
    private Long id;
    private String categoryTitle;
    private String category;

    public CategorySecond() {
    }

    public CategorySecond(String categoryTitle, String category) {
        this.categoryTitle = categoryTitle;
        this.category = category;
    }
}
