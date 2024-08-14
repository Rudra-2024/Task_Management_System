package com.rudra.taskmanagementsystem.Repositories;

import com.rudra.taskmanagementsystem.Models.Task;
import com.rudra.taskmanagementsystem.Models.TaskPriority;
import com.rudra.taskmanagementsystem.Models.TaskStatus;
import com.rudra.taskmanagementsystem.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserAndStatus(User user, TaskStatus status);
    List<Task> findByUserAndPriority(User user, TaskPriority priority);
    List<Task> findByUserAndDueDate(User user, LocalDate dueDate);
    List<Task> findByUserAndTitleContainingOrDescriptionContaining(User user, String title, String description);

    List<Task> findByUser(User user);

    Optional<Task> findByIdAndUser(Long taskId, User user);

}
