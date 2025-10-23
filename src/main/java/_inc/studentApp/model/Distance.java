package _inc.studentApp.model;

import _inc.studentApp.complexKeys.DistanceKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "distances")
public class Distance {
    @EmbeddedId
    DistanceKey id;

    @Column(name = "time_minutes")
    int timeMinutes;

    @ManyToOne
    @MapsId("unitFrom")
    @JoinColumn(name = "unit_from")
    @JsonIgnore
    Unit unitF;
}
