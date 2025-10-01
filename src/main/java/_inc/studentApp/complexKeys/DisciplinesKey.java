package _inc.studentApp.complexKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class DisciplinesKey implements Serializable {
    private String disciplineName;
    private String groupName;
    private String teacherEmail;

    public String getName() {return disciplineName;}
}
