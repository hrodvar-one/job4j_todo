package ru.job4j.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.priority.PriorityRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public List<Priority> getAllPriorities() {
        return priorityRepository.getAllPriorities();
    }

    public Optional<Priority> getPriorityById(int priorityId) {
        return priorityRepository.getPriorityById(priorityId);
    }
}
