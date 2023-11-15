package hello.project.repository.basket.mybatisBasket;

import hello.project.domain.basket.Basket;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BasketMapper {

    void save(Basket basket);

    List<Basket> findByUserId(Long userId);

    Optional<Basket> findById(Long basketId);

    void deleteBasket(Long basketId);

    void deleteByUserId(Long userId);
}
