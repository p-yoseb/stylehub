package hello.project.repository.item;

import hello.project.domain.item.ItemSearch;
import hello.project.domain.item.item.Item;
import hello.project.domain.item.item.ItemFindAll;
import hello.project.domain.item.itemOption.ItemOption;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);

    ItemOption saveOption(ItemOption itemOption);

    Optional<Item> findById(Long id);

    List<ItemOption> findOptionByItemId(Long id);

    Optional<ItemOption> findOptionById(Long id);

    List<Item> findAll(ItemSearch search);

    Item update(Long itemId, Item item);

    ItemOption updateOption(Long itemOptionId, ItemOption itemOption);

    void deleteItem(Long id);

    void deleteItemOption(Long id);

    void deleteItemOptionId(Long id);

    List<ItemFindAll> itemFindAll(ItemFindAll itemFindAll);

    List<ItemFindAll> itemFindAllByCategoryId(Long categoryId);

    List<ItemOption> findPrice(Long itemId);

    Integer views();

}
