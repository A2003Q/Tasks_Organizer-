package Task.example.demo.DAO;

import Task.example.demo.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverEmail(String receiverEmail);
}

