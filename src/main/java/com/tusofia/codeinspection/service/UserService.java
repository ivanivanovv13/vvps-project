package com.tusofia.codeinspection.service;


import com.tusofia.codeinspection.dto.UserDto;
import com.tusofia.codeinspection.model.Train;
import com.tusofia.codeinspection.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    User register(UserDto userDto);

    Optional<User> findUser(String email);

    User updateUser(User user);

    Page<User> findAll(Pageable pageable);
}
