package cofoo.entities;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class MealGroup extends BaseEntity{
    private String name;
    private Boolean lunch;
    private Boolean breakfast;
    private Boolean dinner;
}
