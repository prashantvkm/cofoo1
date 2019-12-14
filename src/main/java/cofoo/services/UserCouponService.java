package cofoo.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cofoo.dtos.CommonResponseDto;
import cofoo.entities.User;
import cofoo.entities.UserCouponDailyLog;
import cofoo.entities.UserMealGroup;
import cofoo.enums.CouponType;
import cofoo.enums.EntityStatus;
import cofoo.repos.UserCouponDailyLogRepo;
import cofoo.repos.UserMealGroupRepo;


@Service
public class UserCouponService {

    @Autowired
    private UserMealGroupRepo userMealGroupRepo;
	
	@Autowired
	private UserCouponDailyLogRepo userCouponDailyLogRepo;
	
	public CommonResponseDto scanQR(User user) throws ParseException {

        UserMealGroup userMealGroup = userMealGroupRepo.findByUser(user);
        if(userMealGroup==null){
            throw new RuntimeException("You are not assigned to any Meal Group");
        }
        Date scanDate = new Date();
        int hour = scanDate.getHours();
//        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
//        Date eight = parser.parse("8:00");
//        Date ten = parser.parse("10:00");
//        Date twele = parser.parse("12:00");
//        Date fifteen = parser.parse("15:00");
//        Date twenty = parser.parse("20:00");
//        Date twentytwo = parser.parse("22:00");

        UserCouponDailyLog userCouponDailyLog = new UserCouponDailyLog();
        userCouponDailyLog.setScanDate(scanDate);
        userCouponDailyLog.setUser(user);

        if (hour >=8 && hour<10) {
            if(userMealGroup.getMealGroup().getBreakfast()){
                userCouponDailyLog.setCouponType(CouponType.BREAKFAST);
            } else {
                throw new RuntimeException("You are not authorized to have Break fast, " +
                        "please contact admin or your manager for more details");
            }
        } else if (hour >=12 && hour<15) {
            if(userMealGroup.getMealGroup().getLunch()){
                userCouponDailyLog.setCouponType(CouponType.LUNCH);
            } else {
                throw new RuntimeException("You are not authorized to have Lunch, " +
                        "please contact admin or your manager for more details");
            }
        } else if (hour >=20 && hour<22) {
            if(userMealGroup.getMealGroup().getDinner()){
                userCouponDailyLog.setCouponType(CouponType.DINNER);
            }else {
                throw new RuntimeException("You are not authorized to have Dinner, " +
                        "please contact admin or your manager for more details");
            }
        } else {
            throw new RuntimeException("You are trying to scan this in out of Breakfast, Lunch and Dinner time," +
                    " Contact you manager and admin to know more.");
        }

        if(userCouponDailyLogRepo.findByUserAndCouponType(user,userCouponDailyLog.getCouponType()).isPresent()){
            throw new RuntimeException("You already used you coupon for "+userCouponDailyLog.getCouponType().name());
        }

        userCouponDailyLogRepo.save(userCouponDailyLog);
        return new CommonResponseDto(
                "Coupon Scanned and redeemed successfully",
                EntityStatus.success,
                null
        );
    }
}
