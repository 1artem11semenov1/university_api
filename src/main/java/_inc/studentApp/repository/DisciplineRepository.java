package _inc.studentApp.repository;

import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.model.Disciplines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DisciplineRepository extends JpaRepository<Disciplines, DisciplinesKey> {
    //void deleteByDisciplineId(DisciplinesKey id);
    //Disciplines findByDisciplinesId(DisciplinesKey id);
    @Query(
            value = "SELECT * FROM disciplines" +
                    " WHERE group_name = :inp_group"
            , nativeQuery = true
    )
    List<Disciplines> findByGroupName(@Param("inp_group") String groupName);

    @Query(
            value = "SELECT * FROM disciplines" +
                    " WHERE teacher_email = :inp_email"
            , nativeQuery = true
    )
    List<Disciplines> findByTeacherEmail(@Param("inp_email") String email);
}
