package _inc.studentApp.repository;

import _inc.studentApp.complexKeys.DistanceKey;
import _inc.studentApp.model.Distance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistanceRepository extends JpaRepository<Distance, DistanceKey> {
}
