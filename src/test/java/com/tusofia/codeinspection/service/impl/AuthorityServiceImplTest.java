package com.tusofia.codeinspection.service.impl;

import com.tusofia.codeinspection.repository.AuthorityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AuthorityServiceImplTest {

    @Mock
    AuthorityRepository authorityRepository;

    @InjectMocks
    AuthorityServiceImpl underTest;

    private static final Long ID = 1L;

    @Test
     void deleteAuthority_shouldInvoke_repositoryDeleteById_once() {
        underTest.deleteAuthority(ID);

        Mockito.verify(authorityRepository, Mockito.times(1)).deleteById(ID);
    }

}
