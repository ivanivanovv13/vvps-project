package com.tusofia.codeinspection.controller;

import com.tusofia.codeinspection.dto.UpdateUserDto;
import com.tusofia.codeinspection.dto.UserRequest;
import com.tusofia.codeinspection.exception.UserNotFoundException;
import com.tusofia.codeinspection.model.Authority;
import com.tusofia.codeinspection.model.User;
import com.tusofia.codeinspection.service.AuthorityService;
import com.tusofia.codeinspection.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(path = "/admin")
@AllArgsConstructor
public class AdminController {

    private UserService userService;

    private AuthorityService authorityService;

    private ModelMapper modelMapper;

    @GetMapping("/get-all-users")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/get-user/{userEmail}")
    public ResponseEntity<User> getUser(@PathVariable String userEmail) {
        User result = userService.findUser(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return ResponseEntity.ok(result);
    }


    @PatchMapping("/update-user/{userEmail}")
    public ResponseEntity<UpdateUserDto> updateUser(@PathVariable String userEmail, @RequestBody UserRequest userRequest) {
        User user = userService.findUser(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (userRequest.getAuthority() != null) {
            Set<Authority> oldAuthorities = user.getAuthority();
            oldAuthorities.forEach(authority -> authorityService.deleteAuthority(authority.getId()));
        }
        modelMapper.map(userRequest, user);

        UpdateUserDto result = modelMapper.map(userService.updateUser(user), UpdateUserDto.class);

        return ResponseEntity.ok(result);
    }
}
