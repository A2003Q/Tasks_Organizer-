package Task.example.demo.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="Task")
@Data
@NoArgsConstructor
public class Task {
    @Id  // the primary key generation strategy
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Task_seq")
    @SequenceGenerator(name = "Task_seq", sequenceName = "global_Task_id_seq", initialValue = 1, allocationSize = 1)
    private long id ;
    private String Task_name ;
    private String Employee_name;
    private String Manager_email;
    private Date Start_date;
    private Date DeadLine ;

    public Date getDeadLine() {
        return DeadLine;
    }

    public void setDeadLine(Date deadLine) {
        DeadLine = deadLine;
    }

    public String getEmployee_name() {
        return Employee_name;
    }

    public void setEmployee_name(String employee_name) {
        Employee_name = employee_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getManager_email() {
        return Manager_email;
    }

    public void setManager_email(String manager_email) {
        Manager_email = manager_email;
    }

    public Date getStart_date() {
        return Start_date;
    }

    public void setStart_date(Date start_date) {
        Start_date = start_date;
    }

    public String getTask_name() {
        return Task_name;
    }

    public void setTask_name(String task_name) {
        Task_name = task_name;
    }
}
