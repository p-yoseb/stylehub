package hello.project.repository.item.mybatisItem;

import hello.project.domain.item.ItemSearch;
import hello.project.domain.item.item.Item;
import hello.project.domain.item.item.ItemFindAll;
import hello.project.domain.item.itemOption.ItemOption;
import hello.project.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisItemRepository implements ItemRepository {
    private final ItemMapper itemMapper;

    @Override
    public Item save(Item item) {
        itemMapper.save(item);
        return item;
    }

    @Override
    public ItemOption saveOption(ItemOption itemOption) {
        itemMapper.saveOption(itemOption);
        return itemOption;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemMapper.findById(id);
    }

    @Override
    public List<ItemOption> findOptionByItemId(Long id) {
        return itemMapper.findOptionByItemId(id);
    }

    @Override
    public Optional<ItemOption> findOptionById(Long id) {
        return itemMapper.findOptionById(id);
    }

    @Override
    public List<Item> findAll(ItemSearch search) {
        return itemMapper.findAll(search);
    }

    @Override
    public Item update(Long itemId, Item item) {
        itemMapper.update(itemId,item);
        return item;
    }

    @Override
    public ItemOption updateOption(Long itemOptionId, ItemOption itemOption) {
        itemMapper.updateOption(itemOptionId, itemOption);
        return itemOption;
    }

    @Override
    public void deleteItem(Long id) {
        itemMapper.deleteItem(id);
    }

    @Override
    public void deleteItemOption(Long id) {
        itemMapper.deleteItemOption(id);
    }

    @Override
    public void deleteItemOptionId(Long id) {
        itemMapper.deleteItemOptionId(id);
    }

    @Override
    public List<ItemFindAll> itemFindAll(ItemFindAll itemFindAll) {
        return itemMapper.itemFindAll(itemFindAll);
    }

    @Override
    public List<ItemFindAll> itemFindAllByCategoryId(Long categoryId) {
        return itemMapper.itemFindAllByCategoryId(categoryId);
    }

    @Override
    public List<ItemOption> findPrice(Long itemId) {
        return itemMapper.findPrice(itemId);
    }

    @Override
    public Integer views() {
        return itemMapper.views();
    }
}
