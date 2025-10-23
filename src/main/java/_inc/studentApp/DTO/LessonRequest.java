package _inc.studentApp.DTO;

import _inc.studentApp.complexKeys.LessonKey;
import _inc.studentApp.model.ClassRoom;
import _inc.studentApp.model.Disciplines;
import _inc.studentApp.model.Lesson;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest {
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
    @NonNull
    private Date date;

    public static LessonRequest fromEntity(Lesson lesson){
        LessonRequest ldto = new LessonRequest();
        ldto.setDisciplineName(lesson
                .getId()
                .getDiscipline()
                .getName());
        ldto.setGroupName(lesson
                .getId()
                .getDiscipline()
                .getGroupName());
        ldto.setTeacherEmail(lesson
                .getId()
                .getDiscipline()
                .getTeacherEmail()
        );
        ldto.setClassroomNumber(lesson
                .getId()
                .getClassroom()
                .getClassroomNumber());
        ldto.setUnitName(lesson
                .getId()
                .getClassroom()
                .getUnitName());
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
