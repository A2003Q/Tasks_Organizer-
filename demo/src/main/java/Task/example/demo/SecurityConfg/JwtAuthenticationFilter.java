package Task.example.demo.SecurityConfg;

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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final PostionsService postionsService;

    @Autowired
    public JwtAuthenticationFilter(PostionsService postionsService) {
        this.postionsService = postionsService;
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("JWT Filter Triggered for Request: " + request.getRequestURI());

        // Get the JWT token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            try {
                // Decode the token using the secret key
                Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
                DecodedJWT decodedJWT = JWT.require(algorithm)
                        .build()
                        .verify(token);

                // Extract claims (for example, the username and role)
                String username = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();

                // Authenticate the user by setting up Spring Security context
                Postions manager = postionsService.findByEmail(username); // Assuming you have such a method

                if (manager != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(manager, null, AuthorityUtils.createAuthorityList(role));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Authenticated user: " + username + " with role: " + role);
                }
            } catch (Exception e) {
                logger.error("Invalid JWT token", e);
            }
        } else {
            logger.warn("Authorization header missing or invalid");
        }

        filterChain.doFilter(request, response);
    }

}




