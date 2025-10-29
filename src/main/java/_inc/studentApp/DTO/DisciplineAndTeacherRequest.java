package _inc.studentApp.DTO;

import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineAndTeacherRequest {
    DisciplineDTO discipline;
    EmployeeDTO teacher;
    List<String> positions;
}
