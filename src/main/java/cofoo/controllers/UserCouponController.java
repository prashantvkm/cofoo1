package cofoo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cofoo.annotations.CurrentUser;
import cofoo.entities.User;
import cofoo.services.UserCouponService;

@RestController
@RequestMapping("/user_coupon")
public class UserCouponController {
	private UserCouponService userCouponService;
	
	@GetMapping("/scan_qr")
	public String scanQR(@CurrentUser User user) {
		System.out.println("Going to hit service for QR code");
		userCouponService.scanQR(user);	
		return "Coupon Scanned";
	}
}
