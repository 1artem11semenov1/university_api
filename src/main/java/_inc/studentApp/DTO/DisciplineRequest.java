package _inc.studentApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@AllArgsConstructor
public class DisciplineRequest {
    @NotNull
    private String disciplineName;
    @NotNull
    private String groupName;
    @NotNull
    private String teacherEmail;
}
