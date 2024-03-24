package com.collab.app.service;

import com.collab.app.model.ToDo;

import java.util.List;

public interface ToDoService {
    ToDo create(ToDo todo);
    ToDo readById(long id);
    ToDo update(ToDo todo);
    void delete(long id);

    List<ToDo> getAll();
    List<ToDo> getByUserId(long userId);

    void deleteCollaborator(long collaborator_id,long todo_id);
}
