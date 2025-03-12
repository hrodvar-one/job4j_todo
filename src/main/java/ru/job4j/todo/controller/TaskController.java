package ru.job4j.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.utility.TimeConversionUtility;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    @GetMapping
    public String getAllTasks(Model model, HttpServletRequest request) {

        User user = (User) request.getAttribute("user");
        List<Task> tasks = taskService.getAllTasks();

        model.addAttribute("tasks", TimeConversionUtility.timeConversion(user, tasks));
        return "tasks/list";
    }

    @GetMapping("/tasks/add")
    public String addTask(Model model) {
        model.addAttribute("priorities", priorityService.getAllPriorities());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "tasks/add";
    }

    @PostMapping("/tasks/add")
    public String addTask(@ModelAttribute Task task,
                          @RequestParam List<Integer> categoryIdsList,
                          HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        List<Category> categories = categoryService.getCategoriesById(categoryIdsList);

        task.setUser(user);
        task.setCategories(categories);
        task.setCreated(LocalDateTime.now(ZoneId.of("UTC")));
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
            Task task = taskOptional.get();

            int defaultPriorityId = task.getPriority().getId();
            model.addAttribute("defaultPriorityId", defaultPriorityId);
            model.addAttribute("task", taskOptional.get());
            model.addAttribute("priorities", priorityService.getAllPriorities());
            model.addAttribute("categories", categoryService.getAllCategories());

            List<Integer> selectedCategoryIds = task.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());

            model.addAttribute("selectedCategoryIds", selectedCategoryIds);

            return "tasks/update";
        }

        model.addAttribute("message", "Задание с указанным id не найдено");
        return "errors/404";
    }

    @PostMapping("/tasks/update")
    public String updateTask(@ModelAttribute Task task,
                             HttpServletRequest request,
                             @RequestParam List<Integer> categoryIdsList,
                             Model model) {

        User user = (User) request.getAttribute("user");

        List<Category> categories = new ArrayList<>();

        for (Integer categoryId : categoryIdsList) {
            categoryService.getCategoryById(categoryId).ifPresent(categories::add);
        }

        task.setUser(user);
        task.setCategories(categories);
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
    public String getCompletedTasks(Model model, HttpServletRequest request) {

        User user = (User) request.getAttribute("user");
        List<Task> completedTasks = taskService.getCompletedTasks();

        model.addAttribute("tasks", TimeConversionUtility.timeConversion(user, completedTasks));
        return "tasks/completed";
    }

    @GetMapping("/tasks/uncompleted")
    public String showUncompletedTasks(Model model, HttpServletRequest request) {

        User user = (User) request.getAttribute("user");
        List<Task> uncompletedTasks = taskService.getUncompletedTasks();

        model.addAttribute("tasks", TimeConversionUtility.timeConversion(user, uncompletedTasks));
        return "tasks/uncompleted";
    }
}
