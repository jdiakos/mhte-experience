package com.iknowhow.mhte.projectsexperience.dto.feign;

import lombok.Data;

@Data
public class CompanyContactPersonDTO {
	
    private Long id;
    private String firstname;
    private String lastname;
    private String mobilePhone;
    private String landlinePhone;
    private String email;

}
