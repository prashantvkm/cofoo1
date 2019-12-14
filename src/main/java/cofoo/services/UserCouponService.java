package cofoo.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cofoo.dtos.CommonResponseDto;
import cofoo.entities.User;
import cofoo.entities.UserCouponDailyLog;
import cofoo.enums.CouponType;
import cofoo.repos.UserCouponDailyLogRepo;


@Service
public class UserCouponService {
	
	@Autowired
	private UserCouponDailyLogRepo userCouponDailyLogRepo;
	
	public CommonResponseDto scanQR(User user){

        UserCouponDailyLog userCouponDailyLog = new UserCouponDailyLog();
        userCouponDailyLog.setScanDate(new Date());
        userCouponDailyLog.setUser(user);
        //get coupon type from database---to be done
        userCouponDailyLog.setCouponType(CouponType.DINNER);
        userCouponDailyLogRepo.save(userCouponDailyLog);

        return null;
    }
}
