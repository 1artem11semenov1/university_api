package _inc.studentApp.repository;

import _inc.studentApp.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    void deleteById(Long id);
    //Position findById(Long id);
    Optional<Position> findByPositionName(String positionName);
}
