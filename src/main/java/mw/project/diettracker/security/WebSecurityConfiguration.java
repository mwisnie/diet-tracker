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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfiguration {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler restFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }



    @ConditionalOnProperty("authentication.type.cookie.enabled")
    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public static class CookieSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private PasswordEncoder encoder;

        @Autowired
        private AuthenticationEntryPoint restAuthenticationEntryPoint;

        @Autowired
        private AuthenticationSuccessHandler restAuthenticationSuccessHandler;

        @Autowired
        private AuthenticationFailureHandler restFailureHandler;

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("user").password(encoder.encode("user")).roles("USER")
                    .and()
                    .withUser("admin").password(encoder.encode("admin")).roles("ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/manage/**").permitAll()
                .and()
                .formLogin()
                    .successHandler(restAuthenticationSuccessHandler)
                    .failureHandler(restFailureHandler)
                .and()
                    .logout();
        }
    }




    @ConditionalOnProperty("authentication.type.jwt.enabled")
    @Configuration
    public static class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {

        //TODO - jwt security

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
