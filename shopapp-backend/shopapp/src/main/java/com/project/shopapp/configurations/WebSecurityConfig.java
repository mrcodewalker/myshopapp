package com.project.shopapp.configurations;

import com.project.shopapp.dtos.EmailDTO;
import com.project.shopapp.dtos.FacebookDTO;
import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.models.Email;
import com.project.shopapp.models.Role;
import com.project.shopapp.resoponses.EmailResponse;
import com.project.shopapp.services.EmailService;
import com.project.shopapp.services.FacebookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
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
    private final EmailService emailService;
    private final FacebookService facebookService;
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
                                     String.format("%s/users/login",apiPrefix),
                                     String.format("%s/users/login/oauth2**",apiPrefix)
                             )
                             .permitAll()

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/users/details",apiPrefix)
                             ).hasAnyRole(Role.ADMIN,Role.USER)

                             .requestMatchers(String.format("%s/oauth2/login/google", apiPrefix)).permitAll()
                             .requestMatchers(String.format("%s/oauth2/login/facebook", apiPrefix)).permitAll()
                             .requestMatchers(String.format("%s/emails/users/**", apiPrefix)).permitAll()
                             .requestMatchers(String.format("%s/emails/users", apiPrefix)).permitAll()
                             .requestMatchers(String.format("%s/facebooks/users/**", apiPrefix)).permitAll()
                             .requestMatchers(String.format("%s/facebooks/users", apiPrefix)).permitAll()
                             .requestMatchers(String.format("%s/users/login/oauth2/**", apiPrefix)).permitAll()
                             .requestMatchers(String.format("%s/users/phone_number", apiPrefix)).permitAll()
                             .requestMatchers(String.format("%s/users/phone/**", apiPrefix)).permitAll()



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
                                     String.format("%s/users/upload/image",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

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

                             .requestMatchers(
                                     HttpMethod.GET,
                                     String.format("%s/orders/get_delivered_orders/**", apiPrefix)
                             ).permitAll()

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
                 .csrf(AbstractHttpConfigurer::disable)
                  .oauth2Login(Customizer.withDefaults());
        http.cors(Customizer.withDefaults());
//        http.oauth2Login().defaultSuccessUrl("http://localhost:4300/", true);
        http.oauth2Login(oauth2 -> oauth2.successHandler(authenticationSuccessHandler()));
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("Origin", "Authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
         return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            Long id = 0L;
            String type = "";
            if (authentication.getPrincipal() instanceof OidcUser) {
                OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                String name = oidcUser.getFullName(); // Lấy tên người dùng
                String email = oidcUser.getEmail(); // Lấy email người dùng
                String picture = oidcUser.getPicture();
                emailService.createUser(EmailDTO.builder()
                                .email(email)
                                .name(name)
                                .picture(picture)
                        .build());
                // Tiếp tục lấy các thông tin khác nếu cần
                // Sau đó thực hiện lưu thông tin vào cơ sở dữ liệu
                id = this.emailService.getUserByEmail(email).getId();
                type="email";
            } else {
                if (authentication.getPrincipal() instanceof OAuth2User) {
                    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                    String name = oauth2User.getAttribute("name"); // Lấy tên người dùng
                    String email = oauth2User.getAttribute("email"); // Lấy email người dùng
                    String facebookId = oauth2User.getAttribute("id");
                    type = "facebook";

                    facebookService.createUser(FacebookDTO.builder()
                            .facebookId(facebookId)
                            .email(email)
                            .name(name)
                            .build());
                    id = this.facebookService.getFacebookByEmail(email).getId();
                }
            }
            // Thực hiện xử lý sau khi đăng nhập thành công, ví dụ: chuyển hướng
            if (id!=0) {
                response.sendRedirect("http://localhost:4300?id=" + id+"&type="+type);
            } else {
                response.sendRedirect("http://localhost:4300");
            }
        };
    }
}
