package com.iknowhow.mhte.projectsexperience.feign;

import com.iknowhow.mhte.projectsexperience.configuration.FeignClientAuthInterceptor;
import com.iknowhow.mhte.projectsexperience.dto.feign.CompanyInfoResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.SearchCompanyInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "${app.feign-names.companies.name}",
        url = "${app.feign-names.companies.url}",
        configuration = FeignClientAuthInterceptor.class)
public interface CompaniesFeignClient {

    @PostMapping("/companies/get-info")
    Page<CompanyInfoResponseDTO> searchCompanyInfo(@RequestBody SearchCompanyInfoDTO dto,
                                                   Pageable pageable);

}
