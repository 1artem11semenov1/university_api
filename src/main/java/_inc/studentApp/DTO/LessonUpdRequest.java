package _inc.studentApp.DTO;

import _inc.studentApp.complexKeys.ClassRoomKey;
import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.complexKeys.LessonKey;
import _inc.studentApp.model.ClassRoom;
import _inc.studentApp.model.Disciplines;
import _inc.studentApp.model.Lesson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Date;

@Getter
public class LessonUpdRequest {
    @NotBlank
    private String disciplineName;
    @NotBlank
    private String groupName;
    @NotBlank
    private String teacherEmail;
    @NotBlank
    private String classroomNumber;
    @NotBlank
    private String unitName;
    @NotNull
    private Date date;

    @NotBlank
    private String newDisciplineName;
    @NotBlank
    private String newGroupName;
    @NotBlank
    private String newTeacherEmail;
    @NotBlank
    private String newClassroomNumber;
    @NotBlank
    private String newUnitName;
    @NotNull
    private Date newDate;

    public Lesson toEntity(Disciplines disc, ClassRoom classRoom){
        Lesson lesson = new Lesson();
        LessonKey key = new LessonKey(
                disc.getId(),
                classRoom.getId(),
                newDate
        );
        lesson.setId(key);
        lesson.setDiscipline(disc);
        lesson.setClassroom(classRoom);

        return lesson;
    }

    public LessonKey getOldId(){
        return new LessonKey(
                new DisciplinesKey(disciplineName, groupName, teacherEmail),
                new ClassRoomKey(classroomNumber, unitName),
                date
        );
    }

    public LessonKey getNewId(){
        return new LessonKey(
                new DisciplinesKey(newDisciplineName, newGroupName, newTeacherEmail),
                new ClassRoomKey(newClassroomNumber, newUnitName),
                newDate
        );
    }
}
