package cofoo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cofoo.entities.User;
import cofoo.entities.UserMealGroup;

@Repository
public interface UserMealGroupRepo extends JpaRepository<UserMealGroup,Long> {

    public UserMealGroup findByUser(User user);
}
