package _inc.studentApp.DTO;

import _inc.studentApp.complexKeys.DistanceKey;
import _inc.studentApp.model.Distance;
import _inc.studentApp.model.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DistanceRequest {
    @NotBlank
    private String unitFrom;
    @NotBlank
    private String unitTo;
    @NotNull
    private int time;

    public static DistanceRequest fromEntity(Distance distance){
        DistanceRequest ddto = new DistanceRequest();
        ddto.setUnitFrom(distance.getId().getUnitFrom());
        ddto.setUnitTo(distance.getId().getUnitTo());
        ddto.setTime(distance.getTimeMinutes());
        return ddto;
    }

    public Distance toEntity(Unit uf){
        Distance dist = new Distance();

        dist.setId(new DistanceKey(unitFrom, unitTo));
        dist.setTimeMinutes(time);
        dist.setUnitF(uf);

        return dist;
    }
}
