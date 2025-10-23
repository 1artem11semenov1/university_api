package _inc.studentApp.repository;

import _inc.studentApp.complexKeys.ClassRoomKey;
import _inc.studentApp.model.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, ClassRoomKey> {
}
