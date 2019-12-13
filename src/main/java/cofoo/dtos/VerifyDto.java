package cofoo.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyDto {
    private String otp;
    private String email;
}
