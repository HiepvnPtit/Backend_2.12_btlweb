package javaSpring.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
// 1. Thêm import này
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 2. QUAN TRỌNG: Thêm dòng này để dùng được @PreAuthorize
public class SecurityConfig {

    private static final String[] PUBLIC_POST_USER_ENDPOINTS = {
            "/api/users", "/authentication/token", "/authentication/introspect",
            "/api/borrowSlips", "/api/reading-history/progress" 
    };

    private static final String[] PUBLIC_GET_USER_ENDPOINTS = {
            "/api/authors", "/api/category", "/api/books",
            "/api/borrowSlips", "/api/ebooks", "/api/ebooks/{bookId}/content",
            "/api/tags", 
            "/api/reading-history",
            "/api/books/author/{authorId}",
            "/api/books/user/{userId}",
            "/api/books/category/{categoryId}/tags",
            "/api/books/searchByTitle",
            "/api/borrowSlips/user/{userId}",
            "/api/borrowSlips/book/{bookId}",
            "/api/borrowSlips/createdAt",
            "/api/reading-history/user/{userId}",
            "/api/reading-history/{bookId}"
    };
 
    // 3. QUAN TRỌNG: Xóa dòng "/api/users/{userId}" khỏi đây
    private static final String[] PUBLIC_PUT_USER_ENDPOINTS = {
            // "/api/users/{userId}",  <-- ĐÃ XÓA (để bắt buộc phải có Token)
            "/api/borrowSlips", 
            "/api/reading-history/progress"
    };

    private static final String[] SWAGGER_ENDPOINTS = {
            "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**",
            "/v2/api-docs", "/webjars/**", "/swagger-resources/**"
    };

    private static final String SIGNER_KEY = "ddaee6f7247285187375aa970cf42d359f3e686ebe2a9a9900bceaee454979cb";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    // 4. Thêm Converter để Spring hiểu ROLE (SCOPE_...) từ Token
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_"); 
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_USER_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_USER_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, PUBLIC_PUT_USER_ENDPOINTS).permitAll()
                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
        );

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> 
                        jwtConfigurer
                            .decoder(jwtDecoder())
                            .jwtAuthenticationConverter(jwtAuthenticationConverter()) // Sử dụng converter
                )
        );

        httpSecurity.csrf().disable();
        return httpSecurity.build();
    }
}