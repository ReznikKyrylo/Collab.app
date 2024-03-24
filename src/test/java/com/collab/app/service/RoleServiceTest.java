package com.collab.app.service;


import com.collab.app.model.Role;
import com.collab.app.repository.RoleRepository;
import com.collab.app.service.impl.RoleServiceImpl;
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
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void create_role() {

        Role expectedRole = new Role();
        expectedRole.setName("TEST_ROLE");

        when(roleRepository.save(expectedRole)).thenReturn(expectedRole);
        assertThat(roleService.create(expectedRole))
                .isNotNull()
                .isInstanceOf(Role.class)
                .usingRecursiveComparison()
                .isEqualTo(expectedRole);

        verify(roleRepository, times(1)).save(expectedRole);
        verifyNoMoreInteractions(roleRepository);
    }


    @Test
    public void read_role_by_id() {

        Role readRole = new Role();
        readRole.setName("NEW_TEST_ROLE");
        readRole.setId(1L);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(readRole));
        assertThat(roleService.readById(1L))
                .isInstanceOf(Role.class)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(readRole);
        verify(roleRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(roleRepository);

    }

    @Test
    public void update_role() {

        Role expectedRole = new Role();
        expectedRole.setName("OLD_TEST_ROLE");
        expectedRole.setId(1L);

        when(roleRepository.save(expectedRole)).thenReturn(expectedRole);

        Role actualRole = roleRepository.save(expectedRole);
        actualRole.setName("NEW_TEST_ROLE");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(expectedRole));

        assertThat(roleService.update(actualRole))
                .isInstanceOf(Role.class)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(actualRole);

        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(2)).save(actualRole);
        verifyNoMoreInteractions(roleRepository);
    }


    @Test
    public void delete_role() {

        Role deleteRole = new Role();
        deleteRole.setName("TEST_ROLE");
        deleteRole.setId(2L);

        doNothing().when(roleRepository).delete(deleteRole);
        when(roleRepository.findById(2L)).thenReturn(Optional.of(deleteRole));

        roleService.delete(2L);

        verify(roleRepository, times(1)).delete(deleteRole);
        verify(roleRepository, times(1)).findById(2L);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void get_all_roles() {

        when(roleRepository.findAll()).thenReturn(Arrays.asList(new Role(), new Role()));

        assertThat(roleService.getAll())
                .isNotNull()
                .hasSize(2)
                .isNotEmpty();
        verify(roleRepository, times(1)).findAll();
        verifyNoMoreInteractions(roleRepository);
    }

}
