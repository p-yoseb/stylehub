package hello.project.domain.category.categoryTitle;

import lombok.Data;

@Data
public class CategoryTitle {
    private Long id;
    private String categoryTitle;

    public CategoryTitle() {
    }

    public CategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
