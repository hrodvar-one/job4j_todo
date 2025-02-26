package ru.job4j.todo.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.CrudRepository;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final CrudRepository crudRepository;

    /**
     * Регистрация нового пользователя и сохранение в БД.
     * @param user новый пользователь.
     * @return Optional or user.
     */
    public Optional<User> save(User user) {
        Optional<User> existingUser = crudRepository.optional(
                "FROM User WHERE login = :login", User.class,
                Map.of("login", user.getLogin())
        );

        if (existingUser.isPresent()) {
            return Optional.empty();
        }

        crudRepository.run(session -> session.save(user));

        return Optional.of(user);
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
