package cofoo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cofoo.entities.UserCouponDailyLog;

@Repository
public interface UserCouponDailyLogRepo extends JpaRepository<UserCouponDailyLog,Long> {
}
