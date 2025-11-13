package _inc.studentApp.repository;

import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.model.Disciplines;
import _inc.studentApp.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

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

    @Query(value = "UPDATE disciplines"
            +" SET discipline_name = :newDName, group_name = :newGName, teacher_email = :newTEmail, count_hours = :newCH"
            +" WHERE discipline_name = :dName AND group_name = :gName AND teacher_email = :tEmail"
            +" RETURNING *",
            nativeQuery = true)
    Disciplines update (@Param("dName") String dName,
                        @Param("gName") String gName,
                        @Param("tEmail") String tEmail,
                        @Param("newDName") String newDName,
                        @Param("newGName") String newGName,
                        @Param("newTEmail") String newTEmail,
                        @Param("newCH") int newCountHours);
}
