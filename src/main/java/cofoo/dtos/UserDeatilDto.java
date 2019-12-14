package cofoo.dtos;

import cofoo.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDeatilDto {
    private UserListDto userListDto;
    private GroupListDto groupListDto;
}
