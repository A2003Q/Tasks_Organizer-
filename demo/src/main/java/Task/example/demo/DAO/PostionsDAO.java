package Task.example.demo.DAO;

import Task.example.demo.Entity.Postions;
import org.aspectj.weaver.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostionsDAO extends JpaRepository<Postions,Long> {
   Optional<Postions> findByEmail(String email);
}
