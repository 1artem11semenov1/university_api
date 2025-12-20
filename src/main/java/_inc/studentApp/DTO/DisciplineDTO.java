package _inc.studentApp.DTO;

import _inc.studentApp.model.Disciplines;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DisciplineDTO {
    private String disciplineName;
    private String groupName;
    private String teacherEmail;
    private int countHours;

    public static DisciplineDTO fromEntity(Disciplines discipline){
        DisciplineDTO ddto = new DisciplineDTO();
        ddto.setDisciplineName(discipline.getDisciplineName());
        ddto.setGroupName(discipline.getGroup().getGroupName());
        ddto.setTeacherEmail(discipline.getTeacher().getEmail());
        ddto.setCountHours(discipline.getCountHours());
        return ddto;
    }
}
