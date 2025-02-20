package Task.example.demo.DAO;

import org.aspectj.weaver.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostionsDAO extends JpaRepository<Position,Long> {
}
