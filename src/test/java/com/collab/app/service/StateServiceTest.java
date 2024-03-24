package com.collab.app.service;

import com.collab.app.model.State;
import com.collab.app.repository.StateRepository;
import com.collab.app.service.impl.StateServiceImpl;
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
public class StateServiceTest {

    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private StateServiceImpl stateService;


    @Test
    public void create_state() {

        State state = new State();
        state.setName("NEW");
        when(stateRepository.save(state)).thenReturn(state);

        assertThat(stateService.create(state))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(State.class)
                .isEqualTo(state);

        verify(stateRepository, times(1)).save(state);
        verifyNoMoreInteractions(stateRepository);
    }

    @Test
    public void read_state_by_id() {

        State state = new State();
        state.setName("NEW");
        state.setId(1L);

        when(stateRepository.findById(1L)).thenReturn(Optional.of(state));

        assertThat(stateService.readById(1L))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(State.class)
                .isEqualTo(state);

        verify(stateRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(stateRepository);
    }

    @Test
    public void update_state() {

        State state = new State();
        state.setName("NEW");
        state.setId(1L);

        when(stateRepository.save(state)).thenReturn(state);

        State updateState = stateRepository.save(state);
        updateState.setName("UPDATE_STATE");

        when(stateRepository.findById(1L)).thenReturn(Optional.of(state));

        assertThat(stateService.update(updateState))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(State.class)
                .isEqualTo(updateState);

        verify(stateRepository, times(1)).findById(1L);
        verify(stateRepository, times(2)).save(updateState);
        verifyNoMoreInteractions(stateRepository);
    }

    @Test
    public void delete_state() {

        State state = new State();
        state.setName("NEW");
        state.setId(1L);

        doNothing().when(stateRepository).delete(state);
        when(stateRepository.findById(1L)).thenReturn(Optional.of(state));

        stateService.delete(1L);

        verify(stateRepository, times(1)).delete(state);
        verify(stateRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(stateRepository);

    }

    @Test
    public void get_state_by_name() {

        State state = new State();
        state.setName("NEW");
        state.setId(1L);

        when(stateRepository.getByName("New")).thenReturn(state);

        assertThat(stateService.getByName("New"))
                .usingRecursiveComparison()
                .isNotNull()
                .isInstanceOf(State.class)
                .isEqualTo(state);

        verify(stateRepository, times(1)).getByName("New");
        verifyNoMoreInteractions(stateRepository);
    }

    @Test
    public void get_all_state() {
        when(stateRepository.getAll()).thenReturn(List.of(new State(), new State()));

        assertThat(stateService.getAll())
                .isNotEmpty()
                .isNotNull()
                .hasSize(2);
        verify(stateRepository, times(1)).getAll();
        verifyNoMoreInteractions(stateRepository);
    }
}
