package com.iknowhow.mhte.projectsexperience.feign;

import com.iknowhow.mhte.authsecurity.security.dtos.MhteUserPrincipalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "users", url = "${app.feign-names.users}")
public interface UsersManagementFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/principal")
    MhteUserPrincipalDTO getUserPrincipalInfo(@RequestHeader("Authorization") String bearerToken);
}