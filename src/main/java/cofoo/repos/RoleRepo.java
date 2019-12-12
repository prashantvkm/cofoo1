package cofoo.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cofoo.entities.Role;
import cofoo.enums.RoleName;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {

    public Optional<Role> findByRoleName(RoleName roleName);
}
