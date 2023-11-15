package hello.project.domain.buy.orderList;

import lombok.Data;

@Data
public class OrderList {
    private Long id;
    private Long userId;
    private String payment;
    private Double totalPrice;

    public OrderList() {
    }

    public OrderList(Long userId, String payment, Double totalPrice) {
        this.userId = userId;
        this.payment = payment;
        this.totalPrice = totalPrice;
    }
}
