package Task.example.demo.RestController;


import Task.example.demo.DAO.LoginDTO;
import Task.example.demo.Entity.Postions;
import Task.example.demo.Service.PostionsService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api2")
public class LoginRestController {
    private static final Logger logger = LoggerFactory.getLogger(LoginRestController.class);

    private final PostionsService postionsService ;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}") // Use the same secret key as in the filter
    private String SECRET_KEY;

    @Autowired
    public LoginRestController(PostionsService postionsService , PasswordEncoder passwordEncoder) {
        this.postionsService=postionsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        Postions postions = postionsService.findByEmail(loginDTO.getEmail());

        if (postions== null) {
            logger.warn("Login failed: User not found with email {}", loginDTO.getEmail());
            return ResponseEntity.status(401).body("User not found");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), postions.getPassword())) {
            logger.warn("Login failed: Invalid password for user {}", loginDTO.getEmail());
            return ResponseEntity.status(401).body("Invalid password");
        }

        Postions.Role role = postions.getRole();
        if (role == null) {
            logger.error("Login failed: User role is not defined for user {}", loginDTO.getEmail());
            return ResponseEntity.status(500).body("User role is not defined");
        }

        String token = generateToken(postions);

        Map<String, String> response = new HashMap<>();
        response.put("role", role.name());
        response.put("token", token);

        logger.info("User {} logged in successfully with role {}", postions.getEmail(), role.name());
        return ResponseEntity.ok(response);
    }

    private String generateToken(Postions postions) {
        return JWT.create()
                .withSubject(postions.getEmail())
                .withClaim("role", postions.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }
}
