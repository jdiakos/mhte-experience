package com.iknowhow.mhte.projectsexperience.configuration;

//import com.iknowhow.mhte.usersmanagement.security.AuthEntryPointJwt;
//import com.iknowhow.mhte.usersmanagement.security.JwtFilter;
//import com.iknowhow.mhte.usersmanagement.security.MhteAccessDeniedHandler;
import com.iknowhow.mhte.projectsexperience.security.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
//
//    private final AuthEntryPointJwt authEntryPointJwt;
//    private final MhteAccessDeniedHandler mhteAccessDeniedHandler;
//    private final JwtFilter jwtFilter;
//
//    @Autowired
//    WebSecurityConfig(AuthEntryPointJwt authEntryPointJwt,
//                      MhteAccessDeniedHandler mhteAccessDeniedHandler,
//                      JwtFilter jwtFilter) {
//        this.authEntryPointJwt = authEntryPointJwt;
//        this.mhteAccessDeniedHandler = mhteAccessDeniedHandler;
//        this.jwtFilter = jwtFilter;
//    }
//
    // @TODO -- IMPLEMENT USER PRINCIPAL
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .permitAll()
                )
                .build();
    }

    @Bean
    public AuthTokenFilter authJwtTokenFilter() {

        return new AuthTokenFilter();
    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable()
//                .exceptionHandling()
//                .accessDeniedHandler(mhteAccessDeniedHandler)
//                .authenticationEntryPoint(authEntryPointJwt)
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .requestMatchers("/auth/**").permitAll()
////                .requestMatchers("/po/**").hasAnyAuthority(PoRole.PO_INITIATOR.getKey())
////                .requestMatchers("/stats/po-status/**").authenticated()
////                .requestMatchers("/stats/**").hasAnyAuthority(PoRole.PO_ADMIN.getKey(), PoRole.PO_FINAL_APPROVER.getKey(), PoRole.PO_APPROVER.getKey())
//                .requestMatchers("/**")
//                .permitAll();
//        return http.build();
//    }
}