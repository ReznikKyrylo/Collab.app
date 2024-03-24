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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void task_create_get() throws Exception {

        mockMvc.perform(get("/tasks/create/todos/{id}", 7))
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(model().attributeExists("priorities"))
                .andExpect(model().attributeExists("todo_id"))
                .andExpect(model().attributeExists("task"))
                .andExpect(view().name("create-task"));
    }

    @Test
    @Transactional
    public void task_create_post() throws Exception {

        mockMvc.perform(post("/tasks/create/todos/{id}", 7)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("stateId", "7")
                        .param("id", "7")
                        .param("todoId", "7")
                        .param("priority", "MEDIUM")
                        .param("name", "Update name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/todos/{*[0-9]+}/tasks"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void task_update_get() throws Exception {

        mockMvc.perform(get("/tasks/{id}/update/todos/{todo_id}", 7, 7))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("priorities"))
                .andExpect(model().attributeExists("states"))

                .andExpect(model().attribute("task", hasProperty("id", equalTo(7L))))
                .andExpect(model().attribute("task", hasProperty("name", equalTo("Task #3"))))

                .andExpect(view().name("update-task"));
    }


    @Test
    @Transactional
    public void task_update_post() throws Exception {
        mockMvc.perform(post("/tasks/{id}/update/todos/{id}", 7, 7)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("stateId", "7")
                        .param("id", "7")
                        .param("todoId", "7")
                        .param("priority", "MEDIUM")
                        .param("name", "Update name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/todos/{*[0-9]+}/tasks"))
                .andExpect(model().hasNoErrors());

    }

    @Test
    @Transactional
    public void task_delete_get() throws Exception {
        mockMvc.perform(get("/tasks/{id}/delete/todos/{id}", 7, 7))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/todos/{*[0-9]+}/tasks"));
    }

}
