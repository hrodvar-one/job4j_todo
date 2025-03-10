package ru.job4j.todo.repository.task;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить все задачи с приоритетами и категориями.
     * @return список всех задач.
     */
    public List<Task> getAllTasks() {
        return crudRepository.query(
                """
                        SELECT DISTINCT t
                        FROM Task t
                        JOIN FETCH t.priority
                        JOIN FETCH t.categories
                        ORDER BY t.id, t.done DESC, t.created ASC
                        """,
                Task.class
        );
    }

    /**
     * Добавить новую задачу в базу.
     * @param task новая задача.
     * @return Optional or user.
     */
    public Optional<Task> addTask(Task task) {
        crudRepository.run(session -> session.persist(task));
        return Optional.of(task);
    }

    /**
     * Получить задачу по id с приоритетами и категориями.
     * @param id задачи.
     * @return Optional or task.
     */
    public Optional<Task> getTaskById(int id) {
        return crudRepository.optional(
                "FROM Task t JOIN FETCH t.priority LEFT JOIN FETCH t.categories WHERE t.id = :taskId", Task.class,
                Map.of("taskId", id)
        );
    }

    /**
     * Обновить задачу в базе.
     * @param task задача.
     * @return Optional or task.
     */
    public Optional<Task> updateTask(Task task) {
        crudRepository.run(session -> session.merge(task));
        return Optional.of(task);
    }

    /**
     * Удалить задачу по id.
     * @param id задачи.
     * @return boolean.
     */
    public boolean deleteTaskById(int id) {
        try {
            crudRepository.run(
                    "DELETE FROM Task WHERE id = :taskId",
                    Map.of("taskId", id)
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получить все выполненные задачи с приоритетами и категориями.
     * @return список всех выполненных задач.
     */
    public List<Task> getCompletedTasks() {
        return crudRepository.query(
                """
                        SELECT DISTINCT t
                        FROM Task t
                        JOIN FETCH t.priority
                        JOIN FETCH t.categories
                        WHERE t.done = true
                        ORDER BY t.done DESC, t.created ASC
                        """,
                Task.class
        );
    }

    /**
     * Получить все невыполненные задачи с приоритетами и категориями.
     * @return список всех невыполненных задач.
     */
    public List<Task> getUncompletedTasks() {
        return crudRepository.query(
                """
                        SELECT DISTINCT t
                        FROM Task t
                        JOIN FETCH t.priority
                        JOIN FETCH t.categories
                        WHERE t.done = false
                        ORDER BY t.done DESC, t.created ASC
                        """,
                Task.class
        );
    }

    /**
     * Установить статус задачи выполненной.
     * @param id задачи.
     * @return int.
     */
    public int updateTaskDoneById(int id) {
        return crudRepository.tx(session -> session.createQuery(
                "UPDATE Task t SET t.done = true WHERE t.id = :id"
                )
                .setParameter("id", id)
                .executeUpdate());
    }
}

