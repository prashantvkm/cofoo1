package cofoo.entities;

import java.math.BigInteger;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Employee extends BaseEntity{

    private String name;
    private Long idCardNo;
    private String email;
    private String mobileNo;
}
