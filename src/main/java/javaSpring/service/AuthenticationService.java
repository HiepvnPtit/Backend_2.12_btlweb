package javaSpring.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    
    //hàm kiểm tra token có hợp lệ không
    public IntrospectResponse introspect(IntrospectRequest request){
        var token = request.getToken();
        
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            //ktra token hết hạn chưa
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            var verified = signedJWT.verify(verifier); // true/false

            IntrospectResponse introspectResponse = new IntrospectResponse(verified && expiryTime.after(new Date()));
            return introspectResponse;
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    //hàm xác thực và trả về token
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
            .token(token)
            .build();
        
    } 

    //hàm để tạo scope giúp Spring phân quyền
    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" "); //tuân theo quy tắc cách nhau bằng dấu cách
        if(!CollectionUtils.isEmpty(user.getRoles())){
            //add từng role vào list và cách nhau bởi dấu cách
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
            .claim("scope", buildScope(user)) //scope trong API dùng để giúp Spring phân quyền
            .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        

        // chữ kí
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }

   

}


