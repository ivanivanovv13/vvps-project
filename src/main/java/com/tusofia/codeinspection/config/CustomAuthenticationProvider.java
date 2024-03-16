package com.tusofia.codeinspection.config;

import com.tusofia.codeinspection.model.User;
import com.tusofia.codeinspection.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final String INVALID_CREDENTIALS = "Invalid Credentials!";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userRepository.findByEmail(name)
                .orElseThrow(() -> new UsernameNotFoundException("User with email+ " + name + " not found!"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            user.getAuthority().forEach(authority -> authorityList.add(new SimpleGrantedAuthority(authority.getAuthority())));
            return new UsernamePasswordAuthenticationToken(name, password, authorityList);
        } else {
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
