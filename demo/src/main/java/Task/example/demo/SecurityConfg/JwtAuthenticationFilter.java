package Task.example.demo.SecurityConfg;

import Task.example.demo.DAO.AppUserRepository;
import Task.example.demo.Entity.AppUser;
import Task.example.demo.Entity.Postions;
import Task.example.demo.Service.PostionsService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final PostionsService postionsService;
    private final AppUserRepository appUserRepository;

    @Autowired
    public JwtAuthenticationFilter(PostionsService postionsService, AppUserRepository appUserRepository) {
        this.postionsService = postionsService;
        this.appUserRepository = appUserRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("JWT Filter Triggered for Request: " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
                DecodedJWT decodedJWT = JWT.require(algorithm)
                        .build()
                        .verify(token);

                String username = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();

                Optional<Postions> manager = postionsService.findByEmail(username);
                if (manager.isPresent()) {
                    Postions user = manager.get();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user.getEmail(), null, AuthorityUtils.createAuthorityList(role));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    Optional<AppUser> existingUser = appUserRepository.findByEmail(username);
                    if (!existingUser.isPresent()) {
                        AppUser newUser = new AppUser();
                        newUser.setEmail(username);
                        newUser.setRole(role);
                        appUserRepository.save(newUser); // Save new user in AppUser table
                    }
                }
            } catch (Exception e) {
                logger.error("Invalid JWT token", e);
            }
        }

        filterChain.doFilter(request, response);
    }

}






