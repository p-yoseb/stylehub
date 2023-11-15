package hello.project.domain.basket;

import lombok.Data;

@Data
public class ItemBasket {
    private Long userId;
    private Long itemOptionId;
    private String name;
    private Long quantity;
    private double price;
    private double totalPrice;
    private double totalPriceDiscount;
    private boolean status;
    private String itemImage;
    private Long basketId;
    private Long orderDetailId;
    private Long itemId;

    public ItemBasket() {
    }

    public ItemBasket(Long userId, Long itemOptionId, String name, Long quantity, double totalPrice, double totalPriceDiscount, boolean status) {
        this.userId = userId;
        this.itemOptionId = itemOptionId;
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.totalPriceDiscount = totalPriceDiscount;
        this.status = status;
    }
}
