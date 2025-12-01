package javaSpring.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        // Các endpoint công khai cho User đăng ký, đăng nhập (POST)
    private static final String[] PUBLIC_POST_USER_ENDPOINTS = {
            "/api/users", "/authentication/token", "/authentication/introspect",
            "/api/borrowSlips", "/api/reading-history/progress"
    };

    // Các endpoint công khai cho User truy cập dữ liệu (GET)
    private static final String[] PUBLIC_GET_USER_ENDPOINTS = {
            "/api/authors", "/api/category", "/api/books",
            "/api/borrowSlips", "/api/ebooks", "/api/ebooks/{bookId}/content",
            "/api/tags", 
            "/api/reading-history"
    };
 
    // Các endpoint công khai cho User sửa dữ liệu (PUT)
    private static final String[] PUBLIC_PUT_USER_ENDPOINTS = {
            "/api/users/{userId}",
            "/api/borrowSlips", "/api/reading-history/progress"
    };

    // Các endpoint Swagger
    private static final String[] SWAGGER_ENDPOINTS = {
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/webjars/**",
            "/swagger-resources/**"
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request
                        // 1. Cho phép các endpoint công khai
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_USER_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_USER_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, PUBLIC_PUT_USER_ENDPOINTS).permitAll()


                        // 2. Cho phép Swagger truy cập mà không cần xác thực
                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()

                        // 3. Cho phép trang lỗi truy cập để tránh lỗi 401 khi không tìm thấy trang
                        .requestMatchers("/error").permitAll()

                        // 4. Các request còn lại phải xác thực
                        .anyRequest().authenticated()
        );

        // Cấu hình xác thực Token JWT
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
        );

        // Tắt CSRF nếu cần cho phát triển
        httpSecurity.csrf().disable();

        return httpSecurity.build();
    }
}
