package lk.ijse.greenshadow.Crop_monitoring_system.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.greenshadow.Crop_monitoring_system.service.JWTService;
import lk.ijse.greenshadow.Crop_monitoring_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
//Request come fist this class
public class JWTConfig extends OncePerRequestFilter{
//
    private final JWTService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String initToken = request.getHeader("Authorization");
        String useEmail;
        String jwToken;

        //Initial Validation
        if(StringUtils.isEmpty(initToken) || !initToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Token received
        jwToken = initToken.substring(7);
        useEmail = jwtService.extractUsername(jwToken);

        //user email validation
        if(StringUtils.isNotEmpty(useEmail) &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            //load user details based on the email
            var loadedUser =
                    userService.userDetailsService().loadUserByUsername(useEmail);
            if(jwtService.isTokenValid(jwToken, loadedUser)) {
                SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetails(request));
                emptyContext.setAuthentication(authToken);
                SecurityContextHolder.setContext(emptyContext);
            }
        }
        filterChain.doFilter(request,response);
    }
}