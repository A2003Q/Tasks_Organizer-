package Task.example.demo.Service;

import Task.example.demo.DAO.AppUserRepository;
import Task.example.demo.DAO.MessageDTO;
import Task.example.demo.DAO.MessageRepository;
import Task.example.demo.Entity.AppUser;
import Task.example.demo.Entity.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AppUserRepository appUserRepository) {
        this.messageRepository = messageRepository;
        this.appUserRepository = appUserRepository;
    }

    public void sendMessage(String senderEmail, String receiverEmail, String content) {
        Optional<AppUser> senderOpt = appUserRepository.findByEmail(senderEmail);
        Optional<AppUser> receiverOpt = appUserRepository.findByEmail(receiverEmail);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            throw new IllegalArgumentException("Sender or receiver not found.");
        }

        AppUser sender = senderOpt.get();
        AppUser receiver = receiverOpt.get();

        // Create a new message
        Message message = new Message();
        message.setSender(sender);  // Set the sender as AppUser
        message.setReceiver(receiver);  // Set the receiver as AppUser
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now()); // Ensure timestamp is set

        // Save the message
        messageRepository.save(message);
    }

    public List<Message> getMessagesByUser(String email) {
        return messageRepository.findByReceiverEmail(email);  // This might need adjustment to use AppUser relationships
    }
}



