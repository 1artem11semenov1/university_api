package _inc.studentApp.repository;

import _inc.studentApp.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {
    void deleteById(Long id);
    //Position findById(Long id);
    List<Position> findByPositionName(String positionName);
}
