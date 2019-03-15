package mw.project.diettracker.security;

import lombok.RequiredArgsConstructor;
import mw.project.diettracker.security.filter.CredentialsAuthenticationFilter;
import mw.project.diettracker.security.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfiguration {

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler restFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



    @ConditionalOnProperty("authentication.type.cookie.enabled")
    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public static class CookieSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private final PasswordEncoder encoder;

        @Autowired
        private final AuthenticationEntryPoint restAuthenticationEntryPoint;

        @Autowired
        private final AuthenticationSuccessHandler restAuthenticationSuccessHandler;

        @Autowired
        private final AuthenticationFailureHandler restAuthenticationFailureHandler;

        @Autowired
        private final LogoutSuccessHandler logoutSuccessHandler;

        @Autowired
        private final UserDetailsService customUserDetailsService;

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            if (WebSecurityConstants.CUSTOM_DAO_AUTHENTICATION_ENABLED) {
                DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
                authenticationProvider.setPasswordEncoder(encoder);
                authenticationProvider.setUserDetailsService(customUserDetailsService);
                auth.authenticationProvider(authenticationProvider);

            } else if (WebSecurityConstants.IN_MEMORY_AUTHENTICATION_ENABLED) {
                auth.inMemoryAuthentication()
                        .withUser("user").password(encoder.encode("user")).roles("USER")
                        .and()
                        .withUser("admin").password(encoder.encode("admin")).roles("ADMIN");
            } else {
                throw new RuntimeException("Could not establish authentication provider - bad configuration");
            }
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors()
                .and()
                .csrf()
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
                    .failureHandler(restAuthenticationFailureHandler)
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessHandler(logoutSuccessHandler);
        }
    }




    @ConditionalOnProperty("authentication.type.jwt.enabled")
    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public static class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private final PasswordEncoder encoder;

        @Autowired
        private final AuthenticationEntryPoint restAuthenticationEntryPoint;

        @Autowired
        private final UserDetailsService customUserDetailsService;

        @Bean
        public AuthenticationManager authenticationManager() throws Exception {
            return super.authenticationManager();
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            if (WebSecurityConstants.CUSTOM_DAO_AUTHENTICATION_ENABLED) {
                DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
                authenticationProvider.setPasswordEncoder(encoder);
                authenticationProvider.setUserDetailsService(customUserDetailsService);
                auth.authenticationProvider(authenticationProvider);

            } else if (WebSecurityConstants.IN_MEMORY_AUTHENTICATION_ENABLED) {
                auth.inMemoryAuthentication()
                        .withUser("user").password(encoder.encode("user")).roles("USER")
                        .and()
                        .withUser("admin").password(encoder.encode("admin")).roles("ADMIN");
            } else {
                throw new RuntimeException("Could not establish authentication provider - bad configuration");
            }
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors()
                .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/manage/**").permitAll()
                    .antMatchers("/login").permitAll()
                .and()
                    .addFilter(new CredentialsAuthenticationFilter(authenticationManager()))
                    .addFilter(new TokenAuthenticationFilter(authenticationManager()))
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout()
                    .disable();
        }
    }

}
