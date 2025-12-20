package _inc.studentApp.DTO;

import _inc.studentApp.model.ClassRoom;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomRequest {
    private String classroomNumber;
    private Long unitID;
    private int capacity;

    public static ClassRoomRequest fromEntity(ClassRoom classRoom){
        ClassRoomRequest cdto = new ClassRoomRequest();
        cdto.setClassroomNumber(classRoom.getClassroomNumber());
        cdto.setUnitID(classRoom.getUnitID());
        cdto.setCapacity(classRoom.getCapacity());
        return cdto;
    }
}
