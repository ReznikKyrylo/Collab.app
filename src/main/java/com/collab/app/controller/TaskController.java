package com.collab.app.controller;

import com.collab.app.dto.TaskDto;
import com.collab.app.dto.TaskTransformer;
import com.collab.app.model.Priority;
import com.collab.app.model.Task;
import com.collab.app.service.StateService;
import com.collab.app.service.TaskService;
import com.collab.app.service.ToDoService;
import com.collab.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final ToDoService toDoService;
    private final TaskService taskService;
    private final StateService stateService;

    @Autowired
    public TaskController(ToDoService toDoService, UserService userService, TaskService taskService, StateService stateService) {
        this.toDoService = toDoService;
        this.taskService = taskService;
        this.stateService = stateService;
    }

    @GetMapping("/create/todos/{todo_id}")
    public String create(@PathVariable("todo_id") long todoId, Model model) {
        logger.info("Task:Create | ToDo id:{} | Method:Get | IN", todoId);
        model.addAttribute("task", new TaskDto());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("todo_id", todoId);
        logger.info("Task:Create | ToDo id:{} | Method:Get | OUT", todoId);
        return "create-task";
    }

    @PostMapping("/create/todos/{todo_id}")
    public String create(@PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task") TaskDto taskDto,
                         BindingResult result) {
        logger.info("Task:Create | ToDo id:{} | Method:Post | IN", todoId);
        if (result.hasErrors()) {
            logger.warn("Task:Create | ToDo id:{} | Method:Post | Binding result has errors", todoId);
            model.addAttribute("todo", toDoService.readById(todoId));
            model.addAttribute("priorities", Priority.values());
            return "create-task";
        }
        Task task = TaskTransformer.convertToEntity(
                taskDto,
                toDoService.readById(taskDto.getTodoId()),
                stateService.getByName("New")
        );
        taskService.create(task);
        logger.info("Task:Create | ToDo id:{} | Method:Post | OUT", todoId);
        return "redirect:/todos/" + todoId + "/tasks";
    }

    @GetMapping("/{task_id}/update/todos/{todo_id}")
    public String update(@PathVariable("task_id") long taskId,
                         @PathVariable("todo_id") long todoId,
                         Model model) {
        logger.info("Task:Update | ToDo id:{} | Task id:{} | Method:Get | IN", todoId, taskId);
        TaskDto taskDto = TaskTransformer
                .convertToDto(taskService.readById(taskId));
        model.addAttribute("task", taskDto);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("states", stateService.getAll());
        logger.info("Task:Update | ToDo id:{} | Task id:{} | Method:Get | OUT", todoId, taskId);
        return "update-task";
    }

    @PostMapping("/{task_id}/update/todos/{todo_id}")
    public String update(@PathVariable("task_id") long taskId,
                         @PathVariable("todo_id") long todoId,
                         Model model,
                         @Validated @ModelAttribute("task") TaskDto taskDto,
                         BindingResult result) {
        logger.info("Task:Update | ToDo id:{} | Task id:{} | Method:Post | IN", todoId, taskId);
        if (result.hasErrors()) {
            logger.warn("Task:Update | ToDo id:{} | Task id:{} | Method:Get | Binding result has errors", todoId, taskId);
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("states", stateService.getAll());
            return "update-task";
        }
        Task task = TaskTransformer.convertToEntity(
                taskDto,
                toDoService.readById(taskDto.getTodoId()),
                stateService.readById(taskDto.getStateId())
        );
        taskService.update(task);
        logger.info("Task:Update | ToDo id:{} | Task id:{} | Method:Post | OUT", todoId, taskId);
        return "redirect:/todos/" + todoId + "/tasks";
    }

    @GetMapping("/{task_id}/delete/todos/{todo_id}")
    public String delete(@PathVariable("task_id") long taskId,
                         @PathVariable("todo_id") long todoId) {
        logger.info("Task:Delete | ToDo id:{} | Task id:{} | Method:Get | IN", todoId, taskId);
        taskService.delete(taskId);
        return "redirect:/todos/" + todoId + "/tasks";
    }
}
