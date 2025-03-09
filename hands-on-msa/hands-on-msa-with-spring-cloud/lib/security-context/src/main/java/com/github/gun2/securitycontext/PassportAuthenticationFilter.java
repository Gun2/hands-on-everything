package com.github.gun2.securitycontext;

import com.github.gun2.securitymodule.ClaimNames;
import com.github.gun2.securitymodule.PassportUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PassportAuthenticationFilter extends OncePerRequestFilter {

    private final PassportUtil passportUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> tokenOptional = PassportUtil.getTokenFromHeader(request);

        if (tokenOptional.isPresent() && isNotTokenExpired(tokenOptional)) {
            String token = tokenOptional.get();
            String username = passportUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (passportUtil.validateToken(token, username)) {
                    String role = passportUtil.extract(token, ClaimNames.ROLE);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, token, List.of(new SimpleGrantedAuthority("ROLE_"+role)));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }


    /**
     * 만료된 토큰이 아님
     * @param tokenOptional
     * @return
     */
    private boolean isNotTokenExpired(Optional<String> tokenOptional) {
        return !passportUtil.isTokenExpired(tokenOptional.get());
    }
}