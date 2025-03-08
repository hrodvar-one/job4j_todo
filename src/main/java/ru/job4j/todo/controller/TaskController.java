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
    public String addTask(@ModelAttribute Task task,
                          @RequestParam("priority.id") int priorityId,
                          HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        Optional<Priority> priorityOptional = priorityService.getPriorityById(priorityId);

        task.setUser(user);
        task.setPriority(priorityOptional.get());

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
    public String updateTask(@ModelAttribute Task task,
                             @RequestParam("priority.id") int priorityId,
                             HttpServletRequest request,
                             Model model) {

        User user = (User) request.getAttribute("user");

        Optional<Priority> priorityOptional = priorityService.getPriorityById(priorityId);

        task.setUser(user);
        task.setPriority(priorityOptional.get());

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
