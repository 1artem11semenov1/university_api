package _inc.studentApp.DTO;

import _inc.studentApp.model.Employee;
import _inc.studentApp.model.Position;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
public class EmployeeDTO {
    private String email;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private int age;
    //private List<String> positions;
    private int experience;

    public static EmployeeDTO fromEntity(Employee employee){
        EmployeeDTO edto = new EmployeeDTO();

        edto.setEmail(employee.getEmail());
        edto.setFirstName(employee.getFirstName());
        edto.setLastName(employee.getLastName());
        edto.setDateOfBirth(employee.getDateOfBirth());
        edto.setAge(employee.getAge());
        edto.setExperience(employee.getExperience());

        /*if (employee.getPositions() != null) {
            List<String> positionsList = new ArrayList<>();
            for (Position p : employee.getPositions()) {
                positionsList.add(p.getPositionName());
            }
            edto.setPositions(positionsList);
        } else {
            edto.setPositions(Collections.emptyList());
        }*/

        return edto;
    }
}
