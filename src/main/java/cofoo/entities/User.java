package cofoo.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cofoo.enums.EntityStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {

    private String name;

    private String password;

    @Column(unique=true)
    private String email;

    private String mobileNo;

    @Setter(AccessLevel.NONE)
    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	if(authorities==null) {
    		authorities = getRoles().stream().map(role ->
            	new SimpleGrantedAuthority(role.getRoleName().name())
    		).collect(Collectors.toList());
    	}
    	return authorities;
    	
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.getStatus().equals(EntityStatus.active);
    }
}
