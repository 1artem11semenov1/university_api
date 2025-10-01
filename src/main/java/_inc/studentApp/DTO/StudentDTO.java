package _inc.studentApp.DTO;

import _inc.studentApp.model.Student;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
public class StudentDTO {
    private String email;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private int age;
    private String group;
    private String level;
    private String enterYear;

    public static StudentDTO fromEntity(Student student){
        StudentDTO sdto = new StudentDTO();
        sdto.setEmail(student.getEmail());
        sdto.setFirstName(student.getFirstName());
        sdto.setLastName(student.getLastName());
        sdto.setDateOfBirth(student.getDateOfBirth());
        sdto.setAge(student.getAge());
        sdto.setGroup(student.getGroup().getGroupName());
        sdto.setLevel(student.getLevel());
        sdto.setEnterYear(student.getEnterYear());
        return sdto;
    }
}
