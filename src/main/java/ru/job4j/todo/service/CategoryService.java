package ru.job4j.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.category.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategory();
    }

    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.getCategoryById(id);
    }

    public List<Category> getCategoriesById(List<Integer> ids) {
        return categoryRepository.getCategoriesById(ids);
    }
}
