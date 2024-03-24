package com.collab.app.service;

import com.collab.app.model.Task;
import com.collab.app.model.ToDo;
import com.collab.app.repository.TaskRepository;
import com.collab.app.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    public void create_state() {

        Task task = new Task();
        task.setName("TASK");

        when(taskRepository.save(task)).thenReturn(task);

        assertThat(taskService.create(task))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(Task.class)
                .isEqualTo(task);

        verify(taskRepository, times(1)).save(task);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void read_task_by_id() {

        Task task = new Task();
        task.setName("TASK");
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThat(taskService.readById(1L))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(Task.class)
                .isEqualTo(task);

        verify(taskRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void update_task() {

        Task task = new Task();
        task.setName("TASK");
        task.setId(1L);

        when(taskRepository.save(task)).thenReturn(task);

        Task updateTask = taskRepository.save(task);
        updateTask.setName("UPDATE_TASK");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThat(taskService.update(updateTask))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(Task.class)
                .isEqualTo(updateTask);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(2)).save(updateTask);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void delete_task() {

        Task task = new Task();
        task.setName("TASK");
        task.setId(1L);

        doNothing().when(taskRepository).delete(task);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.delete(1L);

        verify(taskRepository, times(1)).delete(task);
        verify(taskRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(taskRepository);

    }

    @Test
    public void get_all_state() {
        when(taskRepository.findAll()).thenReturn(List.of(new Task(), new Task()));

        assertThat(taskService.getAll())
                .isNotEmpty()
                .isNotNull()
                .hasSize(2);
        verify(taskRepository, times(1)).findAll();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void get_tasks_by_todo_id() {

        Task task = new Task();
        task.setName("TASK");
        Task task2 = new Task();
        task.setName("TASK2");

        ToDo toDo = new ToDo();
        toDo.setId(1L);
        toDo.setTasks(Arrays.asList(task, task2));

        when(taskRepository.getByTodoId(1L)).thenReturn(Arrays.asList(task, task2));

        assertThat(taskService.getByTodoId(1L))
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .isEqualTo(Arrays.asList(task, task2));
        verify(taskRepository, times(1)).getByTodoId(1L);
        verifyNoMoreInteractions(taskRepository);

    }
}
