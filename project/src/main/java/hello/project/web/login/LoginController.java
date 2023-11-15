package hello.project.web.login;

import hello.project.dataInit.DataInit;
import hello.project.dataInit.Gender;
import hello.project.domain.category.category.Category;
import hello.project.domain.category.category.CategorySecond;
import hello.project.domain.item.item.Item;
import hello.project.domain.item.itemOption.ItemOption;
import hello.project.domain.user.user.User;
import hello.project.repository.category.CategoryRepository;
import hello.project.repository.user.UserRepository;
import hello.project.service.user.login.LoginService;
import hello.project.web.SessionConst;
import hello.project.web.session.SessionManager;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.lang.reflect.Member;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LoginService loginService;

    //로그인페이지
    @GetMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm form, Model model) {
        model.addAttribute("loginError",false);
        return "login/login";
    }


    //회원가입페이지
    @GetMapping("/login/add")
    public String addUser(@ModelAttribute("user") User user, Model model) {

        model.addAttribute("item",new Item());
        return "login/addUser";
    }

    //회원가입페이지
    @PostMapping("/login/add")
    public String save(@Valid @ModelAttribute("user") User user, BindingResult result) {

        if (result.hasErrors())
            return "login/addUser";

        user.setAmount(0.0);
        user.setCoupon(5000.0);
        user.setRatio(0.0);
        user.setGrade(DataInit.NORMAL);
        user.setAdmin(false);

        userRepository.save(user);
        return "login/addUserCompleted";
    }


    //로그인페이지
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult
            bindingResult, @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request, Model model) {

        if (bindingResult.hasErrors()) {

            return "login/login";
        }

        User loginUser = loginService.login(form.getLoginId(),
                form.getPassword());
        log.info("login? {}", loginUser);

        if (loginUser == null) {
            log.info("bindingResult={}",bindingResult);
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            model.addAttribute("loginError",true);
            return "login/login";
        }
        //로그인 성공 처리 TODO

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate(); // 세션을 제거한다.
        }
        return "redirect:/";
    }


//    @ResponseBody
//    @GetMapping("/idCheck")
//    public int overlappedID(User user) throws Exception{
//        int result = memberService.overlappedID(memberVO); // 중복확인한 값을 int로 받음
//        return result;
//    }

    //회원가입완료페이지
    @GetMapping("/login/add/completed")
    public String Completed() {

        return "login/addUserCompleted";
    }
    @ModelAttribute("categories")
    public List<CategorySecond> categories(){
        return categoryRepository.findAll();
    }

    @ModelAttribute("genderList")
    public Gender[] gender(){
        return Gender.values();
    }
}
