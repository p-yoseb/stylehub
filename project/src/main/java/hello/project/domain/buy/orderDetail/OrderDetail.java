package hello.project.domain.buy.orderDetail;

import lombok.Data;

@Data
public class OrderDetail {
    private Long id;
    private Long orderListId;
    private Long itemOptionId;
    private Long quantity;
    private double totalPrice;

    public OrderDetail() {
    }

    public OrderDetail(Long orderListId, Long itemOptionId, Long quantity, double totalPrice) {
        this.orderListId = orderListId;
        this.itemOptionId = itemOptionId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
