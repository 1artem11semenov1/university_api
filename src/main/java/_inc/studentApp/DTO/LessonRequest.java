package _inc.studentApp.DTO;

import _inc.studentApp.complexKeys.LessonKey;
import _inc.studentApp.model.ClassRoom;
import _inc.studentApp.model.Disciplines;
import _inc.studentApp.model.Lesson;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest {
    @NotNull
    private Long disciplineID;
    @NotNull
    private Long classRoomID;
    @NonNull
    private Date date;

    public static LessonRequest fromEntity(Lesson lesson){
        LessonRequest ldto = new LessonRequest();
        ldto.setDisciplineID(lesson.getId().getDiscipline());
        ldto.setClassRoomID(lesson.getId().getClassroom());
        ldto.setDate(lesson
                .getId()
                .getDate());

        return ldto;
    }

    public Lesson toEntity(Disciplines disc, ClassRoom classRoom){
        Lesson lesson = new Lesson();
        LessonKey key = new LessonKey(
                disc.getId(),
                classRoom.getId(),
                date
        );
        lesson.setId(key);
        lesson.setDiscipline(disc);
        lesson.setClassroom(classRoom);

        return lesson;
    }
}
