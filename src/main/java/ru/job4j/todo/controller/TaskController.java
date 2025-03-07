package ru.job4j.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final PriorityService priorityService;

    @GetMapping
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks/list";
    }

    @GetMapping("/tasks/add")
    public String addTask(Model model) {
        model.addAttribute("priorities", priorityService.getAllPriorities());
        return "tasks/add";
    }

    @PostMapping("/tasks/add")
    public String addTask(@RequestParam("title") String title,
                          @RequestParam("description") String description,
                          @RequestParam("priority") int priorityId,
                          HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        Optional<Priority> priorityOptional = priorityService.getPriorityById(priorityId);

        Task task = Task.builder()
                .title(title)
                .description(description)
                .created(LocalDateTime.now())
                .done(false)
                .user(user)
                .priority(priorityOptional.get())
                .build();

        taskService.addTask(task);

        return "redirect:/";
    }

    @GetMapping("/tasks/{id}")
    public String getTaskById(@PathVariable int id, Model model) {
        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным id не найдено");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @GetMapping("/tasks/complete/{id}")
    public String completeTaskById(@PathVariable int id, Model model) {
        boolean updated = taskService.markTaskAsDone(id);

        if (!updated) {
            model.addAttribute("message", "Задание с указанным id не найдено");
            return "errors/404";
        }

        return "redirect:/";
    }

    @GetMapping("/tasks/update/{id}")
    public String showUpdatePage(@PathVariable int id,
                                 Model model) {

        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (taskOptional.isPresent()) {
            int defaultPriorityId = taskOptional.get().getPriority().getId();
            model.addAttribute("defaultPriorityId", defaultPriorityId);
            model.addAttribute("task", taskOptional.get());
            model.addAttribute("priorities", priorityService.getAllPriorities());
            return "tasks/update";
        }

        model.addAttribute("message", "Задание с указанным id не найдено");
        return "errors/404";
    }

    @PostMapping("/tasks/update")
    public String updateTask(@RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("priority") int priorityId,
                             @RequestParam("taskId") int taskId,
                             HttpServletRequest request,
                             Model model) {

        User user = (User) request.getAttribute("user");

        Optional<Priority> priorityOptional = priorityService.getPriorityById(priorityId);

        Optional<Task> existingTaskOptional = taskService.getTaskById(taskId);

        Task task = existingTaskOptional.get();
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priorityOptional.get());
        task.setUser(user);

        Optional<Task> updatedTask = taskService.updateTask(task);

        if (updatedTask.isEmpty()) {
            model.addAttribute("message", "Ошибка: Не удалось обновить задачу.");
            return "errors/404";
        }

        return "redirect:/";
    }

    @GetMapping("/tasks/delete/{id}")
    public String deleteTaskById(@PathVariable int id, Model model) {
        boolean isDeleted = taskService.deleteTaskById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задание с указанным id не найдено");
            return "errors/404";
        }
        return "redirect:/";
    }

    @GetMapping("/tasks/completed")
    public String getCompletedTasks(Model model) {
        List<Task> completedTasks = taskService.getCompletedTasks();
        model.addAttribute("tasks", completedTasks);
        return "tasks/completed";
    }

    @GetMapping("/tasks/uncompleted")
    public String showUncompletedTasks(Model model) {
        List<Task> uncompletedTasks = taskService.getUncompletedTasks();
        model.addAttribute("tasks", uncompletedTasks);
        return "tasks/uncompleted";
    }
}
