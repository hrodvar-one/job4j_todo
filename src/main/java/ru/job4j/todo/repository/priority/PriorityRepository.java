package ru.job4j.todo.repository.priority;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PriorityRepository {

    private final CrudRepository crudRepository;

    public List<Priority> getAllPriorities() {
        return crudRepository.query("from Priority", Priority.class);
    }

    public Optional<Priority> getPriorityById(int priorityId) {
        return crudRepository.optional(
                "from Priority where id = :fId",
                Priority.class,
                Map.of("fId", priorityId)
        );
    }
}
