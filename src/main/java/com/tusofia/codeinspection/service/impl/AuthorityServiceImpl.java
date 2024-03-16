package com.tusofia.codeinspection.service.impl;

import com.tusofia.codeinspection.repository.AuthorityRepository;
import com.tusofia.codeinspection.service.AuthorityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private AuthorityRepository authorityRepository;

    @Override
    public void deleteAuthority(Long id) {
        authorityRepository.deleteById(id);
    }
}
