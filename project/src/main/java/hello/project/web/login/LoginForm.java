package hello.project.web.login;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
