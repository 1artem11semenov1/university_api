package _inc.studentApp.model;

import _inc.studentApp.complexKeys.ClassRoomKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Table(name = "classrooms")
public class ClassRoom {
    @EmbeddedId
    ClassRoomKey id;
    int capacity;

    @ManyToOne
    @MapsId("unitName")
    @JoinColumn(name = "unit_name")
    @JsonIgnore
    Unit unit;

    @JsonIgnore
    @OneToMany(mappedBy = "classroom")
    List<Lesson> lessons = new LinkedList<>();
}
