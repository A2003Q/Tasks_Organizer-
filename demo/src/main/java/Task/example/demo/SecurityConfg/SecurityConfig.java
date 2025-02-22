package Task.example.demo.SecurityConfg;

import Task.example.demo.DAO.AppUserRepository;
import Task.example.demo.Service.PostionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PostionsService postionsService;
    private final AppUserRepository appUserRepository;

    @Autowired
    public SecurityConfig(@Lazy PostionsService postionsService, AppUserRepository appUserRepository) {
        this.postionsService = postionsService;
        this.appUserRepository = appUserRepository;
    }

    // Define JwtAuthenticationFilter as a Bean, passing both dependencies
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(postionsService,appUserRepository);  // Pass both dependencies
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Disable CSRF for stateless authentication
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Disable sessions, since we use JWT
                .and()
                .authorizeHttpRequests()  // Use authorizeHttpRequests instead of authorizeRequests
                .requestMatchers("/api2/login", "/api/signup", "/error", "/error/**")
                .permitAll()  // Allow public access to these endpoints
                .requestMatchers(HttpMethod.POST, "/tasks/create").hasAuthority("MANAGER")
                .requestMatchers(HttpMethod.PUT, "/tasks/{id}").hasAuthority("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/tasks/{id}").hasAuthority("MANAGER")
                .requestMatchers(HttpMethod.GET, "/tasks/employee/**").hasAuthority("EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/tasks").authenticated()
                .requestMatchers("/profile", "/api/messages/send", "/api/messages/inbox").authenticated()
                .anyRequest().authenticated();  // Require authentication for all other endpoints

        // Add the JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



