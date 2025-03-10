package ru.job4j.todo.repository.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final CrudRepository crudRepository;

    public List<Category> getAllCategory() {
        return crudRepository.query("from Category", Category.class);
    }

    public Optional<Category> getCategoryById(int id) {
        return crudRepository.optional(
                "from Category where id = :fId", Category.class,
                Map.of("fId", id));
    }
}
