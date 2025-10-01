package _inc.studentApp.DTO;

import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class DisciplineUpdRequest {
    @NotNull
    private String disciplineName;
    @NotNull
    private String groupName;
    @NotNull
    private String teacherEmail;
    @NotNull
    private String newDisciplineName;
    @NotNull
    private String newGroupName;
    @NotNull
    private String newTeacherEmail;
}
