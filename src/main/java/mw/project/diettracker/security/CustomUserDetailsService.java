package mw.project.diettracker.security;

import lombok.RequiredArgsConstructor;
import mw.project.diettracker.entity.Authority;
import mw.project.diettracker.entity.User;
import mw.project.diettracker.repository.AuthorityRepository;
import mw.project.diettracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found."));

        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Authority for username " + username + " not found.")));

        return new UserPrincipal(user, authorities);
    }

}
