package com.tusofia.codeinspection.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

    private String email;

    private String password;

    private Set<AuthorityDto> authority;
}
