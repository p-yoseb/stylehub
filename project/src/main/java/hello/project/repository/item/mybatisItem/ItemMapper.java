package hello.project.repository.item.mybatisItem;

import hello.project.domain.item.ItemSearch;
import hello.project.domain.item.item.Item;
import hello.project.domain.item.item.ItemFindAll;
import hello.project.domain.item.itemOption.ItemOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemMapper {
    void save(Item item);

    void saveOption(ItemOption itemOption);

    Optional<Item> findById(Long id);

    List<ItemOption> findOptionByItemId(Long id);

    Optional<ItemOption> findOptionById(Long id);

    List<Item> findAll(ItemSearch search);

    void update(@Param("itemId") Long itemId, @Param("item") Item item);

    void updateOption(@Param("itemOptionId") Long itemOptionId, @Param("itemOption") ItemOption itemOption);

    void deleteItem(Long id);

    void deleteItemOption(Long id);

    void deleteItemOptionId(Long id);

    List<ItemFindAll> itemFindAll(ItemFindAll itemFindAll);

    List<ItemFindAll> itemFindAllByCategoryId(Long categoryId);

    List<ItemOption> findPrice(Long itemId);

    Integer views();

}
