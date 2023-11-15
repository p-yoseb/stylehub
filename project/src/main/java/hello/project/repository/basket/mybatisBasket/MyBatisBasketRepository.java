package hello.project.repository.basket.mybatisBasket;

import hello.project.domain.basket.Basket;
import hello.project.repository.basket.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisBasketRepository implements BasketRepository {
    private final BasketMapper basketMapper;

    @Override
    public void save(Basket basket) {
        basketMapper.save(basket);
    }

    @Override
    public List<Basket> findByUserId(Long userId) {
        return basketMapper.findByUserId(userId);
    }

    @Override
    public Optional<Basket> findById(Long basketId) {
        return basketMapper.findById(basketId);
    }

    @Override
    public void deleteBasket(Long basketId) {
        basketMapper.deleteBasket(basketId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        basketMapper.deleteByUserId(userId);
    }
}
