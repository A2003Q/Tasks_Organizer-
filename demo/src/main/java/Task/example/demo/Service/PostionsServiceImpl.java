package Task.example.demo.Service;

import Task.example.demo.DAO.EmployeeDAO;
import Task.example.demo.DAO.ManagerDAO;
import Task.example.demo.DAO.PostionsDAO;
import Task.example.demo.DAO.PostionsSignUpDTO;
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
    public void registerUser(PostionsSignUpDTO postionsSignUpDTO) {
        if (existsByEmail(postionsSignUpDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        String role =postionsSignUpDTO.getRole();
    }

    @Override
    public Postions findByEmail(String email) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean validatePassword(String rawPassword, String storedPassword) {
        return false;
    }
}
