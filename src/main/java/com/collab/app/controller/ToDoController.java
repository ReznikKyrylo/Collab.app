package com.collab.app.controller;

import com.collab.app.model.ToDo;
import com.collab.app.model.User;
import com.collab.app.service.ToDoService;
import com.collab.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/todos")
public class ToDoController {

    private final Logger logger = LoggerFactory.getLogger(ToDoController.class);
    private final ToDoService toDoService;
    private final UserService userService;

    @Autowired
    public ToDoController(ToDoService toDoService, UserService userService) {
        this.toDoService = toDoService;
        this.userService = userService;
    }

    @GetMapping("/create/users/{owner_id}")
    public String create(@PathVariable("owner_id") long owner_id,
                         @ModelAttribute("todo") ToDo toDo,
                         Model model) {
        logger.info("ToDo:Create | Owner id:{} | Method:Get | IN", owner_id);
        model.addAttribute("owner_id", owner_id);
        logger.info("ToDo:Create | Owner id:{} | Method:Get | OUT", owner_id);
        return "create-todo";
    }

    @PostMapping("/create/users/{owner_id}")
    public String create(@PathVariable("owner_id") long owner_id,
                         @ModelAttribute("todo") @Valid ToDo toDo,
                         BindingResult bindingResult) {
        logger.info("ToDo:Create | Owner id:{} | Method:Post | IN", owner_id);
        if (bindingResult.hasErrors()) {
            logger.warn("ToDo:Create | Owner id:{} | Method:Post | Binding result has errors", owner_id);
            return "create-todo";
        }
        toDo.setCreatedAt(LocalDateTime.now());
        toDo.setOwner(userService.readById(owner_id));
        toDoService.create(toDo);
        logger.info("ToDo:Create | Owner id:{} | Method:Post | IN", owner_id);
        return "redirect:/todos/all/users/" + owner_id;
    }


    @GetMapping("/{id}/tasks")
    public String read(@PathVariable("id") long id, Model model) {
        logger.info("ToDo:Read | Todo id:{} | Method:Get | IN", id);
        ToDo todo = toDoService.readById(id);
        List<User> collaborators = userService.getAll()
                .stream()
                .filter(user -> user.getId() != todo.getOwner().getId())
                .collect(Collectors.toList());
        model.addAttribute("toDo", todo);
        model.addAttribute("collaborators", collaborators);
        logger.info("ToDo:Read | Todo id:{} | Method:Get | OUT", id);
        return "todo-tasks";
    }


    @GetMapping("/{todo_id}/update/users/{owner_id}")
    public String update(@PathVariable("todo_id") long todo_id,
                         @PathVariable("owner_id") long owner_id, Model model) {
        logger.info("ToDo:Update | Owner id:{} | Todo id:{} | Method:Get | IN", owner_id, todo_id);
        ToDo todo = toDoService.readById(todo_id);
        model.addAttribute("todo", todo);
        logger.info("ToDo:Update | Owner id:{} | Todo id:{} | Method:Get | OUT", owner_id, todo_id);
        return "update-todo";
    }

    @PostMapping("/{todo_id}/update/users/{owner_id}")
    public String update(@PathVariable("todo_id") long todo_id,
                         @PathVariable("owner_id") long owner_id,
                         @Valid @ModelAttribute("todo") ToDo toDo,
                         BindingResult bindingResult) {
        logger.info("ToDo:Update | Owner id:{} | Todo id:{} | Method:Post | IN", owner_id, todo_id);
        if (bindingResult.hasErrors()) {
            logger.warn("ToDo:Update | Owner id:{} | Todo id:{} | Method:Post | Binding result has errors", owner_id, todo_id);
            toDo.setOwner(userService.readById(owner_id));
            return "update-todo";
        }
        ToDo todo = toDoService.readById(todo_id);
        todo.setTitle(toDo.getTitle());
        toDoService.update(todo);
        logger.info("ToDo:Update | Owner id:{} | Todo id:{} | Method:Post | OUT", owner_id, todo_id);
        return "redirect:/todos/all/users/" + owner_id;
    }

    @GetMapping("/{todo_id}/delete/users/{owner_id}")
    public String delete(@PathVariable("owner_id") long owner_id,
                         @PathVariable("todo_id") long todo_id) {
        logger.info("ToDo:Delete | Owner id:{} | Todo id:{} | Method:Get | IN", owner_id, todo_id);
        toDoService.delete(todo_id);
        logger.info("ToDo:Delete | Owner id:{} | Todo id:{} | Method:Post | OUT", owner_id, todo_id);
        return "redirect:/todos/all/users/" + owner_id;
    }

    @GetMapping("/all/users/{user_id}")
    public String getAll(@PathVariable("user_id") long id, Model model) {
        logger.info("ToDo:All | Owner id:{} | Method:Get | IN", id);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        model.addAttribute("todos", toDoService.getByUserId(id));
        model.addAttribute("owner", userService.readById(id));
        model.addAttribute("format", format);
        logger.info("ToDo:All | Owner id:{} | Method:Get | OUT", id);
        return "todos-user";
    }

    @GetMapping("/{id}/add")
    public String addCollaborator(@PathVariable("id") long id, @RequestParam("user_id") long user_id) {
        logger.info("ToDo:Add Collaborator | Owner id:{} | Collaborator id:{} | Method:Get | IN", id, user_id);
        if (user_id == 999999999) {
            return "redirect:/todos/" + id + "/tasks";
        }
        ToDo toDo = toDoService.readById(id);
        User user = userService.readById(user_id);
        List<User> collaborators = toDo.getCollaborators();
        if (!collaborators.contains(user)) {
            collaborators.add(user);
            toDo.setCollaborators(collaborators);
            toDoService.update(toDo);
        }
        logger.info("ToDo:Add Collaborator | Owner id:{} | Collaborator id:{} | Method:Get | OUT", id, user_id);
        return "redirect:/todos/" + id + "/tasks";
    }

    @GetMapping("/{id}/remove")
    public String removeCollaborator(@PathVariable("id") long id, @RequestParam("user_id") long user_id) {
        logger.info("ToDo:Remove Collaborator | Owner id:{} | Collaborator id:{} | Method:Get | IN", id, user_id);
        ToDo toDo = toDoService.readById(id);
        List<User> collaborators = toDo.getCollaborators();
        collaborators.remove(userService.readById(user_id));
        toDo.setCollaborators(collaborators);
        toDoService.update(toDo);
        logger.info("ToDo:Remove Collaborator | Owner id:{} | Collaborator id:{} | Method:Get | OUT", id, user_id);
        return "redirect:/todos/" + id + "/tasks";
    }

}
