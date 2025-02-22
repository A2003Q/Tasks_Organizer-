package Task.example.demo.RestController;

import Task.example.demo.DAO.ProfileDTO;
import Task.example.demo.Entity.Postions;
import Task.example.demo.Service.PostionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping
public class ProfileRestController {

    private final PostionsService postionsService;

    @Autowired
    public ProfileRestController(PostionsService postionsService) {
        this.postionsService = postionsService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        System.out.println("Extracted email from authentication: " + email); // Debugging

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not found in authentication.");
        }

        Optional<Postions> user = postionsService.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Safely retrieve user information
        Postions userDetails = user.get();

        ProfileDTO profileDTO = new ProfileDTO(userDetails.getName(), userDetails.getEmail(), userDetails.getRole().toString());
        return ResponseEntity.ok(profileDTO);
    }
}





