package cofoo.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterResponseDto {
    private String name;
    private String email;
    private String mobileNo;
}
