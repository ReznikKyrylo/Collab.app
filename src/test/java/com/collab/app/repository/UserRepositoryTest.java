package com.collab.app.repository;

import com.collab.app.model.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;


@TestPropertySource(properties = {"spring.datasource.initialization-mode=never"})
@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)


public class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;


    public User user() {
        User user = new User();
        user.setFirstName("Name");
        user.setLastName("Last");
        user.setPassword("Passw0rd");
        user.setEmail("email@gmail.com");
        return user;
    }

    @Test
    public void create_user() {

        userRepository.save(user());

        User savedUser = userRepository.getOne(1L);

        assertThat(savedUser)
                .isInstanceOf(User.class)
                .isNotNull();

        assertThat(savedUser)
                .extracting(User::getId)
                .isEqualTo(1L);
    }

    @Test
    public void read_user() {
        User user = user();

        entityManager.persist(user);

        User readUser = userRepository.findById(user.getId()).orElse(null);

        assertThat(readUser)
                .usingRecursiveComparison()
                .isInstanceOf(User.class)
                .isNotNull()
                .isEqualTo(user);
    }

    @Test
    public void update_user() {
        User user = user();
        entityManager.persist(user);

        user.setFirstName("NewName");
        userRepository.save(user);

        User updatedUser = entityManager.find(User.class, user.getId());

        assertThat(updatedUser.getFirstName()).isEqualTo("NewName");
    }

    @Test
    public void delete_user() {
        User user = user();
        entityManager.persist(user);

        userRepository.delete(user);

        User deletedUser = entityManager.find(User.class, user.getId());

        assertThat(deletedUser).isNull();
    }

    @Test
    public void get_user_by_email() {
        User user = user();
        entityManager.persist(user);

        User readUser = userRepository.getUserByEmail("email@gmail.com");

        assertThat(readUser.getEmail()).isEqualTo("email@gmail.com");
    }
}
