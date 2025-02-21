package Task.example.demo.DAO;

import Task.example.demo.Entity.Postions;
import org.aspectj.weaver.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostionsDAO extends JpaRepository<Postions,Long> {
   Postions findByEmail(String email);
}
