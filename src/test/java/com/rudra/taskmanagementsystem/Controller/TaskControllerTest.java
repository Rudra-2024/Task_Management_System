package com.rudra.taskmanagementsystem.Controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.rudra.taskmanagementsystem.Exceptions.TaskNotFoundException;
import com.rudra.taskmanagementsystem.Exceptions.UnAuthorizedAccessException;
import com.rudra.taskmanagementsystem.Models.Task;
import com.rudra.taskmanagementsystem.Models.User;
import com.rudra.taskmanagementsystem.Services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;
    private User user;
    private Task task;

    @BeforeEach
    public void setUp() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);
        user = new User();  // Set up a mock user or real user depending on the test
        SecurityContextHolder.getContext().setAuthentication(mock(Authentication.class));
    }

    @Test
    public void testCreateTask() {
        Task task = new Task("Title", "Description", "Todo", "High", "2024-08-15", user);
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        ResponseEntity<Task> response = (ResponseEntity<Task>) taskController.createTask(task, user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    public void testGetTaskById() {
        Long taskId = 1L;
        Task task = new Task("Title", "Description", "Todo", "High", "2024-08-15", user);
        when(taskService.findByIdAndUser(taskId, user)).thenReturn(Optional.of(task));

        ResponseEntity<?> response = taskController.getTaskById(taskId, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    public void testUpdateTask() {
        Long taskId = 1L;
        Task updatedTask = new Task("Updated Title", "Updated Description", "In Progress", "Medium", "2024-08-20", user);
        when(taskService.updateTask(any(Task.class))).thenReturn(updatedTask);

        ResponseEntity<?> response = taskController.updateTask(taskId, updatedTask, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTask, response.getBody());
    }

    @Test
    public void testDeleteTask() throws UnAuthorizedAccessException, TaskNotFoundException {
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId, user);

        ResponseEntity<?> response = taskController.deleteTask(taskId, user);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}