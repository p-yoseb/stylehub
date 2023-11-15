package hello.project.config;

import hello.project.repository.basket.BasketRepository;
import hello.project.repository.basket.mybatisBasket.BasketMapper;
import hello.project.repository.basket.mybatisBasket.MyBatisBasketRepository;
import hello.project.repository.buy.BuyRepository;
import hello.project.repository.buy.mybatisBuy.BuyMapper;
import hello.project.repository.buy.mybatisBuy.MyBatisBuyRepository;
import hello.project.repository.category.CategoryRepository;
import hello.project.repository.category.mybatisCategory.CategoryMapper;
import hello.project.repository.category.mybatisCategory.MyBatisCategoryRepository;
import hello.project.repository.item.ItemRepository;
import hello.project.repository.item.mybatisItem.ItemMapper;
import hello.project.repository.item.mybatisItem.MyBatisItemRepository;
import hello.project.repository.user.UserRepository;
import hello.project.repository.user.mybatisUser.MyBatisUserRepository;
import hello.project.repository.user.mybatisUser.UserMapper;
import hello.project.web.LoginCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final CategoryMapper categoryMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final BasketMapper basketMapper;
    private final BuyMapper buyMapper;

    @Bean
    public CategoryRepository categoryRepository(){
        return new MyBatisCategoryRepository(categoryMapper);
    }

    @Bean
    public ItemRepository itemRepository(){
        return new MyBatisItemRepository(itemMapper);
    }

    @Bean
    public UserRepository userRepository(){ return new MyBatisUserRepository(userMapper); }

    @Bean
    public BasketRepository basketRepository(){return new MyBatisBasketRepository(basketMapper);
    }

    @Bean
    public BuyRepository buyRepository(){return new MyBatisBuyRepository(buyMapper);
    }

    @Bean
    public LoginCheck loginCheck(){return new LoginCheck(userRepository());}

}
