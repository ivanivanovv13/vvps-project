package com.tusofia.codeinspection.service.impl;

import com.tusofia.codeinspection.dto.UserDto;
import com.tusofia.codeinspection.exception.UserAlreadyExistException;
import com.tusofia.codeinspection.model.Train;
import com.tusofia.codeinspection.model.User;
import com.tusofia.codeinspection.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDetailsServiceImpl underTest;

    private User user;

    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "test";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void updateUser_shouldInvoke_repositorySave_once() {
        underTest.updateUser(user);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void register_shouldInvoke_repositorySave_once() {
        underTest.register(createUserDto(EMAIL, PASSWORD));

        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    void register_shouldInvoke_throwUserAlreadyExistException() {
        UserDto userDto = createUserDto(EMAIL, PASSWORD);
        Mockito.when((userRepository.findByEmail(userDto.getEmail()))).thenReturn(Optional.of(user));
        UserAlreadyExistException ex = assertThrows(UserAlreadyExistException.class, () -> underTest.register(userDto));

        assertEquals("User with given email already exist!", ex.getMessage());
    }

    @Test
    void register_shouldReturn_newUser() {
        Mockito.when(userRepository.save(any())).thenReturn(user);
        User createdUser = underTest.register(createUserDto(EMAIL, PASSWORD));

        assertEquals(user, createdUser);
    }

    @Test
    void findUser_shouldReturn_correctUser() {
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        Optional<User> foundUser = underTest.findUser(EMAIL);
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void findTrain_shouldReturn_emptyOptional() {
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        Optional<User> foundUser = underTest.findUser(EMAIL);
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void findAll_shouldReturn_emptyPage() {
        Pageable pageable = PageRequest.of(0, 8);
        Mockito.when(userRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<User> result = underTest.findAll(pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturn_pageWithOneReservation() {
        Pageable pageable = PageRequest.of(0, 1);
        Mockito.when(userRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(user), pageable, 1));

        Page<User> result = underTest.findAll(pageable);

        assertEquals(1,result.getSize());
        assertEquals(1,result.getTotalElements());
        assertEquals(1,result.getTotalPages());
    }

    @Test
    void loadUserByUsername_shouldThrow_UsernameNotFoundException() {
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername(EMAIL));

        assertTrue(ex.getMessage().contains("not found!"));
    }

    @Test
    void loadUserByUsername() {
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails userDetails = underTest.loadUserByUsername(EMAIL);

        assertEquals(EMAIL, userDetails.getUsername());
    }

    private UserDto createUserDto(String email, String password) {
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(password);
        return userDto;
    }
}
