package cofoo.repos;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cofoo.entities.User;
import cofoo.entities.UserCouponDailyLog;
import cofoo.enums.CouponType;

@Repository
public interface UserCouponDailyLogRepo extends JpaRepository<UserCouponDailyLog,Long> {

    public Optional<UserCouponDailyLog> findByUserAndCouponType(User user, CouponType couponType);
}
