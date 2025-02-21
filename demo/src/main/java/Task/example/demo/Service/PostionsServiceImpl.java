package Task.example.demo.Service;

import Task.example.demo.DAO.EmployeeDAO;
import Task.example.demo.DAO.ManagerDAO;
import Task.example.demo.DAO.PostionsDAO;
import Task.example.demo.DAO.SignUpDTO;
import Task.example.demo.Entity.Employee;
import Task.example.demo.Entity.Manager;
import Task.example.demo.Entity.Postions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PostionsServiceImpl implements PostionsService{
    private final PostionsDAO postionsDAO ;
    private final ManagerDAO managerDAO ;
    private final EmployeeDAO employeeDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PostionsServiceImpl(EmployeeDAO employeeDAO, PostionsDAO postionsDAO, ManagerDAO managerDAO, BCryptPasswordEncoder passwordEncoder) {
        this.employeeDAO = employeeDAO;
        this.postionsDAO = postionsDAO;
        this.managerDAO = managerDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void registerUser(SignUpDTO signUpDTO) {
        if (existsByEmail(signUpDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        String role = signUpDTO.getRole();
        switch (role.toUpperCase()) {
            case "MANAGER":
                Manager manager = new Manager();
                populateUserFields(manager, signUpDTO);
                managerDAO.save(manager);
                break;
            case "EMPLOYEE":
                Employee employee = new Employee();
                populateUserFields(employee, signUpDTO);
                employeeDAO.save(employee);
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
    private void populateUserFields(Postions postions, SignUpDTO dto) {
       postions.setName(dto.getName());
        postions.setEmail(dto.getEmail());
        postions.setPassword(passwordEncoder.encode(dto.getPassword()));
        postions.setRole(Postions.Role.valueOf(dto.getRole().toUpperCase()));
    }
    @Override
    public Postions findByEmail(String email) {
        return postionsDAO.findByEmail(email);
    }
    // Manually check if the email exists in the database
    public boolean existsByEmail(String email) {
        // Query the userDAO to check if the email already exists
        Postions position = postionsDAO.findByEmail(email);
        return position != null;  // Returns true if a user is found, otherwise false
    }
    @Override
    public boolean validatePassword(String rawPassword, String storedPassword) {
        return passwordEncoder.matches(rawPassword, storedPassword);
    }



}
