package _inc.studentApp.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UnitUpdRequest {
    @NotBlank
    private String oldName;

    @NotBlank
    private String newName;
    @NotBlank
    private String newAddress;
}
