package _inc.studentApp.model;

import _inc.studentApp.complexKeys.DisctanceKey;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "distances")
public class Distance {
    @EmbeddedId
    DisctanceKey id;

    @Column(name = "time_minutes")
    int timeMinutes;

    @ManyToOne
    @MapsId("unitFrom")
    @JoinColumn(name = "unit_from")
    Unit unitFrom;
}
