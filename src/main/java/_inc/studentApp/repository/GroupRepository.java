package _inc.studentApp.repository;

import _inc.studentApp.model.Group;
import _inc.studentApp.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, String> {
    void deleteByGroupName(String name);
    Optional<Group> findByGroupName(String name);

    @Query(value = "UPDATE groups SET group_name = :newName WHERE group_name = :oldName RETURNING *", nativeQuery = true)
    Group update (@Param("oldName") String oldName, @Param("newName") String newName);


}
