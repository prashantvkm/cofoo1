package cofoo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cofoo.entities.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long> {
}
