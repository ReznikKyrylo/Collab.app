package com.collab.app.controller;

import com.collab.app.model.User;
import com.collab.app.service.UserService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    @Test
    public void user_create_get() throws Exception {

        mockMvc.perform(get("/users/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("create-user"));
    }

    @Transactional
    @Test
    public void user_create_post() throws Exception {

        MockHttpServletRequestBuilder mock = post("/users/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Arni")
                .param("lastName", "Buzovin")
                .param("email", "arnibuz@gmail.com")
                .param("password", "Passw0rd");

        mockMvc.perform(mock)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/todos/all/users/{*[0-9]+}"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void user_read_get() throws Exception {
        long id = 5L;
        mockMvc.perform(get("/users/{id}/read", id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", hasProperty("id", equalTo(id))))
                .andExpect(view().name("user-info"));
    }

    @Test
    public void user_update_get() throws Exception {
        long id = 5L;
        mockMvc.perform(get("/users/{id}/update", id))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("user", hasProperty("id", equalTo(id))))
                .andExpect(model().attribute("roles", hasSize(2)))
                .andExpect(view().name("update-user"));
    }

    @Transactional
    @Test
    public void user_update_post() throws Exception {

        MockHttpServletRequestBuilder mock = post("/users/{id}/update", 5)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Arni")
                .param("lastName", "Buzovin")
                .param("email", "arnibuz@gmail.com")
                .param("password", "Passw0rd")
                .param("roleId", "2");

        mockMvc.perform(mock)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/users/{*[0-9]+}/read"))
                .andExpect(model().hasNoErrors());
    }

    @Transactional
    @Test
    public void user_delete_get() throws Exception {
        mockMvc.perform(get("/users/{id}/delete", 5))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void user_all_get() throws Exception {

        List<User> all = userService.getAll();
        System.out.println(all);

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(3)))
                .andExpect(model().attribute("users", all))
                .andExpect(view().name("users-list"));
    }
}
