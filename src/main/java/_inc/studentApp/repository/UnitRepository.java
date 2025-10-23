package _inc.studentApp.repository;

import _inc.studentApp.model.Lesson;
import _inc.studentApp.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, String> {
}
