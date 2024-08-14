package com.rudra.taskmanagementsystem.Services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.rudra.taskmanagementsystem.Exceptions.TaskNotFoundException;
import com.rudra.taskmanagementsystem.Exceptions.UnAuthorizedAccessException;
import com.rudra.taskmanagementsystem.Models.Task;
import com.rudra.taskmanagementsystem.Models.User;
import com.rudra.taskmanagementsystem.Repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class TaskServiceTest {

    private TaskService taskService;
    private TaskRepository taskRepository;
    private User user;

    public TaskServiceTest(TaskService taskService, TaskRepository taskRepository, User user){
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.user = user;
    }

    @BeforeEach
    public void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService();
        user = new User();  // Set up a mock user or real user depending on the test
    }

    @Test
    public void testCreateTask() {
        Task task = new Task("Title", "Description", "Todo", "High", "2024-08-15", user);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertEquals(task, createdTask);
    }

    @Test
    public void testFindByIdAndUser() {
        Long taskId = 1L;
        Task task = new Task("Title", "Description", "Todo", "High", "2024-08-15", user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Optional<Task> foundTask = taskService.findByIdAndUser(taskId, user);

        assertTrue(foundTask.isPresent());
        assertEquals(task, foundTask.get());
    }

    @Test
    public void testUpdateTask() {
        Long taskId = 1L;
        Task existingTask = new Task("Title", "Description", "Todo", "High", "2024-08-15", user);
        Task updatedTask = new Task("Updated Title", "Updated Description", "In Progress", "Medium", "2024-08-20", user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Optional<Task> result = Optional.ofNullable(taskService.updateTask(updatedTask));

        assertTrue(result.isPresent());
        assertEquals(updatedTask, result.get());
    }

    @Test
    public void testDeleteTask() throws UnAuthorizedAccessException, TaskNotFoundException {
        Long taskId = 1L;
        doNothing().when(taskRepository).deleteById(taskId);

        taskService.deleteTask(taskId, user);

        verify(taskRepository).deleteById(taskId);
    }
}