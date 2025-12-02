package javaSpring.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringJoiner;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javaSpring.dto.request.user.AuthenticationRequest;
import javaSpring.dto.request.user.IntrospectRequest;
import javaSpring.dto.response.AuthenticationResponse;
import javaSpring.dto.response.IntrospectResponse;
import javaSpring.dto.response.UserResponse; 
import javaSpring.entity.User;
import javaSpring.exception.AppException;
import javaSpring.exception.ErrorCode;
import javaSpring.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.Generated;

@Service
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private final UserRepository userRepository;
    
    protected static final String SIGNER_KEY = "ddaee6f7247285187375aa970cf42d359f3e686ebe2a9a9900bceaee454979cb";

    @Generated
    public AuthenticationService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Kiểm tra token
    public IntrospectResponse introspect(IntrospectRequest request){
        var token = request.getToken();
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);
            return new IntrospectResponse(verified && expiryTime.after(new Date()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    // --- XÁC THỰC VÀ TRẢ VỀ TOKEN + FULL USER INFO ---
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        // 1. Tìm user trong DB
        var user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 2. So khớp mật khẩu
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // 3. Tạo Token
        var token = generateToken(user);

        // 4. Tạo UserResponse (ĐÃ CẬP NHẬT ĐẦY ĐỦ CÁC TRƯỜNG)
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .userCode(user.getUserCode())
                .username(user.getUsername())
                .email(user.getEmail())
                
                // --- SỬA LỖI TẠI ĐÂY ---
                // Chuyển Set sang List bằng new ArrayList<>(...)
                .roles(new ArrayList<>(user.getRoles())) 
                // -----------------------
                
                .phoneNumber(user.getPhoneNumber())
                .location(user.getLocation())
                .birthDate(user.getBirthDate())
                .bookQuota(user.getBookQuota())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toLocalDate() : null)
                .build();

        // 5. Trả về kết quả gồm Token và User Info
        return AuthenticationResponse.builder()
            .token(token)
            .authenticated(true)
            .user(userResponse) 
            .build();
    } 

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" "); 
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(s -> stringJoiner.add(s));
        }
        return stringJoiner.toString();
    }

    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(user.getUsername())
            .issuer("mlongkk.com")
            .issueTime(new Date())
            .expirationTime(new Date(
                Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())
            )
            .claim("scope", buildScope(user)) 
            .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}