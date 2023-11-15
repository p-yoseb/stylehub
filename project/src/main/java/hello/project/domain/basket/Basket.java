package hello.project.domain.basket;

import lombok.Data;

@Data
public class Basket {
    private Long id;
    private Long userId;
    private Long itemOptionId;
    private Long quantity;

    public Basket() {
    }

    public Basket(Long userId, Long itemOptionId, Long quantity) {
        this.userId = userId;
        this.itemOptionId = itemOptionId;
        this.quantity = quantity;
    }
}
