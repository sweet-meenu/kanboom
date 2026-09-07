package com.meenakshimishra.kanboom.service;

import com.meenakshimishra.kanboom.dto.task.TaskRequest;
import com.meenakshimishra.kanboom.dto.task.TaskResponse;
import com.meenakshimishra.kanboom.dto.task.UpdateStatusRequest;
import com.meenakshimishra.kanboom.entity.Task;
import com.meenakshimishra.kanboom.entity.TaskStatus;
import com.meenakshimishra.kanboom.entity.User;
import com.meenakshimishra.kanboom.repository.TaskRepository;
import com.meenakshimishra.kanboom.security.CurrentUserProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CurrentUserProvider currentUserProvider;

    public TaskService(TaskRepository taskRepository, CurrentUserProvider currentUserProvider) {
        this.taskRepository = taskRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public TaskResponse create(TaskRequest request) {
        User owner = currentUserProvider.getCurrentUser();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);
        task.setOwner(owner);

        return new TaskResponse(taskRepository.save(task));
    }

    public List<TaskResponse> list(TaskStatus status) {
        Long ownerId = currentUserProvider.getCurrentUser().getId();
        List<Task> tasks = status != null
                ? taskRepository.findByOwnerIdAndStatus(ownerId, status)
                : taskRepository.findByOwnerId(ownerId);
        return tasks.stream().map(TaskResponse::new).toList();
    }

    public TaskResponse getById(Long id) {
        return new TaskResponse(findOwnedTask(id));
    }

    public TaskResponse update(Long id, TaskRequest request) {
        Task task = findOwnedTask(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        return new TaskResponse(taskRepository.save(task));
    }

    public TaskResponse updateStatus(Long id, UpdateStatusRequest request) {
        Task task = findOwnedTask(id);
        task.setStatus(request.getStatus());
        return new TaskResponse(taskRepository.save(task));
    }

    public void delete(Long id) {
        taskRepository.delete(findOwnedTask(id));
    }

    private Task findOwnedTask(Long id) {
        Long ownerId = currentUserProvider.getCurrentUser().getId();
        return taskRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
}
