package com.iknowhow.mhte.projectsexperience.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Configuration
public class FeignClientAuthInterceptor implements RequestInterceptor {

    // List of paths that "Authorization" header should be excluded - otherwise it is included by default.
    private static List<String> excludedPaths = List.of();

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        // Exclude the "Authorization" header for specific paths/urls
        if (authorizationHeader != null && !shouldExcludeAuthorizationHeader(template)) {
            template.header("Authorization", authorizationHeader);
        }
    }

    private boolean shouldExcludeAuthorizationHeader(RequestTemplate template) {
        // Add conditional logic to determine if the method should exclude the header
        String methodUrl = template.path();
        return excludedPaths.stream().anyMatch(item -> item.equals(methodUrl));
    }
}