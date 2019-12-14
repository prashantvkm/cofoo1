package cofoo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cofoo.annotations.CurrentUser;
import cofoo.dtos.*;
import cofoo.entities.User;
import cofoo.enums.EntityStatus;
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

    @PostMapping("/re_otp")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto reOtp(@Valid @RequestBody VerifyDto verifyDto) {
        return userService.reOtp(verifyDto);
    }

    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto update(@Valid @RequestBody UserDto userDto, @CurrentUser User user){
        return userService.update(userDto, user);
    }

    @GetMapping("/find/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto find(@PathVariable Long id){
        return userService.find(id);
    }

    @PostMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto findByEmail(@Valid @RequestBody UserSearchDto email){
        return userService.findByEmail(email.getEmail());
    }
}
