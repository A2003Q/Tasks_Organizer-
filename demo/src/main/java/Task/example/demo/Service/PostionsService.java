package Task.example.demo.Service;

import Task.example.demo.DAO.PostionsSignUpDTO;
import Task.example.demo.Entity.Postions;

public interface PostionsService {
    void registerUser(PostionsSignUpDTO postionsSignUpDTO);
    Postions findByEmail(String email);
    boolean existsByEmail(String email);
    boolean validatePassword(String rawPassword, String storedPassword);
}
