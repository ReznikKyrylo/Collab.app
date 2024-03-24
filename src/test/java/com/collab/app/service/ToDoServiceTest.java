package com.collab.app.service;

import com.collab.app.model.ToDo;
import com.collab.app.model.User;
import com.collab.app.repository.ToDoRepository;
import com.collab.app.service.impl.ToDoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ToDoServiceTest {

    @Mock
    private ToDoRepository toDoRepository;

    @InjectMocks
    private ToDoServiceImpl toDoService;


    @Test
    public void create_state() {

        ToDo toDo = new ToDo();
        toDo.setTitle("ToDo");

        when(toDoRepository.save(toDo)).thenReturn(toDo);

        Assertions.assertThat(toDoService.create(toDo))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(ToDo.class)
                .isEqualTo(toDo);

        verify(toDoRepository, times(1)).save(toDo);
        verifyNoMoreInteractions(toDoRepository);
    }


    @Test
    public void read_todo_by_id() {

        ToDo toDo = new ToDo();
        toDo.setTitle("ToDo");
        toDo.setId(1L);

        when(toDoRepository.findById(1L)).thenReturn(Optional.of(toDo));

        Assertions.assertThat(toDoService.readById(1L))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(ToDo.class)
                .isEqualTo(toDo);

        verify(toDoRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(toDoRepository);
    }

    @Test
    public void update_task() {

        ToDo toDo = new ToDo();
        toDo.setTitle("ToDo");
        toDo.setId(1L);

        when(toDoRepository.save(toDo)).thenReturn(toDo);

        ToDo updateToDo = toDoRepository.save(toDo);
        updateToDo.setTitle("UPDATE_TASK");

        when(toDoRepository.findById(1L)).thenReturn(Optional.of(toDo));

        Assertions.assertThat(toDoService.update(updateToDo))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(ToDo.class)
                .isEqualTo(updateToDo);

        verify(toDoRepository, times(1)).findById(1L);
        verify(toDoRepository, times(2)).save(updateToDo);
        verifyNoMoreInteractions(toDoRepository);
    }

    @Test
    public void delete_task() {

        ToDo toDo = new ToDo();
        toDo.setTitle("ToDo");
        toDo.setId(1L);

        doNothing().when(toDoRepository).delete(toDo);
        when(toDoRepository.findById(1L)).thenReturn(Optional.of(toDo));

        toDoService.delete(1L);

        verify(toDoRepository, times(1)).delete(toDo);
        verify(toDoRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(toDoRepository);

    }

    @Test
    public void get_all_state() {
        when(toDoRepository.findAll()).thenReturn(List.of(new ToDo(), new ToDo()));

        Assertions.assertThat(toDoService.getAll())
                .isNotEmpty()
                .isNotNull()
                .hasSize(2);
        verify(toDoRepository, times(1)).findAll();
        verifyNoMoreInteractions(toDoRepository);
    }

    @Test
    public void get_todo_by_user_id() {

        User user = new User();
        user.setId(2L);

        ToDo toDo = new ToDo();
        toDo.setId(3L);

        user.setMyTodos(List.of(toDo));
        toDo.setOwner(user);

        when(toDoRepository.getByUserId(2L)).thenReturn(List.of(toDo));

        Assertions.assertThat(toDoService.getByUserId(2L))
                .usingRecursiveComparison()
                .isNotNull()
                .isEqualTo(List.of(toDo));
        verify(toDoRepository, times(1)).getByUserId(2L);
        verifyNoMoreInteractions(toDoRepository);
    }

    @Test
    public void delete_collaborator() {

        doNothing().when(toDoRepository).deleteCollaborator(anyLong(), anyLong());

        toDoService.deleteCollaborator(anyLong(), anyLong());

        verify(toDoRepository, times(1)).deleteCollaborator(anyLong(), anyLong());
        verifyNoMoreInteractions(toDoRepository);
    }
}
