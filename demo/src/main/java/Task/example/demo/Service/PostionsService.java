package Task.example.demo.Service;

import Task.example.demo.DAO.SignUpDTO;
import Task.example.demo.Entity.Postions;

public interface PostionsService {
    void registerUser(SignUpDTO signUpDTO);
    Postions findByEmail(String email);
    boolean existsByEmail(String email);
    boolean validatePassword(String rawPassword, String storedPassword);

}
