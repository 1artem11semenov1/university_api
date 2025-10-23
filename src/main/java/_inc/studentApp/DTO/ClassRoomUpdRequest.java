package _inc.studentApp.DTO;

import _inc.studentApp.complexKeys.ClassRoomKey;
import lombok.Getter;

@Getter
public class ClassRoomUpdRequest {
    private ClassRoomKey old;

    private String newNumber;
    private int newCapacity;
}
