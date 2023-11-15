package hello.project.web;

import hello.project.dataInit.DataInit;
import hello.project.domain.user.user.User;
import hello.project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class LoginCheck {
    private final UserRepository userRepository;

    public User Check(User loginUser, Model model){
        if (loginUser != null) {
            model.addAttribute("myPageCheck",true);
            User user = userRepository.findById(loginUser.getId()).get();
            if(user.getAdmin()){
                model.addAttribute("managerCheck",true);
            }
            return user;
        }
        return new User();
    }

    public Long age(int year){
        Long age;
        log.info("LocalDate.now().getYear()={}",LocalDate.now().getYear());
        int ageTest = LocalDate.now().getYear() - year + 1;

            if(ageTest>=40){
                age = 40L;
            }else if(ageTest>=30){
                age = 30L;
            }else if(ageTest>=20){
                age = 20L;
            }else{
                age = 10L;
            }

        return age;
    }
}
