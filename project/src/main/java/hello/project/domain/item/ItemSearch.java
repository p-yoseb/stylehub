package hello.project.domain.item;

import lombok.Data;

@Data
public class ItemSearch {
    private String itemName;

    public ItemSearch() {
    }

    public ItemSearch(String itemName) {
        this.itemName = itemName;
    }
}
