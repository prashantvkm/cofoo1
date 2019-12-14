package cofoo.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cofoo.enums.EntityStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupListDto {

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Boolean lunch;
    @NotNull
    private Boolean breakfast;
    @NotNull
    private Boolean dinner;
    @NotNull
    private EntityStatus status;
}
