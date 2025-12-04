package javaSpring.configuration;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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

    private static final String[] PUBLIC_PUT_USER_ENDPOINTS = {
            "/api/borrowSlips",
            "/api/reading-history/progress"
    };

    private static final String[] GET_ADMIN_ENDPOINTS = {
            "/api/users/{username}"
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

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    // --- SỬA QUAN TRỌNG: Cấu hình CORS ---
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173", // Frontend 1
                "http://localhost:5174", // Frontend 2 (Cổng Vite của bạn đang chạy ở đây!)
                "https://zestful-celebration-production.up.railway.app",
                "https://hiepvnptitgithubio-production.up.railway.app"
        ));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

    // --- SỬA QUAN TRỌNG: Security Filter Chain ---
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            // 1. Tắt CSRF bằng cú pháp mới (Sửa lỗi biên dịch)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Kích hoạt CORS (Sẽ dùng Bean corsFilter ở trên)
            .cors(Customizer.withDefaults())

            // 3. Phân quyền
            .authorizeHttpRequests(request -> request
                    // Cho phép OPTIONS request (Pre-flight check của trình duyệt) đi qua
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    
                    .requestMatchers(HttpMethod.POST, PUBLIC_POST_USER_ENDPOINTS).permitAll()
                    .requestMatchers(HttpMethod.GET, PUBLIC_GET_USER_ENDPOINTS).permitAll()
                    .requestMatchers(HttpMethod.PUT, PUBLIC_PUT_USER_ENDPOINTS).permitAll()
                    .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                    //Chỉ Admin mới xem được danh sách users
                    .requestMatchers(HttpMethod.GET, GET_ADMIN_ENDPOINTS).hasAuthority("SCOPE_ADMIN")
                    .requestMatchers("/error").permitAll() // Cho phép hiển thị lỗi
                    .anyRequest().authenticated()
            )

            // 4. Cấu hình Resource Server (JWT)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer
                    .decoder(jwtDecoder())
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return httpSecurity.build();
    }
}
