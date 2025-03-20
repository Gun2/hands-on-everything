package com.github.gun2.authapp.security;

import com.github.gun2.authapp.service.AccessTokenBlackListService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AccessTokenUtil accessTokenUtil;
    private final AccessTokenBlackListService accessTokenBlackListService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> tokenOptional = AccessTokenUtil.getTokenFromHeader(request);

        if (tokenOptional.isPresent() && isNotTokenExpired(tokenOptional) && isNotBlackListToken(tokenOptional)) {
            String token = tokenOptional.get();
            String username = accessTokenUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (accessTokenUtil.validateToken(token, username)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * blacklist에 등록된 토큰이 아님
     * @param tokenOptional
     * @return
     */
    private boolean isNotBlackListToken(Optional<String> tokenOptional) {
        return !accessTokenBlackListService.isBlackListToken(tokenOptional.get());
    }

    /**
     * 만료된 토큰이 아님
     * @param tokenOptional
     * @return
     */
    private boolean isNotTokenExpired(Optional<String> tokenOptional) {
        try {
            return !accessTokenUtil.isTokenExpired(tokenOptional.get());
        }catch (ExpiredJwtException e){
            log.error("ExpiredJwtException ", e);
            return false;
        }
    }
}