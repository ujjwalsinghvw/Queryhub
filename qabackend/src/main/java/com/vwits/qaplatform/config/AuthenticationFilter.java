package com.vwits.qaplatform.config;

import com.vwits.qaplatform.entity.User;
import com.vwits.qaplatform.service.JwtService;
import com.vwits.qaplatform.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractEmail(jwt);
        if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null){
            User user = userService.findByEmail(userEmail);

            if(jwtService.isTokenValid(jwt, user)){


                Date lastRequest = user.getLastRequest();
                Date currentRequest = new Date();

                if (lastRequest != null && isTimeGapMoreThan24Hours(lastRequest, currentRequest)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: Session timed_out, login again!");
                    return;
                }

                user.setLastRequest(currentRequest);
                userService.updateUser(user);

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }

        }
        filterChain.doFilter(request,response);
    }

    private boolean isTimeGapMoreThan24Hours(Date lastRequest, Date currentRequest) {
        long timeDifference = currentRequest.getTime() - lastRequest.getTime();
        long hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifference);
        return hoursDifference >= 24;
    }

}
