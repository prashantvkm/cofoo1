package cofoo.services;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cofoo.entities.User;
import cofoo.entities.UserCouponDailyLog;
import cofoo.enums.CouponType;
import cofoo.repos.UserCouponDailyLogRepo;


@Service
public class UserCouponService {
	
	@Autowired
	private UserCouponDailyLogRepo userCouponDailyLogRepo;
	
	public void scanQR(User user){
        System.out.println("This is scan QR repo");
        UserCouponDailyLog userCouponDailyLog = new UserCouponDailyLog();
        userCouponDailyLog.setScanDate(new Date());
        userCouponDailyLog.setUser(user);
        //get coupon type from database---to be done
        userCouponDailyLog.setCouponType(CouponType.DINNER);
        userCouponDailyLogRepo.save(userCouponDailyLog);
        
    }
}
