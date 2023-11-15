package hello.project.repository.user.mybatisUser;

import hello.project.domain.user.user.User;
import hello.project.domain.user.user.UserSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import hello.project.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisUserRepository implements UserRepository {
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        userMapper.save(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return userMapper.findByLoginId(loginId);
    }

    @Override
    public List<User> findAll(UserSearch userSearch) {
        return userMapper.findAll(userSearch);
    }

    @Override
    public void deleteUser(Long userId) {
        userMapper.deleteUser(userId);
    }

    @Override
    public User update(Long userId, User user) {
        userMapper.update(userId, user);
        return user;
    }

    @Override
    public Optional<User> findByLonginId(String loginId) {
        return userMapper.findByLonginId(loginId);
    }

    @Override
    public Integer countGender(String gender) {
        return userMapper.countGender(gender);
    }
}
