package _inc.studentApp.repository;

import _inc.studentApp.complexKeys.ClassRoomKey;
import _inc.studentApp.model.ClassRoom;
import _inc.studentApp.model.Disciplines;
import _inc.studentApp.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    @Query(value = "UPDATE classrooms"
            +" SET number = :newNum, capacity = :newCapacity"
            +" WHERE unit_id = :uid and number = :oldNum"
            +" RETURNING *",
            nativeQuery = true)
    ClassRoom update (@Param("uid") Long unitId,
                      @Param("oldNum") String oldNumber,
                      @Param("newNum") String newNumber,
                      @Param("newCapacity") int newCapacity);

    @Query(value = "SELECT * FROM classrooms"
            +" WHERE number = :cnum AND unit_id = :uid",
            nativeQuery = true)
    Optional<ClassRoom> findByPotentialKey (@Param("cnum") String classRoomNumber,
                                              @Param("uid") Long unitId);
}
