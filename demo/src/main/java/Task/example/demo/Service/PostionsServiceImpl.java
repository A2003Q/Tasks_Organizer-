package Task.example.demo.Service;

import Task.example.demo.DAO.*;
import Task.example.demo.Entity.AppUser;
import Task.example.demo.Entity.Employee;
import Task.example.demo.Entity.Manager;
import Task.example.demo.Entity.Postions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostionsServiceImpl implements PostionsService {
    private final PostionsDAO postionsDAO;
    private final ManagerDAO managerDAO;
    private final EmployeeDAO employeeDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;

    @Autowired
    public PostionsServiceImpl(EmployeeDAO employeeDAO, PostionsDAO postionsDAO, ManagerDAO managerDAO, BCryptPasswordEncoder passwordEncoder, AppUserRepository appUserRepository) {
        this.employeeDAO = employeeDAO;
        this.postionsDAO = postionsDAO;
        this.managerDAO = managerDAO;
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional
    public void registerUser(SignUpDTO signUpDTO) {
        if (existsByEmail(signUpDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        String role = signUpDTO.getRole().toUpperCase();
        Postions newUser;
        switch (role) {
            case "MANAGER":
                newUser = new Manager();
                managerDAO.save((Manager) newUser);
                break;
            case "EMPLOYEE":
                newUser = new Employee();
                employeeDAO.save((Employee) newUser);
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
        populateUserFields(newUser, signUpDTO);

        // Save user in AppUser if not already present
        if (!appUserRepository.findByEmail(signUpDTO.getEmail()).isPresent()) {
            AppUser appUser = new AppUser();
            appUser.setEmail(signUpDTO.getEmail());
            appUser.setRole(role);
            appUserRepository.save(appUser);
        }
    }

    private void populateUserFields(Postions postions, SignUpDTO dto) {
        postions.setName(dto.getName());
        postions.setEmail(dto.getEmail());
        postions.setPassword(passwordEncoder.encode(dto.getPassword()));
        postions.setRole(Postions.Role.valueOf(dto.getRole().toUpperCase()));
    }

    @Override
    public Optional<Postions> findByEmail(String email) {
        return postionsDAO.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return postionsDAO.findByEmail(email).isPresent();
    }

    @Override
    public boolean validatePassword(String rawPassword, String storedPassword) {
        return passwordEncoder.matches(rawPassword, storedPassword);
    }
}



