package ru.job4j.todo.repository.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.CrudRepository;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final CrudRepository crudRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    /**
     * Регистрация нового пользователя и сохранение в БД.
     * @param user новый пользователь.
     * @return Optional or user.
     */
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.save(user));
            return Optional.of(user);
        } catch (Exception e) {
            LOGGER.error("Ошибка при сохранении пользователя", e);
        }
        return Optional.empty();
    }

    /**
     * Поиск пользователя по логину и паролю.
     * @param login логин.
     * @param password пароль.
     * @return Optional or user.
     */
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "FROM User WHERE login = :login AND password = :password", User.class,
                Map.of("login", login, "password", password)
        );
    }
}
