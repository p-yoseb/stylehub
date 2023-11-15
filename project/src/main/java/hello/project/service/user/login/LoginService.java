package hello.project.service.user.login;

import hello.project.domain.user.user.User;
import hello.project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    /**
     * @return null이면 로그인 실패
     */
    public User login(String loginId, String password) {
        return userRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
