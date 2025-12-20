package _inc.studentApp.repository;

import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.model.Disciplines;
import _inc.studentApp.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.util.List;
import java.util.Optional;

public interface DisciplineRepository extends JpaRepository<Disciplines, Long> {
    //void deleteByDisciplineId(DisciplinesKey id);
    //Disciplines findByDisciplinesId(DisciplinesKey id);
    @Query(
            value = "SELECT disciplines.id, count_hours, group_id, teacher_id, discipline_name FROM disciplines" +
                    " JOIN groups ON disciplines.group_id = groups.id" +
                    " WHERE group_name = :inp_group"
            , nativeQuery = true
    )
    List<Disciplines> findByGroupName(@Param("inp_group") String groupName);

    @Query(
            value = "SELECT disciplines.id, count_hours, group_id, teacher_id, discipline_name FROM disciplines" +
                    " JOIN employees on teacher_id = employees.id" +
                    " WHERE employees.email = :inp_email"
            , nativeQuery = true
    )
    List<Disciplines> findByTeacherEmail(@Param("inp_email") String email);

    @Query(value = "UPDATE disciplines"
            +" SET discipline_name = :newDName, group_id = :newGid, teacher_id = :newTid, count_hours = :newCH"
            +" WHERE discipline_name = :dName AND group_id = :gid AND teacher_id = :tid"
            +" RETURNING *",
            nativeQuery = true)
    Disciplines update (@Param("dName") String dName,
                        @Param("gid") Long gid,
                        @Param("tid") Long tid,
                        @Param("newDName") String newDName,
                        @Param("newGid") Long newGid,
                        @Param("newTid") Long newTid,
                        @Param("newCH") int newCountHours);
    @Query(value = "SELECT * FROM disciplines"
            +" WHERE discipline_name = :dname AND group_id = :gid AND teacher_id = :tid",
            nativeQuery = true)
    Optional<Disciplines> findByPotentialKey (@Param("dname") String disciplineName,
                                              @Param("gid") Long groupId,
                                              @Param("tid") Long teacherId);
}
