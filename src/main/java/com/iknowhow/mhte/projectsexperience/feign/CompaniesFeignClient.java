package com.iknowhow.mhte.projectsexperience.feign;

import com.iknowhow.mhte.projectsexperience.configuration.FeignClientAuthInterceptor;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "${app.feign-names.companies.name}",
        url = "${app.feign-names.companies.url}",
        configuration = FeignClientAuthInterceptor.class)
public interface CompaniesFeignClient {


}
