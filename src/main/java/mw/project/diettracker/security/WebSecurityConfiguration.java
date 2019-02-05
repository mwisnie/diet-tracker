package mw.project.diettracker.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfiguration {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }



    @ConditionalOnProperty("authentication.memory.enabled")
    @Configuration
    public static class InMemorySecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private PasswordEncoder encoder;

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("user").password(encoder.encode("user")).roles("USER")
                    .and()
                    .withUser("admin").password(encoder.encode("admin")).roles("ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/status/**").permitAll();
        }
    }



    @ConditionalOnProperty("authentication.jwt.enabled")
    @Configuration
    public static class JwtConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private PasswordEncoder encoder;

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
        }
    }

}
