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
    private String unitName;
    private int capacity;

    public static ClassRoomRequest fromEntity(ClassRoom classRoom){
        ClassRoomRequest cdto = new ClassRoomRequest();
        cdto.setClassroomNumber(classRoom.getId().getClassroomNumber());
        cdto.setUnitName(classRoom.getId().getUnitName());
        cdto.setCapacity(classRoom.getCapacity());
        return cdto;
    }
}
