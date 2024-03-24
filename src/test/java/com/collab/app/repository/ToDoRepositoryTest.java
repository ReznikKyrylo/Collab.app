package com.collab.app.repository;

import com.collab.app.model.ToDo;
import com.collab.app.model.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestPropertySource(properties = {"spring.datasource.initialization-mode=never"})
@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ToDoRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ToDoRepository toDoRepository;


    public ToDo toDo() {
        ToDo toDo = new ToDo();
        toDo.setTitle("Todo 1");
        toDo.setCreatedAt(LocalDateTime.now());
        return toDo;
    }

    public User user() {
        User user = new User();
        user.setFirstName("Teri");
        user.setLastName("Hops");
        user.setPassword("Passw0rd");
        user.setEmail("email@gmail.com");
        return user;
    }

    @Test
    public void create_todo() {

        toDoRepository.save(toDo());

        ToDo savedTodo = toDoRepository.getOne(1L);

        assertThat(savedTodo)
                .isInstanceOf(ToDo.class)
                .isNotNull();

        assertThat(savedTodo)
                .extracting(ToDo::getId)
                .isEqualTo(1L);
    }

    @Test
    public void read_todo() {
        ToDo toDo = toDo();

        entityManager.persist(toDo);

        ToDo readToDo = toDoRepository.findById(toDo.getId()).orElse(null);

        assertThat(readToDo)
                .usingRecursiveComparison()
                .isInstanceOf(ToDo.class)
                .isNotNull()
                .isEqualTo(toDo);
    }

    @Test
    public void update_todo() {
        ToDo toDo = toDo();
        entityManager.persist(toDo);

        toDo.setTitle("Update Todo 1");
        toDoRepository.save(toDo);

        ToDo updatedToDo = entityManager.find(ToDo.class, toDo.getId());

        assertThat(updatedToDo.getTitle()).isEqualTo("Update Todo 1");
    }

    @Test
    public void delete_todo() {
        ToDo toDo = toDo();

        entityManager.persist(toDo);

        toDoRepository.delete(toDo);

        ToDo deletedToDo = entityManager.find(ToDo.class, toDo.getId());

        assertThat(deletedToDo).isNull();
    }

    @Test
    public void get_todo_by_user_id() {
        User user = user();
        ToDo toDo = toDo();

        entityManager.persist(toDo);
        entityManager.persist(user);

        user.setMyTodos(List.of(toDo));
        toDo.setOwner(user);

        List<ToDo> toDoList = toDoRepository.getByUserId(user.getId());

        assertThat(toDoList)
                .hasSize(1)
                .isNotNull()
                .isEqualTo(List.of(toDo));
    }

}
