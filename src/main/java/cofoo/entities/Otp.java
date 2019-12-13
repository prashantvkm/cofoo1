package cofoo.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import cofoo.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Otp extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String code;
    private Date expiredAt;

    public Otp(User user, String code, Date expiredAt, EntityStatus status){
        setUser(user);
        setCode(code);
        setExpiredAt(expiredAt);
        setStatus(status);
    }

    public static Otp createOtp(User user){
        return new Otp(
                user,
                String.valueOf(new Random().nextInt(9999)),
                new Date(Calendar.getInstance().getTimeInMillis() + (5 * 60000)),
                EntityStatus.active);
    }
}
