package hello.project.repository.user.mybatisUser;

import hello.project.domain.user.user.User;
import hello.project.domain.user.user.UserSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    void save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByLoginId(String loginId);

    List<User> findAll(UserSearch userSearch);

    void deleteUser(Long userId);

    void update(@Param("userId") Long userId, @Param("user") User user);

    Optional<User> findByLonginId(String loginId);

    Integer countGender(String gender);
}
