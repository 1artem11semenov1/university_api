package _inc.studentApp.repository;

import _inc.studentApp.complexKeys.ClassRoomKey;
import _inc.studentApp.model.ClassRoom;
import _inc.studentApp.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, ClassRoomKey> {
    @Query(value = "UPDATE classrooms"
            +" SET number = :newNum, capacity = :newCapacity"
            +" WHERE unit_name = :unitName and number = :oldNum"
            +" RETURNING *",
            nativeQuery = true)
    ClassRoom update (@Param("unitName") String unitName,
                      @Param("oldNum") String oldNumber,
                      @Param("newNum") String newNumber,
                      @Param("newCapacity") int newCapacity);
}
