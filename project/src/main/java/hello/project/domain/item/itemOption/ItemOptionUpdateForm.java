package hello.project.domain.item.itemOption;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemOptionUpdateForm {
    @NotBlank
    private String name;
    @NotNull
    private double price;
    @NotNull
    private Long quantity;

    public ItemOptionUpdateForm() {
    }

    public ItemOptionUpdateForm(String name, double price, Long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
