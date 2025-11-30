package javaSpring.dto.response;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.Data;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE)

public class AuthenticationResponse{
    String token;

    public AuthenticationResponse() {}

    
    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class Builder {
        private String token;

        public Builder token(String token){
            this.token = token;
            return this;
        }

        public AuthenticationResponse build() {
            return new AuthenticationResponse(token);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
    
    

}



