package cofoo.entities;

import java.util.Date;

import javax.persistence.*;

import cofoo.enums.CouponType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserCouponConfig extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CouponType couponType;
}
