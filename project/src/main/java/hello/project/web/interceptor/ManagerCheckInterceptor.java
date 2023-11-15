package hello.project.web.interceptor;

import hello.project.domain.user.user.User;
import hello.project.repository.user.UserRepository;
import hello.project.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
public class ManagerCheckInterceptor implements HandlerInterceptor {
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse
            response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        log.info("manager 인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession(false);

        User user = new User();
        user = (User)session.getAttribute(SessionConst.LOGIN_USER);
        User userSave = userRepository.findById(user.getId()).get();

        if (session.getAttribute(SessionConst.LOGIN_USER) == null) {
            log.info("미인증 사용자 요청");
            //로그인으로 redirect
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }

        log.info("userSave.getAdmin()={}",userSave.getAdmin());

        if(!userSave.getAdmin()){
            log.info("관리자 사용자 요청");
            //로그인으로 redirect
            response.sendRedirect("/manager/false?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }
}
