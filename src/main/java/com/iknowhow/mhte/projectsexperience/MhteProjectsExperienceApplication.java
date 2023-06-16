package com.iknowhow.mhte.projectsexperience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MhteProjectsExperienceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MhteProjectsExperienceApplication.class, args);
    }

}
