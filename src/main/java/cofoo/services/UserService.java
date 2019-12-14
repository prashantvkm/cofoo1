package cofoo.services;

import java.util.Collections;
import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cofoo.dtos.*;
import cofoo.entities.Otp;
import cofoo.entities.Role;
import cofoo.entities.User;
import cofoo.enums.EntityStatus;
import cofoo.enums.RoleName;
import cofoo.exceptions.CodeInvalidOrExpired;
import cofoo.exceptions.DuplicateAccountException;
import cofoo.exceptions.OtpVerificationPending;
import cofoo.exceptions.RecordNotFoundException;
import cofoo.repos.OtpRepo;
import cofoo.repos.RoleRepo;
import cofoo.repos.UserRepo;
import cofoo.utils.EmailSenderUtil;
import cofoo.utils.JwtTokenUtil;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private OtpRepo otpRepo;

    @Autowired
    private EmailSenderUtil emailSenderUtil;

    @Transactional
    public CommonResponseDto register(RegisterDto registerDto){
        if(userRepo.findByEmail(registerDto.getEmail()).isPresent()){
        	User user = userRepo.findByEmail(registerDto.getEmail()).get();
            if(user.getStatus().equals(EntityStatus.pending)){
                throw new OtpVerificationPending();
            } else {
                throw new DuplicateAccountException("Account with Email Id '"+registerDto.getEmail()+"' already exists.");
            }
        }
        User userEntity = modelMapper.map(registerDto, User.class);
        userEntity.setStatus(EntityStatus.pending);
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        if(!roleRepo.findByRoleName(RoleName.ROLE_NORMAL_USER).isPresent()){
            roleRepo.save(new Role(RoleName.ROLE_NORMAL_USER,EntityStatus.active));
        }
        Role role = roleRepo.findByRoleName(RoleName.ROLE_NORMAL_USER)
                .orElseThrow(()-> new RecordNotFoundException());
                //.orElse(roleRepo.save(new Role(RoleName.ROLE_NORMAL_USER,EntityStatus.active)));
        userEntity.setRoles(Collections.singleton(role));
        User result = userRepo.save(userEntity);
        Otp otp = otpRepo.save(Otp.createOtp(result));
        System.out.println(otp.getCode());
//        emailSenderUtil.sendEmail(
//                emailSenderUtil.createEmail(
//                        registerDto.getEmail(),
//                        "OTP for Email verification",
//                        "Please use the code below and verify you email. "+otp.getCode()
//                ));
        return new CommonResponseDto("Registration successful please verify your account using OTP that we have send to your Email",
                EntityStatus.success,
                modelMapper.map(result, RegisterResponseDto.class));
    }

    @Transactional
    public CommonResponseDto login(LoginDto loginDto){
        String userName = loginDto.getEmail();
        String password = loginDto.getPassword();
        if(userRepo.findByEmail(loginDto.getEmail()).isPresent()){
            User user = userRepo.findByEmail(loginDto.getEmail()).get();
            if(user.getStatus().equals(EntityStatus.pending)){
                throw new OtpVerificationPending();
            }
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtTokenUtil.generateToken(authentication);
        return new CommonResponseDto("Login successful",
                EntityStatus.success,
                new LoginResponseDto(loginDto.getEmail(),jwtToken));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email).orElseThrow(
                ()->new UsernameNotFoundException("User not found with Email : " + email)
        );
    }

    @Transactional
    public User loadUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow(
                ()->new UsernameNotFoundException("User not found with id : " + userId));
    }

    @Transactional
    public CommonResponseDto verify(VerifyDto verifyDto) {
        User user = userRepo.findByEmail(verifyDto.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("User not found with Email : " + verifyDto.getEmail()));
        Otp otp = otpRepo.findByCodeAndUserAndExpiredAtAfterAndStatus(verifyDto.getOtp(), user, new Date(), EntityStatus.active)
        .orElseThrow(()-> new CodeInvalidOrExpired("Code you're using either Invalid or Expired, Please try again."));
        User userEntity = otp.getUser();
        userEntity.setStatus(EntityStatus.active);
        return new CommonResponseDto("Your OTP successfully verified, you can now login to our services",
                EntityStatus.success,
                modelMapper.map(userRepo.save(userEntity),UserDto.class));
    }

    public CommonResponseDto reOtp(VerifyDto verifyDto) {
        User user = null;
        if(userRepo.findByEmail(verifyDto.getEmail()).isPresent()){
            user = userRepo.findByEmail(verifyDto.getEmail()).get();
            if(user.getStatus().equals(EntityStatus.active)){
                throw new RuntimeException("User already verified please login to use our services");
            }
        }
        otpRepo.updateOtpStatus();
        Otp otp = otpRepo.save(Otp.createOtp(user));
        System.out.println(otp.getCode());
//        emailSenderUtil.sendEmail(
//                emailSenderUtil.createEmail(
//                        registerDto.getEmail(),
//                        "OTP for Email verification",
//                        "Please use the code below and verify you email. "+otp.getCode()
//                ));
        return new CommonResponseDto("New OTP generate and sent to your email address",EntityStatus.success,null);
    }
}
