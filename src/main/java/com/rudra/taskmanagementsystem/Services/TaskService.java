package com.rudra.taskmanagementsystem.Services;

import com.rudra.taskmanagementsystem.Exceptions.TaskNotFoundException;
import com.rudra.taskmanagementsystem.Exceptions.UnAuthorizedAccessException;
import com.rudra.taskmanagementsystem.Models.Task;
import com.rudra.taskmanagementsystem.Models.TaskPriority;
import com.rudra.taskmanagementsystem.Models.TaskStatus;
import com.rudra.taskmanagementsystem.Models.User;
import com.rudra.taskmanagementsystem.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;


     //Creates a new task.
     //@param task The task to be created.
     //@return The newly created task.
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

     //Updates an existing task.
     //@param task The task to be updated.
     //@return The updated task.

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

     //Deletes a task.
     //@param task The task to be deleted.

    public void deleteTask(long taskId, User user) throws TaskNotFoundException, UnAuthorizedAccessException {
        // Find the task by ID
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        // Optional: Check if the task belongs to the user (for authorization purposes)
        if (!task.getUser().equals(user)) {
            throw new UnAuthorizedAccessException("You are not authorized to delete this task");
        }

        // Delete the task
        taskRepository.delete(task);
    }

     //Finds all tasks for a specific user.
     //@param user The user whose tasks are to be retrieved.
     //@return A list of tasks belonging to the user.

    public List<Task> findTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

     //Finds a task by its ID and user.
     //@param taskId The ID of the task to be retrieved.
     //@param user   The user who owns the task.
     //@return An Optional containing the task if found, or empty if not found.

    public Optional<Task> findByIdAndUser(Long taskId, User user) {
        return taskRepository.findByIdAndUser(taskId, user);
    }

     //Filters tasks by status for a specific user.
     //@param user   The user whose tasks are to be filtered.
     //@param status The status to filter by.
     //@return A list of tasks matching the status.

    public List<Task> findByUserAndStatus(User user, TaskStatus status) {
        return taskRepository.findByUserAndStatus(user, status);
    }


     //Filters tasks by priority for a specific user.
     //@param user     The user whose tasks are to be filtered.
     //@param priority The priority to filter by.
     //@return A list of tasks matching the priority.

    public List<Task> findByUserAndPriority(User user, TaskPriority priority) {
        return taskRepository.findByUserAndPriority(user, priority);
    }

     //Filters tasks by due date for a specific user.
     //@param user    The user whose tasks are to be filtered.
     //@param dueDate The due date to filter by.
     //@return A list of tasks matching the due date.

    public List<Task> findByUserAndDueDate(User user, LocalDate dueDate) {
        return taskRepository.findByUserAndDueDate(user, dueDate);
    }

     //Searches tasks by title or description for a specific user.
     //@param user   The user whose tasks are to be searched.
     //@param search The search term to filter tasks by title or description.
     //@return A list of tasks matching the search term.

    public List<Task> searchTasks(User user, String search) {
        return taskRepository.findByUserAndTitleContainingOrDescriptionContaining(user, search, search);
    }
}
