package cofoo.services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cofoo.dtos.EmployeeDto;
import cofoo.entities.Employee;
import cofoo.repos.EmployeeRepo;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ModelMapper modelMapper;

    public void save(@Valid EmployeeDto employeeDto){
        employeeRepo.save(modelMapper.map(employeeDto,Employee.class));
    }

    public List<EmployeeDto> findAll(){
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        employeeRepo
                .findAll()
                .forEach(
                        employeeEntity -> {
                            employeeDtoList.add(modelMapper.map(employeeEntity,EmployeeDto.class));
                        });
        return employeeDtoList;
    }

    public EmployeeDto find(Long id) {
        return modelMapper.map(employeeRepo.findById(id),EmployeeDto.class);
    }
}
