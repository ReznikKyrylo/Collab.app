package com.collab.app.controller;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void todo_create_get() throws Exception {
        mockMvc.perform(get("/todos/create/users/{id}", 7))
                .andExpect(model().attributeExists("owner_id"))
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().attribute("owner_id", 7L))
                .andExpect(view().name("create-todo"));
    }

    @Test
    @Transactional
    public void todo_create_post() throws Exception {
        mockMvc.perform(post("/todos/create/users/{id}", 6)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "ToDo"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/todos/all/users/{*[0-9]+}"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void todo_read_get() throws Exception {
        mockMvc.perform(get("/todos/{id}/tasks", 7))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("toDo"))
                .andExpect(model().attributeExists("collaborators"))
                .andExpect(model().attribute("toDo", hasProperty("id", equalTo(7L))))
                .andExpect(view().name("todo-tasks"));
    }

    @Test
    public void todo_update_get() throws Exception {
        mockMvc.perform(get("/todos/{id}/update/users/{id}", 7, 4))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().attribute("todo", hasProperty("id", equalTo(7L))));
    }

    @Test
    @Transactional
    public void todo_update_post() throws Exception {
        mockMvc.perform(post("/todos/{id}/update/users/{id}", 7, 4)
                        .param("title", "Update ToDo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/todos/all/users/{*[0-9]+}"));
    }

    @Test
    @Transactional
    void todo_delete_get() throws Exception {
        mockMvc.perform(get("/todos/{todo_id}/delete/users/{owner_id}", 7, 4))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/todos/all/users/{*[0-9]+}"));
    }

    @Test
    void todo_all_get() throws Exception {
        mockMvc.perform(get("/todos/all/users/{user_id}", 4))
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("format"))
                .andExpect(model().attribute("owner", hasProperty("id", equalTo(4L))))
                .andExpect(view().name("todos-user"));
    }

    @Test
    @Transactional
    void todo_add_collaborator_get() throws Exception {
        mockMvc.perform(get("/todos/{id}/add", "7")
                        .param("user_id", "4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/todos/{*[0-9]+}/tasks"));
    }

    @Test
    @Transactional
    void todo_remove_collaborator_get() throws Exception {
        mockMvc.perform(get("/todos/{id}/remove", 7)
                        .param("user_id", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/todos/{*[0-9]+}/tasks"));
    }

}
