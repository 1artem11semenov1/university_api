package _inc.studentApp.repository;

import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.model.Disciplines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisciplineRepository extends JpaRepository<Disciplines, DisciplinesKey> {
    //void deleteByDisciplineId(DisciplinesKey id);
    //Disciplines findByDisciplinesId(DisciplinesKey id);
}
