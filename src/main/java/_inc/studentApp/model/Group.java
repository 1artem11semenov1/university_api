package _inc.studentApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Student> students = new LinkedList<>();
    @OneToMany(mappedBy = "groupName", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Disciplines> disciplines = new ArrayList<>();
}
