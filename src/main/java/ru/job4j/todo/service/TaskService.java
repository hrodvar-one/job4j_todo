package ru.job4j.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.task.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public Optional<Task> addTask(Task task) {
        return taskRepository.addTask(task);
    }

    public Optional<Task> getTaskById(int id) {
        return taskRepository.getTaskById(id);
    }

    public Optional<Task> updateTask(Task task) {
        return taskRepository.updateTask(task);
    }

    public boolean deleteTaskById(int id) {
        return taskRepository.deleteTaskById(id);
    }

    public List<Task> getCompletedTasks() {
        return taskRepository.getCompletedTasks();
    }

    public List<Task> getUncompletedTasks() {
        return taskRepository.getUncompletedTasks();
    }

    public boolean markTaskAsDone(int id) {
        int updatedRows = taskRepository.updateTaskDoneById(id);
        return updatedRows > 0;
    }
}
