package cofoo.repos;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cofoo.entities.User;
import cofoo.entities.UserCouponConfig;
import cofoo.enums.EntityStatus;

@Repository
public interface UserCouponConfigRepo extends JpaRepository<UserCouponConfig,Long> {
	
    //public Optional<UserCouponConfig> findByUseDateAndUserAndStatus(Date userDate, User user, EntityStatus status);
}
