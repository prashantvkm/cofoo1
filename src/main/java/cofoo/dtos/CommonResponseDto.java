package cofoo.dtos;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import cofoo.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseDto {
    private String message;

    @Enumerated(EnumType.STRING)
    private EntityStatus status;
    private Object data;
}
