package com.collab.app.service;


import com.collab.app.model.User;
import com.collab.app.repository.UserRepository;
import com.collab.app.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceTest {


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        when(userRepository.getOne(1L)).thenReturn(expectedUser());
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser()));
        when(userRepository.save(expectedUser())).thenReturn(expectedUser());
        doNothing().when(userRepository).delete(expectedUser());
        when(userRepository.getUserByEmail("testemail@gmail.com")).thenReturn(expectedUser());
    }

    public User expectedUser() {
        User expectedUser = new User();
        expectedUser.setFirstName("Service");
        expectedUser.setLastName("Test");
        expectedUser.setPassword("TestPass");
        expectedUser.setEmail("testemail@gmail.com");
        expectedUser.setId(1L);
        return expectedUser;
    }


    @Test
    public void create_user() {

        Assertions.assertThat(userService.create(expectedUser()))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(User.class)
                .isEqualTo(expectedUser());

        verify(userRepository, times(1)).save(expectedUser());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void read_by_id() {
        Assertions.assertThat(userService.readById(1L))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(User.class)
                .isEqualTo(expectedUser());

        verify(userRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void update_user() {

        User updateUser = userRepository.save(expectedUser());
        updateUser.setFirstName("Update");

        when(userRepository.save(updateUser)).thenReturn(updateUser);

        Assertions.assertThat(userService.update(updateUser))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(User.class)
                .isEqualTo(updateUser);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(expectedUser());
        verify(userRepository, times(1)).save(updateUser);
//        verify(userRepository, times(1)).f
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void delete_user() {

        userService.delete(1L);

        verify(userRepository, times(1)).delete(expectedUser());
        verify(userRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void find_all() {
        User secondExpectedUser = expectedUser();
        secondExpectedUser.setId(2L);

        when(userRepository.findAll())
                .thenReturn(Arrays.asList(expectedUser(), secondExpectedUser));

        assertIterableEquals(userRepository.findAll(), userService.getAll());

        verify(userRepository, times(2)).findAll();
        verifyNoMoreInteractions(userRepository);

    }
}
