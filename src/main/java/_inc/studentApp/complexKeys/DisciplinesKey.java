package _inc.studentApp.complexKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DisciplinesKey implements Serializable {
    @Column(name = "discipline_name")
    private String disciplineName;
    @Column(name = "group_name")
    private String groupName;
}
