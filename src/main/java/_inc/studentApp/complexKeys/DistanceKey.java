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
public class DistanceKey implements Serializable {
    @Column(name = "unit_from")
    String unitFrom;

    @Column(name = "unit_to")
    String unitTo;
}
