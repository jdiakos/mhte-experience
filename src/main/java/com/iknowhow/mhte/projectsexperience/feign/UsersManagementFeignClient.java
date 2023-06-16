package com.iknowhow.mhte.projectsexperience.feign;

import com.iknowhow.mhte.authsecurity.security.dtos.MhteUserPrincipalDTO;
import com.iknowhow.mhte.projectsexperience.configuration.FeignClientAuthInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "${app.feign-names.users.name}", url = "${app.feign-names.users.url}", configuration = FeignClientAuthInterceptor.class)
public interface UsersManagementFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/principal")
    MhteUserPrincipalDTO getUserPrincipalInfo();
}