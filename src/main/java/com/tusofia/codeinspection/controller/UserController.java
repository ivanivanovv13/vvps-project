package com.tusofia.codeinspection.controller;

import com.tusofia.codeinspection.dto.UserDto;
import com.tusofia.codeinspection.service.impl.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserDetailsServiceImpl userDetailsService;

    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userModel) {
        userDetailsService.register(userModel);
        return ResponseEntity.ok("Registration successful!");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userModel, Model model) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid Credentials!");
        }

        model.addAttribute("login", true);
        return ResponseEntity.ok("Logged In!");
    }
}
