package cofoo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cofoo.dtos.*;
import cofoo.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponseDto signup(@Valid @RequestBody RegisterDto registerDto){
        return userService.register(registerDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CommonResponseDto login(@Valid @RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CommonResponseDto verify(@Valid @RequestBody VerifyDto verifyDto){
        return userService.verify(verifyDto);
    }

    @PostMapping("re_otp")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto reOtp(@Valid @RequestBody VerifyDto verifyDto) {
        return userService.reOtp(verifyDto);
    }
}
