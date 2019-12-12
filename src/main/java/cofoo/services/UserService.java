package cofoo.services;

import java.util.Collections;

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

import cofoo.dtos.LoginDto;
import cofoo.dtos.LoginResponseDto;
import cofoo.dtos.RegisterDto;
import cofoo.dtos.RegisterResponseDto;
import cofoo.entities.Role;
import cofoo.entities.User;
import cofoo.enums.EntityStatus;
import cofoo.enums.RoleName;
import cofoo.exceptions.DuplicateAccountException;
import cofoo.repos.RoleRepo;
import cofoo.repos.UserRepo;
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

    public RegisterResponseDto register(RegisterDto registerDto){
        if(userRepo.findByEmail(registerDto.getEmail()).isPresent()){
            throw new DuplicateAccountException("Account with Email Id '"+registerDto.getEmail()+"' already exists.");
        }
        User userEntity = modelMapper.map(registerDto, User.class);
        userEntity.setStatus(EntityStatus.pending);
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Role role = roleRepo.findByRoleName(RoleName.ROLE_NORMAL_USER)
                .orElse(roleRepo.save(new Role(RoleName.ROLE_NORMAL_USER,EntityStatus.active)));
        userEntity.setRoles(Collections.singleton(role));
        User result = userRepo.save(userEntity);

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
}
