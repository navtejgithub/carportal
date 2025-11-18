package com.carportal.config;

import com.carportal.entity.User;
import com.carportal.repository.UserRepository;
import com.carportal.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    public JwtFilter(JwtService jwtService,
                     UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    private JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal
            (HttpServletRequest request,
             HttpServletResponse response,
             FilterChain filterChain) throws ServletException, IOException {
        String token=request.getHeader("Authorization");
        if(token!=null && token.startsWith("Bearer ")){
            String jwtToken=token.substring(7,token.length());
//            System.out.println(token);
//            System.out.println(jwtToken);
            String username=jwtService.getUsername(jwtToken);
            System.out.println(username);
            Optional<User> opUser=userRepository.findByUsername(username);
            if(opUser.isPresent()){
                User user=opUser.get();
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(user,null,null);
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        // 🔥 Always call this so controller can execute
        filterChain.doFilter(request, response);

    }
}
