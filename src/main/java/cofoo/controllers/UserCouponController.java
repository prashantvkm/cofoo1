package cofoo.controllers;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cofoo.annotations.CurrentUser;
import cofoo.dtos.CommonResponseDto;
import cofoo.entities.User;
import cofoo.services.UserCouponService;

@RestController
@RequestMapping("/user_coupon")
public class UserCouponController {

	@Autowired
	private UserCouponService userCouponService;
	
	@GetMapping("/scan")
	@ResponseStatus(HttpStatus.OK)
	public CommonResponseDto scan(@CurrentUser User user) throws ParseException {
		return userCouponService.scanQR(user);
	}
}
