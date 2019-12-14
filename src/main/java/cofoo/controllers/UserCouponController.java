package cofoo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cofoo.annotations.CurrentUser;
import cofoo.dtos.CommonResponseDto;
import cofoo.entities.User;
import cofoo.services.UserCouponService;

@RestController
@RequestMapping("/user_coupon")
public class UserCouponController {
	private UserCouponService userCouponService;
	
	@GetMapping("/scan_qr")
	public CommonResponseDto scanQR(@CurrentUser User user) {
		return userCouponService.scanQR(user);
	}
}
