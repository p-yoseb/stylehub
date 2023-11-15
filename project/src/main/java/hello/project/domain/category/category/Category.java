package hello.project.domain.category.category;

import lombok.Data;

@Data
public class Category {
    private Long id;
    private Long titleId;
    private String category;

    public Category() {
    }

    public Category(Long titleId, String category) {
        this.titleId = titleId;
        this.category = category;
    }
}
