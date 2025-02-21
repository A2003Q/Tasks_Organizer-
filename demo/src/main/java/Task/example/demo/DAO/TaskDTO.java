package Task.example.demo.DAO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
@Data
public class TaskDTO {
    @NotBlank(message = "Task_name is required")
    private String Task_name ;
    @NotBlank(message = "Employee_name is required")
    private String Employee_name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String Manager_email;
    private Date Start_date;
    private Date DeadLine ;

    public TaskDTO(Date deadLine, String employee_name, String manager_email, Date start_date, String task_name) {
        DeadLine = deadLine;
        Employee_name = employee_name;
        Manager_email = manager_email;
        Start_date = start_date;
        Task_name = task_name;
    }

    public Date getDeadLine() {
        return DeadLine;
    }

    public void setDeadLine(Date deadLine) {
        DeadLine = deadLine;
    }

    public @NotBlank(message = "Employee_name is required") String getEmployee_name() {
        return Employee_name;
    }

    public void setEmployee_name(@NotBlank(message = "Employee_name is required") String employee_name) {
        Employee_name = employee_name;
    }

    public @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String getManager_email() {
        return Manager_email;
    }

    public void setManager_email(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String manager_email) {
        Manager_email = manager_email;
    }

    public Date getStart_date() {
        return Start_date;
    }

    public void setStart_date(Date start_date) {
        Start_date = start_date;
    }

    public @NotBlank(message = "Task_name is required") String getTask_name() {
        return Task_name;
    }

    public void setTask_name(@NotBlank(message = "Task_name is required") String task_name) {
        Task_name = task_name;
    }
}
