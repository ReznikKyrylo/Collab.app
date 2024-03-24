package com.collab.app.repository;

import com.collab.app.model.Task;
import com.collab.app.model.ToDo;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestPropertySource(properties = {"spring.datasource.initialization-mode=never"})
@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)


public class TaskRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TaskRepository taskRepository;


    public Task task() {
        Task task = new Task();
        task.setName("Task 1");
        return task;
    }

    @Test
    public void create_task() {

        taskRepository.save(task());

        Task savedTask = taskRepository.getOne(1L);

        assertThat(savedTask)
                .isInstanceOf(Task.class)
                .isNotNull();

        assertThat(savedTask)
                .extracting(Task::getId)
                .isEqualTo(1L);
    }

    @Test
    public void read_task() {
        Task task = task();

        entityManager.persist(task);

        Task readTask = taskRepository.findById(task.getId()).orElse(null);

        assertThat(readTask)
                .usingRecursiveComparison()
                .isInstanceOf(Task.class)
                .isNotNull()
                .isEqualTo(task);
    }

    @Test
    public void update_task() {
        Task task = task();
        entityManager.persist(task);

        task.setName("Update Task 1");
        taskRepository.save(task);

        Task updatedTask = entityManager.find(Task.class, task.getId());

        assertThat(updatedTask.getName()).isEqualTo("Update Task 1");
    }

    @Test
    public void delete_task() {
        Task task = task();
        entityManager.persist(task);

        taskRepository.delete(task);

        Task deletedState = entityManager.find(Task.class, task.getId());

        assertThat(deletedState).isNull();
    }

    @Test
    public void get_tasks_by_todo_id() {
        Task task = task();
        Task task1 = new Task();
        task1.setName("Task 2");

        ToDo toDo = new ToDo();
        toDo.setTitle("Todo");
        toDo.setCreatedAt(LocalDateTime.now());
        toDo.setTasks(Arrays.asList(task, task1));

        task.setTodo(toDo);
        task1.setTodo(toDo);

        entityManager.persist(task);
        entityManager.persist(task1);
        entityManager.persist(toDo);

        List<Task> taskList = taskRepository.getByTodoId(1L);

        assertThat(taskList)
                .isNotNull()
                .hasSize(2)
                .isEqualTo(Arrays.asList(task, task1));
    }

}
