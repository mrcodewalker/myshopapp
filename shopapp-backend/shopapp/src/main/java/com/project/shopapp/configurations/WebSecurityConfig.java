package com.project.shopapp.configurations;

import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
         http
                 .csrf(AbstractHttpConfigurer::disable)
                 .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                 .authorizeHttpRequests(requests->{
                     requests
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/payments/**",apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)

                             .requestMatchers(
                                     String.format("%s/users/register",apiPrefix),
                                     String.format("%s/users/login",apiPrefix)
                             )
                             .permitAll()

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/users/details",apiPrefix)
                             ).hasAnyRole(Role.ADMIN,Role.USER)

                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/users/update-user**",apiPrefix)).hasAnyRole(Role.ADMIN)

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/users/changes-password/**",apiPrefix))
                             .hasAnyRole(Role.ADMIN,Role.USER)

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/users/details/**",apiPrefix)
                             ).hasAnyRole(Role.ADMIN,Role.USER)

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/roles**",apiPrefix))
                             .permitAll()

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/users**",apiPrefix)).hasRole(Role.ADMIN)

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/users/get-districts/**",apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/users/get-provinces",apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/users/get-communes/**",apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/categories**",apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/categories/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/categories/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/categories/**",apiPrefix)).hasAnyRole(Role.ADMIN)
// ---------------------------------------------------------------------------------------------------------------------------------------------------
                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/comments/create_comment/**",apiPrefix)
                             ).hasAnyRole(Role.ADMIN, Role.USER)

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/comments/get_comments_by_id/**",apiPrefix)
                             ).hasAnyRole(Role.ADMIN, Role.USER)
// ---------------------------------------------------------------------------------------------------------------------------------------------------

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products**",apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/products/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/products/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/products/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products/images/**", apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products/images/detail/*",apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products/**",apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products/by-**",apiPrefix)).permitAll()

// ---------------------------------------------------------------------------------------------------------------------------------------------------
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/orders/**",apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/orders/list/**",apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)

                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.ADMIN)


                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                             .requestMatchers(
                                     HttpMethod.GET,
                                     String.format("%s/orders/get_orders_by_user_id/**", apiPrefix)
                             ).hasAnyRole(Role.ADMIN, Role.USER)

// ---------------------------------------------------------------------------------------------------------------------------------------------------

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.USER)

                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.ADMIN)

// ---------------------------------------------------------------------------------------------------------------------------------------------------

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/coupons/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/coupons/list**",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/coupons/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/coupons/**",apiPrefix)).hasAnyRole(Role.ADMIN)

                             .anyRequest().authenticated();

                 })
                 .csrf(AbstractHttpConfigurer::disable);

             http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
         return http.build();
    }
}
