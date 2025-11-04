package _inc.studentApp.DTO;

import _inc.studentApp.model.Employee;
import _inc.studentApp.model.Position;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class EmployeeRequest {
    private String email;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> positions = new ArrayList<>();
    private int experience;

    public static EmployeeRequest fromEntity(Employee e){
        EmployeeRequest edto = new EmployeeRequest();
        edto.setEmail(e.getEmail());
        edto.setFirstName(e.getFirstName());
        edto.setLastName(e.getLastName());
        edto.setDateOfBirth(e.getDateOfBirth());
        edto.setPositions(e.getPositions().stream().map(Position::getPositionName).toList());
        edto.setExperience(e.getExperience());

        return edto;
    }
}
