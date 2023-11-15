package hello.project.domain.item.item;

import hello.project.domain.user.user.User;
import lombok.Data;

@Data
public class ItemCategory {
    private Long id;
    private String title;
    private String categoryName;
    private String imageName1;
    private String imageName2;
    private String explanation;
    private Long views;

    public ItemCategory() {
    }

    public ItemCategory(String title, String categoryName, String imageName1, String imageName2, String explanation) {
        this.title = title;
        this.categoryName = categoryName;
        this.imageName1 = imageName1;
        this.imageName2 = imageName2;
        this.explanation = explanation;
    }

    public ItemCategory(String categoryName, Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.categoryName = categoryName;
        this.imageName1 = item.getImageName1();
        this.imageName2 = item.getImageName2();
        this.explanation = item.getExplanation();
        this.views = item.getViews();
    }

    public ItemCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
