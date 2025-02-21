package Task.example.demo.Service;

import Task.example.demo.DAO.TaskDAO;
import Task.example.demo.DAO.TaskDTO;
import Task.example.demo.Entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public Task createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTask_name(taskDTO.getTask_name());
        task.setEmployee_name(taskDTO.getEmployee_name());
        task.setManager_email(taskDTO.getManager_email());
        task.setStart_date(taskDTO.getStart_date());
        task.setDeadLine(taskDTO.getDeadLine());
        return taskDAO.save(task);
    }

    public List<Task> getAllTasks() {
        return taskDAO.findAll();
    }

    public Task getTaskById(Long id) {
        return taskDAO.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task updateTask(Long id, TaskDTO taskDTO) {
        Task task = getTaskById(id);
        task.setTask_name(taskDTO.getTask_name());
        task.setEmployee_name(taskDTO.getEmployee_name());
        task.setManager_email(taskDTO.getManager_email());
        task.setStart_date(taskDTO.getStart_date());
        task.setDeadLine(taskDTO.getDeadLine());
        return taskDAO.save(task);
    }

    public void deleteTask(Long id) {
        taskDAO.deleteById(id);
    }

    public List<Task> getTasksForEmployee(String employeeName) {
        return taskDAO.findAll().stream()
                .filter(task -> task.getEmployee_name().equalsIgnoreCase(employeeName))
                .collect(Collectors.toList());
    }
}

