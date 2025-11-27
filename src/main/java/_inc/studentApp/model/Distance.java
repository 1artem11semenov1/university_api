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
    @JoinColumn(
            name = "unit_from",
            foreignKey = @ForeignKey(
                    name = "units_distances",
                    foreignKeyDefinition = "FOREIGN KEY (unit_from) REFERENCES units(unit_name) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    @JsonIgnore
    Unit unitF;

    @ManyToOne
    @MapsId("unitTo")
    @JoinColumn(
            name = "unit_to",
            foreignKey = @ForeignKey(
                    name = "units_distances_to",
                    foreignKeyDefinition = "FOREIGN KEY (unit_to) REFERENCES units(unit_name) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    @JsonIgnore
    Unit unitT;
}
