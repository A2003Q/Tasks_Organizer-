package Task.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "postions")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Postions {
    @Id  // the primary key generation strategy
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "global_user_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(unique = true) // unique constraint in database .
    private String email;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING) //an enumeration for roles in a JPA entity and specifies how the role will be stored in the database.
    private Role role;

    public enum Role { //Provides a restricted set of allowed values.
        MANAGER,EMPLOYEE
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
