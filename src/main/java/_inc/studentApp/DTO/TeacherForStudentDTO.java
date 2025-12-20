package _inc.studentApp.DTO;

import _inc.studentApp.model.Employee;
import _inc.studentApp.model.Position;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Setter
public class TeacherForStudentDTO {
    private String email;
    private String firstName;
    private String lastName;
    private List<String> positions;
    private int experience;

    public static TeacherForStudentDTO fromEntity(Employee employee){
        TeacherForStudentDTO tfsdto = new TeacherForStudentDTO();

        tfsdto.setEmail(employee.getEmail());
        tfsdto.setFirstName(employee.getFirstName());
        tfsdto.setLastName(employee.getLastName());
        tfsdto.setPositions(
                employee.getPositions()
                        .stream().map(Position::getPositionName)
                        .collect(Collectors.toList())
        );
        tfsdto.setExperience(employee.getExperience());

        return tfsdto;
    }
}
