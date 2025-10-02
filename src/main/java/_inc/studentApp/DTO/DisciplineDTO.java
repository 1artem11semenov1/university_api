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

    public static DisciplineDTO fromEntity(Disciplines discipline){
        DisciplineDTO ddto = new DisciplineDTO();
        ddto.setDisciplineName(discipline.getDisciplineName());
        ddto.setGroupName(discipline.getId().getGroupName());
        ddto.setTeacherEmail(discipline.getId().getTeacherEmail());
        return ddto;
    }
}
