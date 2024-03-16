package com.tusofia.codeinspection.service.impl;

import com.tusofia.codeinspection.dto.UserDto;
import com.tusofia.codeinspection.exception.UserAlreadyExistException;
import com.tusofia.codeinspection.model.Authority;
import com.tusofia.codeinspection.model.User;
import com.tusofia.codeinspection.repository.UserRepository;
import com.tusofia.codeinspection.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService, UserService {
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found!"));
        return new org.springframework.security.core.userdetails.User(foundUser.getEmail(), foundUser.getPassword(), new ArrayList<>());
    }

    @Override
    public User register(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User with given email already exist!");
        }

        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Authority authority = new Authority();
        authority.setAuthority("USER");
        newUser.setAuthority(Set.of(authority));

        return userRepository.save(newUser);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
