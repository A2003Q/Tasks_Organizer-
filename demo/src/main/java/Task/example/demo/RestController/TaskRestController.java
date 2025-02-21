package Task.example.demo.RestController;

import Task.example.demo.DAO.TaskDTO;
import Task.example.demo.Entity.Postions;
import Task.example.demo.Entity.Task;
import Task.example.demo.Service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskDTO taskDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Postions) {
            Postions manager = (Postions) principal;
            if (!"MANAGER".equals(manager.getRole().name())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            taskDTO.setManager_email(manager.getEmail());
            return ResponseEntity.ok(taskService.createTask(taskDTO));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/{employeeName}")
    public ResponseEntity<List<Task>> getTasksForEmployee(@PathVariable String employeeName) {
        return ResponseEntity.ok(taskService.getTasksForEmployee(employeeName));
    }
}

