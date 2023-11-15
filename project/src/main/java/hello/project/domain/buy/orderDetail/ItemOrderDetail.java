package hello.project.domain.buy.orderDetail;

import lombok.Data;

@Data
public class ItemOrderDetail {
    private Long userId;
    private Long itemOptionId;
    private String name;
    private Long quantity;
    private double totalPrice;
    private double totalPriceDiscount;
    private boolean status;

    public ItemOrderDetail() {
    }

    public ItemOrderDetail(Long userId, Long itemOptionId, String name, Long quantity, double totalPrice, double totalPriceDiscount, boolean status) {
        this.userId = userId;
        this.itemOptionId = itemOptionId;
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.totalPriceDiscount = totalPriceDiscount;
        this.status = status;
    }
}
