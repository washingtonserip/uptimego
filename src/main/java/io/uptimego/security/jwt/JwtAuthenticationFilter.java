package io.uptimego.security.jwt;

import io.uptimego.security.service.UserDetailsServiceImpl;
import io.uptimego.security.utils.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        final String requestURI = req.getRequestURI();

        if (requestURI.contains(SecurityConstants.LOGIN_REQUEST_URI) || requestURI.contains(SecurityConstants.REGISTRATION_REQUEST_URI)) {
            chain.doFilter(req, res);
            return;
        }

        final String header = req.getHeader(SecurityConstants.HEADER_STRING);
        String username = null;
        String authToken = null;
        if (Objects.nonNull(header) && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {

            authToken = header.substring(SecurityConstants.TOKEN_PREFIX.length());
            try {
                username = jwtTokenManager.getEmailFromToken(authToken);
            }
            catch (Exception e) {
                log.error("Authentication Exception : {}", e.getMessage());
            }
        }

        final SecurityContext securityContext = SecurityContextHolder.getContext();

        if (Objects.nonNull(username) && Objects.isNull(securityContext.getAuthentication())) {

            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenManager.validateToken(authToken, userDetails.getUsername())) {

                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                log.info("Authentication successful. Logged in username : {} ", username);
                securityContext.setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }
}
