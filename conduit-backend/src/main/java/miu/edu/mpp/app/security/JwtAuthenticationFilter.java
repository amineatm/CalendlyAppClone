package miu.edu.mpp.app.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(6); // "Token " es 6 chars

            if (jwtUtil.isTokenValid(token)) {
                Claims claims = jwtUtil.extractAllClaims(token);

                CurrentUser user = new CurrentUser();
                user.setId(claims.get("id", Integer.class).longValue());
                user.setEmail(claims.get("email", String.class));
                user.setUsername(claims.get("username", String.class));

                UserContext.set(user);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear(); // ✅ Limpieza
        }
    }
}
