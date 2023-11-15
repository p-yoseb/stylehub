package hello.project.domain.item.item;

import lombok.Data;

@Data
public class Item {
    private Long id;
    private String title;
    private Long categoryId;
    private String imageName1;
    private String imageName2;
    private String explanation;
    private Long views;

    public Item() {
    }

    public Item(String title, Long categoryId, String imageName1, String imageName2, String explanation) {
        this.title = title;
        this.categoryId = categoryId;
        this.imageName1 = imageName1;
        this.imageName2 = imageName2;
        this.explanation = explanation;
    }
}
