package miu.edu.mpp.app.security;

import com.sun.istack.logging.Logger;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println(">> JwtAuthenticationFilter triggered for URI: {}"+ request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && (authHeader.startsWith("Bearer ") || authHeader.startsWith("Token "))) {
            String token = authHeader.substring(authHeader.indexOf(" ") + 1); // soporta ambos: Bearer / Token

            if (jwtUtil.isTokenValid(token)) {
                Claims claims = jwtUtil.extractAllClaims(token);

                CurrentUser user = new CurrentUser();
                user.setId(claims.get("id", Integer.class).longValue());
                user.setEmail(claims.get("email", String.class));
                user.setUsername(claims.get("username", String.class));

                UserContext.set(user);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                // ✅ Ahora sí: registra en Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear(); // limpieza al final
        }
    }
}
