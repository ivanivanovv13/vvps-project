package com.tusofia.codeinspection.dto;

import com.tusofia.codeinspection.model.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UpdateUserDto {

    private String email;

    private Set<AuthorityDto> authority;
}
