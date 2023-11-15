package hello.project.repository.user;

import hello.project.domain.item.item.Item;
import hello.project.domain.user.user.User;
import hello.project.domain.user.user.UserSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByLoginId(String loginId);

    List<User> findAll(UserSearch userSearch);

    void deleteUser(Long userId);

    User update(Long userId, User user);

    Optional<User> findByLonginId(String loginId);

    Integer countGender(String gender);

}
