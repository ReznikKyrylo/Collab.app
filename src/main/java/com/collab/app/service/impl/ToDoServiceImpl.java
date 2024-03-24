package com.collab.app.service.impl;

import com.collab.app.exception.NullEntityReferenceException;
import com.collab.app.model.ToDo;
import com.collab.app.repository.ToDoRepository;
import com.collab.app.service.ToDoService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository todoRepository;

    public ToDoServiceImpl(ToDoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public ToDo create(ToDo todo) {
        try {
            return todoRepository.save(todo);
        } catch (RuntimeException e) {
            throw new NullEntityReferenceException("To-Do cannot be 'null'");
        }
    }

    @Override
    public ToDo readById(long id) {
        Optional<ToDo> optional = todoRepository.findById(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("To-Do with id " + id + " not found"));
    }

    @Override
    public ToDo update(ToDo todo) {
        if (todo != null) {
            ToDo oldTodo = readById(todo.getId());
            if (oldTodo != null) {
                return todoRepository.save(todo);
            }
        }
        throw new NullEntityReferenceException("To-Do cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        ToDo todo = readById(id);
        if (todo != null) {
            todoRepository.delete(todo);
        } else {
            throw new EntityNotFoundException("To-Do with id " + id + " not found");
        }
    }

    @Override
    public List<ToDo> getAll() {
        List<ToDo> todos = todoRepository.findAll();
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        List<ToDo> todos = todoRepository.getByUserId(userId);
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }

    @Override
    public void deleteCollaborator(long collaborator_id, long todo_id) {
        todoRepository.deleteCollaborator(collaborator_id, todo_id);
    }
}
