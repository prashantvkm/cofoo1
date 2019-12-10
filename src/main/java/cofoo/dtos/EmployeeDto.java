package cofoo.dtos;

import java.math.BigInteger;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cofoo.annotations.MobileNo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDto {

    @NotBlank(message="Name should not be null or blank")
    private String name;

    @NotBlank(message="Email should not be null or blank")
    @Email(message="Please provide a valid Email address")
    private String email;

    @NotBlank(message="Mobile no should not be null or blank")
    @MobileNo
    private String mobileNo;

    @NotNull(message="ID Card no should not be null")
    private Long idCardNo;

}
