package hello.project.domain.item.item;

import lombok.Data;

@Data
public class ItemFindAll {
    private Long itemId;
    private Long itemOptionId;
    private Long categoryId;
    private String categoryTitle;
    private String category;
    private String title;
    private String explanation;
    private String imageName1;
    private String imageName2;
    private double price;
    private double priceDiscount;

    public ItemFindAll() {
    }

    public ItemFindAll(Long itemId, Long itemOptionId, Long categoryId, String categoryTitle, String category, String title, String explanation, String imageName1, String imageName2, double price, double priceDiscount) {
        this.itemId = itemId;
        this.itemOptionId = itemOptionId;
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.category = category;
        this.title = title;
        this.explanation = explanation;
        this.imageName1 = imageName1;
        this.imageName2 = imageName2;
        this.price = price;
        this.priceDiscount = priceDiscount;
    }
}
