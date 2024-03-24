package com.collab.app.controller;

import com.collab.app.model.User;
import com.collab.app.service.RoleService;
import com.collab.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("user") User user) {
        logger.info("User:Create | Method:Get | IN");
        logger.info("User:Create | Method:Get | OUT");
        return "create-user";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("user")
                         @Valid User user,
                         BindingResult bindingResult) {
        logger.info("User:Create | | Method:Post | IN");
        if (bindingResult.hasErrors()) {
            logger.warn("User:Create | User id:{} | Method:Post | Binding result has errors", user.getId());
            return "create-user";
        }
        user.setRole(roleService.readById(2));
        userService.create(user);
        logger.info("User:Create | User id:{} | Method:Post | OUT", user.getId());
        return "redirect:/todos/all/users/" + user.getId();
    }


    @GetMapping("/{id}/read")
    public String read(@PathVariable long id, Model model) {
        logger.info("User:Read | User id:{} | Method:Get | IN", id);
        User user = userService.readById(id);
        model.addAttribute("user", user);
        logger.info("User:Read | User id:{} | Method:Get | OUT", id);
        return "user-info";
    }


    @GetMapping("/{id}/update")
    public String update(@PathVariable(value = "id") Long id,
                         Model model) {
        logger.info("User:Update | User id:{} | Method:Get | IN", id);
        User User = userService.readById(id);
        model.addAttribute("user", User);
        model.addAttribute("roles", roleService.getAll());
        logger.info("User:Update | User id:{} | Method:Get | OUT", id);
        return "update-user";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable(value = "id") Long id,
                         @ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         Model model) {
        logger.info("User:Update | User id:{} | Method:Post | IN", id);
        User update = userService.readById(id);
        if (bindingResult.hasFieldErrors("lastName") || bindingResult.hasFieldErrors("firstName")) {
            if (bindingResult.hasFieldErrors("password")) {
                user.setPassword(update.getPassword());
            }
            model.addAttribute("roles", roleService.getAll());
            return "update-user";
        } else if (bindingResult.hasFieldErrors("password") && (user.getPassword().isEmpty() || user.getPassword() == null)) {
            user.setPassword(update.getPassword());
            userService.update(user);
            return "redirect:/users/" + id + "/read";
        } else if (bindingResult.hasErrors()) {
            logger.warn("User:Update | User id:{} | Method:Post | Binding result has errors", user.getId());
            user.setPassword(update.getPassword());
            model.addAttribute("roles", roleService.getAll());
            return "update-user";
        }
        userService.update(user);
        logger.info("User:Update | User id:{} | Method:Post | OUT", id);
        return "redirect:/users/" + id + "/read";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable(value = "id") Long id) {
        logger.warn("User:Delete | User id:{} | Method:Get | IN", id);
        userService.delete(id);
        logger.warn("User:Delete | User id:{} | Method:Get | OUT", id);
        return "redirect:/home";
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        logger.warn("User:All | Method:Get | IN");
        model.addAttribute("users", userService.getAll());
        logger.warn("User:All | Method:Get | OUT");
        return "users-list";
    }
}
