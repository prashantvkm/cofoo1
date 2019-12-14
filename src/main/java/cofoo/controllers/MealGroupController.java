package cofoo.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import cofoo.dtos.CommonResponseDto;
import cofoo.dtos.GroupDto;
import cofoo.dtos.GroupListDto;
import cofoo.dtos.UserMealGroupDto;
import cofoo.entities.MealGroup;
import cofoo.entities.User;
import cofoo.entities.UserMealGroup;
import cofoo.enums.EntityStatus;
import cofoo.exceptions.RecordNotFoundException;
import cofoo.repos.MealGroupRepo;
import cofoo.repos.UserMealGroupRepo;
import cofoo.repos.UserRepo;

@RestController
@RequestMapping("/group")
//@Secured("ROLE_ADMIN")
public class MealGroupController {

    @Autowired
    private MealGroupRepo mealGroupRepo;

    @Autowired
    private UserMealGroupRepo userMealGroupRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto create(@Valid @RequestBody GroupDto groupDto){
        return new CommonResponseDto(
                "Group successfully created",
                EntityStatus.success,
                modelMapper.map(
                        mealGroupRepo.save(
                                modelMapper.map(groupDto, MealGroup.class)),
                        GroupDto.class
                )
        );
    }

    @PutMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto create(@Valid @RequestBody GroupDto groupDto,@PathVariable Long id){
        MealGroup group = mealGroupRepo.findById(id).orElseThrow(()->new RecordNotFoundException());
        modelMapper.map(groupDto,group);
        return new CommonResponseDto(
                "Group successfully updated",
                EntityStatus.success,
                modelMapper.map(
                        mealGroupRepo.save(group),
                        GroupDto.class
                )
        );
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto list(){
        List<GroupListDto> groups = new ArrayList<>();
        mealGroupRepo.findAll().forEach(group -> {
            groups.add(modelMapper.map(group,GroupListDto.class));
        });
        return new CommonResponseDto(
                "Group List",
                EntityStatus.success,
                groups
        );
    }

    @PostMapping("/setuser")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponseDto create(@Valid @RequestBody UserMealGroupDto userMealGroupDto){
        User user = userRepo.findById(userMealGroupDto.getUserId())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        MealGroup mealGroup = mealGroupRepo.findById(userMealGroupDto.getMealGroupId())
                .orElseThrow(()-> new RecordNotFoundException());
        return new CommonResponseDto(
                "Group successfully created",
                EntityStatus.success,
                modelMapper.map(
                                userMealGroupRepo.save(new UserMealGroup(user,mealGroup)),
                                UserMealGroupDto.class
                        )
                );
    }
}
