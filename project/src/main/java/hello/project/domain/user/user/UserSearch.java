package hello.project.domain.user.user;

import lombok.Data;

@Data
public class UserSearch {
//    su-dong
    private String loginId;
    private String name;

    public UserSearch() {
    }

    public UserSearch(String loginId, String name) {
        this.loginId = loginId;
        this.name = name;
    }
}
