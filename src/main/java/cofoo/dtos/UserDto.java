package cofoo.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import cofoo.annotations.MobileNo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @NotBlank(message="Name should not be null or blank")
    private String name;

    @NotBlank(message="Password should not be null or blank")
    private String password;

    @NotBlank(message="Email should not be null or blank")
    @Email(message="Please provide a valid Email address")
    private String email;

    @NotBlank(message="Mobile no should not be null or blank")
    @MobileNo
    private String mobileNo;
}
