package Task.example.demo.RestController;

import Task.example.demo.DAO.AppUserRepository;
import Task.example.demo.DAO.MessageDTO;
import Task.example.demo.DAO.MessageRepository;
import Task.example.demo.Entity.AppUser;
import Task.example.demo.Entity.Message;
import Task.example.demo.Service.MessageService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.aspectj.bridge.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(LoginRestController.class);
    private final MessageRepository messageRepository;
    private final AppUserRepository appUserRepository;
    private final MessageService messageService; // Assuming you have a MessageService to handle message logic
    @Value("${jwt.secret}") // Use the same secret key as in the filter
    private String SECRET_KEY;
    @Autowired
    public MessageController(MessageRepository messageRepository, AppUserRepository appUserRepository, MessageService messageService) {
        this.messageRepository = messageRepository;
        this.appUserRepository = appUserRepository;
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageRequest) {
        try {
            Optional<AppUser> senderOptional = appUserRepository.findByEmail(messageRequest.getSenderEmail());
            Optional<AppUser> receiverOptional = appUserRepository.findByEmail(messageRequest.getReceiverEmail());

            if (!senderOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sender not found");
            }

            if (!receiverOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Receiver not found");
            }

            AppUser sender = senderOptional.get();
            AppUser receiver = receiverOptional.get();

            Message newMessage = new Message();
            newMessage.setSender(sender);
            newMessage.setReceiver(receiver);
            newMessage.setContent(messageRequest.getContent());
            newMessage.setTimestamp(LocalDateTime.now());

            messageRepository.save(newMessage);

            return ResponseEntity.ok("Message sent successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending message: " + e.getMessage());
        }
    }


    private String extractEmailFromToken(String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {  // ✅ Ensure token is present
                throw new IllegalArgumentException("Missing or malformed token");
            }

            String jwt = token.substring(7);  // ✅ Remove "Bearer " prefix
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(jwt);

            logger.info("Extracted Email: " + decodedJWT.getSubject());
            return decodedJWT.getSubject();
        } catch (Exception e) {
            logger.error("Invalid token", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token", e);
        }
    }



    @GetMapping("/inbox")
    public ResponseEntity<List<Message>> getInboxMessages(@RequestHeader("Authorization") String token) {
        String userEmail = extractEmailFromToken(token); // Fix this method
        List<Message> messages = messageService.getMessagesByUser(userEmail);

        return ResponseEntity.ok(messages.isEmpty() ? Collections.emptyList() : messages);
    }



}





