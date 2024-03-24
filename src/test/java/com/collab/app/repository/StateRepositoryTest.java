package com.collab.app.repository;

import com.collab.app.model.State;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestPropertySource(properties = {"spring.datasource.initialization-mode=never"})
@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)


public class StateRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    StateRepository stateRepository;


    public State state() {
        State state = new State();
        state.setName("New");
        return state;
    }

    @Test
    public void create_state() {

        stateRepository.save(state());

        State savedState = stateRepository.getOne(1L);

        assertThat(savedState)
                .isInstanceOf(State.class)
                .isNotNull();

        assertThat(savedState)
                .extracting(State::getId)
                .isEqualTo(1L);
    }

    @Test
    public void read_state() {
        State state = state();

        entityManager.persist(state);

        State readState = stateRepository.findById(state.getId()).orElse(null);

        assertThat(readState)
                .usingRecursiveComparison()
                .isInstanceOf(State.class)
                .isNotNull()
                .isEqualTo(state);
    }

    @Test
    public void update_state() {
        State state = state();
        entityManager.persist(state);

        state.setName("NewName");
        stateRepository.save(state);

        State updatedState = entityManager.find(State.class, state.getId());

        assertThat(updatedState.getName()).isEqualTo("NewName");
    }

    @Test
    public void delete_state() {
        State state = state();
        entityManager.persist(state);

        stateRepository.delete(state);

        State deletedState = entityManager.find(State.class, state.getId());

        assertThat(deletedState).isNull();
    }

    @Test
    public void get_state_by_name() {
        State state = state();
        entityManager.persist(state);

        State readState = stateRepository.getByName("New");

        assertThat(readState.getName()).isEqualTo("New");
    }


    @Test
    public void get_all_state() {
        State state = state();
        State state1 = state();
        state1.setName("Doing");

        entityManager.persist(state);
        entityManager.persist(state1);

        List<State> stateList = stateRepository.getAll();

        assertThat(stateList)
                .containsExactly(state, state1);


    }
}
