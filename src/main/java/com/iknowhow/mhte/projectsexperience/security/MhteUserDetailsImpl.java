package com.iknowhow.mhte.projectsexperience.security;

import com.iknowhow.mhte.authsecurity.security.MhteUserDetailsService;
import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.authsecurity.security.dtos.MhteUserPrincipalDTO;
import com.iknowhow.mhte.projectsexperience.feign.UsersManagementFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MhteUserDetailsImpl implements MhteUserDetailsService {

    private final UsersManagementFeignClient usersManagementFeignClient;

    @Autowired
    public MhteUserDetailsImpl(UsersManagementFeignClient usersManagementFeignClient) {
        this.usersManagementFeignClient = usersManagementFeignClient;
    }

    @Override
    @Transactional
    public MhteUserPrincipal loadUserByUsername(String username) {
        MhteUserPrincipalDTO user = usersManagementFeignClient.getUserPrincipalInfo();
        Set<SimpleGrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authorityDTO -> new SimpleGrantedAuthority(authorityDTO.getAuthority()))
                .collect(Collectors.toSet());


        return MhteUserPrincipal.builder()
                .taxId(user.getTaxId())
                .username(user.getTaxId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .authorities(grantedAuthorities)
                .build();
    }
}
