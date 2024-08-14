package com.rudra.taskmanagementsystem.Controller;


import com.rudra.taskmanagementsystem.Exceptions.TaskNotFoundException;
import com.rudra.taskmanagementsystem.Exceptions.UnAuthorizedAccessException;
import com.rudra.taskmanagementsystem.Models.Task;
import com.rudra.taskmanagementsystem.Models.TaskPriority;
import com.rudra.taskmanagementsystem.Models.TaskStatus;
import com.rudra.taskmanagementsystem.Models.User;
import com.rudra.taskmanagementsystem.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task, @AuthenticationPrincipal User user) {
        task.setUser(user);
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<?> getTasks(
            @AuthenticationPrincipal User user,
            @RequestParam Optional<TaskStatus> status,
            @RequestParam Optional<TaskPriority> priority,
            @RequestParam Optional<LocalDate> dueDate,
            @RequestParam Optional<String> search
    ) {
        List<Task> tasks;

        if (status.isPresent()) {
            tasks = taskService.findByUserAndStatus(user, status.get());
        } else if (priority.isPresent()) {
            tasks = taskService.findByUserAndPriority(user, priority.get());
        } else if (dueDate.isPresent()) {
            tasks = taskService.findByUserAndDueDate(user, dueDate.get());
        } else if (search.isPresent()) {
            tasks = taskService.searchTasks(user, search.get());
        } else {
            tasks = taskService.findTasksByUser(user);
        }

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId, @AuthenticationPrincipal User user) {
        Optional<Task> taskOptional = taskService.findByIdAndUser(taskId, user);
        if (taskOptional.isPresent()) {
            return ResponseEntity.ok(taskOptional.get());
        } else {
            return ResponseEntity.status(404).body("Task not found");
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask, @AuthenticationPrincipal User user) {
        Optional<Task> existingTaskOpt = taskService.findByIdAndUser(taskId, user);

        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setDueDate(updatedTask.getDueDate());
            Task savedTask = taskService.updateTask(existingTask);
            return ResponseEntity.ok(savedTask);
        } else {
            return ResponseEntity.status(404).body("Task not found");
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId, @AuthenticationPrincipal User user) throws UnAuthorizedAccessException, TaskNotFoundException {
        Optional<Task> task = taskService.findByIdAndUser(taskId, user);
        if (task.isPresent()) {
            taskService.deleteTask(taskId, user);
            return ResponseEntity.ok("Task deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Task not found");
        }
    }
}
