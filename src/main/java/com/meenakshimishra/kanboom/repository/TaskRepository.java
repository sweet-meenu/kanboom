package com.meenakshimishra.kanboom.repository;

import com.meenakshimishra.kanboom.entity.Task;
import com.meenakshimishra.kanboom.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwnerId(Long ownerId);

    List<Task> findByOwnerIdAndStatus(Long ownerId, TaskStatus status);

    Optional<Task> findByIdAndOwnerId(Long id, Long ownerId);
}
