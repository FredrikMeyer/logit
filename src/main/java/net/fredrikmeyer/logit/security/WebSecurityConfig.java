package net.fredrikmeyer.logit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/custom.css",
                                        "/webjars/**",
                                        "/favicon.ico"
                                        //"/**"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        //.failureHandler(authenticationFailureHandler())
                        //.successForwardUrl("/")
                        //.failureForwardUrl("/loginfailure")
                        .permitAll())
                .logout(LogoutConfigurer::permitAll)
                .rememberMe(remember -> remember.alwaysRemember(true));

        return http.build();

    }

    @Autowired
    DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
