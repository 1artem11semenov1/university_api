package _inc.studentApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Table(name = "groups")
public class Group {
    @Id
    private String groupName;

    @OneToMany(mappedBy = "group")
    private List<Student> students = new LinkedList<>();
    @OneToMany(mappedBy = "groupName")
    private List<Disciplines> disciplines = new ArrayList<>();
}
