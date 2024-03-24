package com.collab.app.service.impl;

import com.collab.app.exception.NullEntityReferenceException;
import com.collab.app.model.Task;
import com.collab.app.repository.TaskRepository;
import com.collab.app.service.TaskService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Task user) {
        try {
            return taskRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new NullEntityReferenceException("Task cannot be 'null'");
        }
    }

    @Override
    public Task readById(long id) {
        Optional<Task> optional = taskRepository.findById(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public Task update(Task task) {
        if (task != null) {
            Task oldTask = readById(task.getId());
            if (oldTask != null) {
                return taskRepository.save(task);
            }
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Task task = readById(id);
        if (task != null) {
            taskRepository.delete(task);
        } else {
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }

    @Override
    public List<Task> getByTodoId(long todoId) {
        List<Task> tasks = taskRepository.getByTodoId(todoId);
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }
}
