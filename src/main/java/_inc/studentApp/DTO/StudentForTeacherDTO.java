package _inc.studentApp.DTO;

import _inc.studentApp.model.Student;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class StudentForTeacherDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String group;
    private String enterYear;

    public static StudentForTeacherDTO fromEntity(Student student){
        StudentForTeacherDTO sftdto = new StudentForTeacherDTO();

        sftdto.setEmail(student.getEmail());
        sftdto.setFirstName(student.getFirstName());
        sftdto.setLastName(student.getLastName());
        sftdto.setGroup(student.getGroup().getGroupName());
        sftdto.setEnterYear(student.getEnterYear());

        return sftdto;
    }
}
