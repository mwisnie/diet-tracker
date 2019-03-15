package mw.project.diettracker.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mw.project.diettracker.entity.Authority;
import mw.project.diettracker.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    @Getter
    private User user;

    private List<Authority> authorities;

    public UserPrincipal(org.springframework.security.core.userdetails.User springUser) {
        user = User.builder()
                .username(springUser.getUsername())
                .build();

        authorities = springUser.getAuthorities().stream()
                .map(a -> new Authority(springUser.getUsername(), a.getAuthority()))
                .collect(Collectors.toList());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
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

}
