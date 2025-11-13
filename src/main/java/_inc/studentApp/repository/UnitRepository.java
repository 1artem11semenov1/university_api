package _inc.studentApp.repository;

import _inc.studentApp.model.Group;
import _inc.studentApp.model.Lesson;
import _inc.studentApp.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, String> {
    @Query(value = "UPDATE units"
            +" SET unit_name = :newName, address = :newAddress"
            +" WHERE unit_name = :oldName"
            +" RETURNING *",
            nativeQuery = true)
    Unit update (
            @Param("oldName") String oldName,
            @Param("newName") String newName,
            @Param("newAddress") String newAddress
    );
}
