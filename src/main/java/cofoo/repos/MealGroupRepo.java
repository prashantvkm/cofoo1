package cofoo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cofoo.entities.MealGroup;

@Repository
public interface MealGroupRepo extends JpaRepository<MealGroup,Long> {

}
