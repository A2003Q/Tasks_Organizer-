package Task.example.demo.DAO;

import Task.example.demo.Entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerDAO extends JpaRepository<Manager, Long> {
}
