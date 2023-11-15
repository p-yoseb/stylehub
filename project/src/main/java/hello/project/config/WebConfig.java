package hello.project.config;

import hello.project.repository.user.UserRepository;
import hello.project.repository.user.mybatisUser.UserMapper;
import hello.project.web.argumentresolver.LoginMemberArgumentResolver;
import hello.project.web.interceptor.LogInterceptor;
import hello.project.web.interceptor.LoginCheckInterceptor;
import hello.project.web.interceptor.ManagerCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserRepository userRepository;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/imgs/**");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "", "/", "/login/add", "/login","/item/detail/{}", "/item", "/item/{}", "/item/title/{}",
                        "/manager/false",
                        "/css/**", "/*.ico", "/error", "/imgs/**"
                );
        registry.addInterceptor(new ManagerCheckInterceptor(userRepository))
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "", "/", "/login/add", "/login","/item/detail/{}", "/item", "/item/{}", "/item/title/{}",
                        "/mypage", "/mypage/order", "/mypage/deleteOrder/{}", "/mypage/coupon", "/mypage/deposit",
                        "/mypage/interest", "/mypage/detail", "/mypage/detail/edit", "/mypage/detail/edit",
                        "/buy", "/buy/basket", "/buy/basket/delete/{}", "/buy/payment", "/buy/payment/redirect",
                        "/buy/payment/delete/{}", "/buy/paymentCompleted", "/logout", "/manager/false",
                        "/css/**", "/*.ico", "/error", "/imgs/**"
                );

    }


}
