package cofoo.services;

import java.util.Collections;
import java.util.Date;

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

import cofoo.annotations.CurrentUser;
import cofoo.dtos.*;
import cofoo.entities.Otp;
import cofoo.entities.Role;
import cofoo.entities.User;
import cofoo.enums.EntityStatus;
import cofoo.enums.RoleName;
import cofoo.exceptions.CodeInvalidOrExpired;
import cofoo.exceptions.DuplicateAccountException;
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

    public RegisterResponseDto register(RegisterDto registerDto){
        if(userRepo.findByEmail(registerDto.getEmail()).isPresent()){
            throw new DuplicateAccountException("Account with Email Id '"+registerDto.getEmail()+"' already exists.");
        }
        User userEntity = modelMapper.map(registerDto, User.class);
        userEntity.setStatus(EntityStatus.pending);
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Role role = roleRepo.findByRoleName(RoleName.ROLE_NORMAL_USER)
                .orElseThrow(()-> new RecordNotFoundException());
                //.orElse(roleRepo.save(new Role(RoleName.ROLE_NORMAL_USER,EntityStatus.active)));
        userEntity.setRoles(Collections.singleton(role));
        User result = userRepo.save(userEntity);
        Otp otp = otpRepo.save(Otp.createOtp(result));
        emailSenderUtil.sendEmail(
                emailSenderUtil.createEmail(
                        registerDto.getEmail(),
                        "OTP for Email verification",
                        "Please use the code below and verify you email. "+otp.getCode()
                ));
        return modelMapper.map(result, RegisterResponseDto.class);
    }

    public LoginResponseDto login(LoginDto loginDto){
        String userName = loginDto.getEmail();
        String password = loginDto.getPassword();
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtTokenUtil.generateToken(authentication);
        return new LoginResponseDto(loginDto.getEmail(),jwtToken);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email).orElseThrow(
                ()->new UsernameNotFoundException("User not found with Email : " + email)
        );
    }

    public User loadUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow(
                ()->new UsernameNotFoundException("User not found with id : " + userId));
    }

    public UserDto verify(VerifyDto verifyDto) {
        User user = userRepo.findByEmail(verifyDto.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("User not found with Email : " + verifyDto.getEmail()));
        Otp otp = otpRepo.findByCodeAndUserAndExpiredAtAfterAndStatus(verifyDto.getOtp(), user, new Date(), EntityStatus.active)
        .orElseThrow(()-> new CodeInvalidOrExpired("Code you're using either Invalid or Expired, Please try again."));
        User userEntity = otp.getUser();
        userEntity.setStatus(EntityStatus.active);
        return modelMapper.map(userRepo.save(userEntity),UserDto.class);
    }
}
