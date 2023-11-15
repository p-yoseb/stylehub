package hello.project.repository.basket;

import hello.project.domain.basket.Basket;
import hello.project.domain.item.item.Item;

import java.util.List;
import java.util.Optional;

public interface BasketRepository {

    void save(Basket basket);

    List<Basket> findByUserId(Long userId);

    Optional<Basket> findById(Long basketId);

    void deleteBasket(Long basketId);

    void deleteByUserId(Long userId);
}
