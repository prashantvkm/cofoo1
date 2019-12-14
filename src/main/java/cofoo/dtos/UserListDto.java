package cofoo.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import cofoo.annotations.MobileNo;
import cofoo.enums.EntityStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListDto {

    private Long id;
    private String name;
    private String email;
    private String mobileNo;
    private EntityStatus status;
}
