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
import lombok.NonNull;

import java.util.Date;

@Getter
public class LessonUpdRequest {
    @NotNull
    private Long disciplineID;
    @NotNull
    private Long classRoomID;
    @NonNull
    private Date date;

    @NotNull
    private Long newDisciplineID;
    @NotNull
    private Long newClassRoomID;
    @NonNull
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
                disciplineID,
                classRoomID,
                date
        );
    }

    public LessonKey getNewId(){
        return new LessonKey(
                newDisciplineID,
                newDisciplineID,
                newDate
        );
    }
}
