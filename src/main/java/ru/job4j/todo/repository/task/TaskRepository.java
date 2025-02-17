package ru.job4j.todo.repository.task;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskRepository {

    private final SessionFactory sf;

    public List<Task> getAllTasks() {
        Session session = sf.openSession();
        Transaction tx = null;
        List<Task> tasks = new ArrayList<>();

        try {
            tx = session.beginTransaction();

            tasks = session.createQuery(
                            "FROM Task ORDER BY done DESC, created ASC", Task.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }

        return tasks;
    }

    public Optional<Task> addTask(Task task) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            session.save(task);

            tx.commit();
            return Optional.of(task);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            return Optional.empty();
        } finally {
            session.close();
        }
    }

    public Optional<Task> getTaskById(int id) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Task task = session.get(Task.class, id);

            tx.commit();
            return Optional.ofNullable(task);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public Optional<Task> updateTask(Task task) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Task existingTask = session.get(Task.class, task.getId());
            if (existingTask != null) {
                existingTask.setTitle(task.getTitle());
                existingTask.setDescription(task.getDescription());
                existingTask.setDone(task.isDone());
                session.update(existingTask);
                tx.commit();
                return Optional.of(existingTask);
            } else {
                tx.rollback();
                return Optional.empty();
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean deleteTaskById(int id) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Task task = session.get(Task.class, id);
            if (task != null) {
                session.delete(task);
                tx.commit();
                return true;
            } else {
                tx.rollback();
                return false;
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Task> getCompletedTasks() {
        Session session = sf.openSession();
        Transaction tx = null;
        List<Task> completedTasks = new ArrayList<>();

        try {
            tx = session.beginTransaction();

            completedTasks = session.createQuery(
                            "FROM Task WHERE done = true", Task.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }

        return completedTasks;
    }

    public List<Task> getUncompletedTasks() {
        Session session = sf.openSession();
        Transaction tx = null;
        List<Task> uncompletedTasks;

        try {
            tx = session.beginTransaction();

            uncompletedTasks = session.createQuery(
                            "FROM Task WHERE done = false", Task.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }

        return uncompletedTasks;
    }
}
