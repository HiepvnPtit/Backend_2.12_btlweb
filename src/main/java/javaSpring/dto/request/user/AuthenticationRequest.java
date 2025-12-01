package javaSpring.dto.request.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)

//lớp dùng để nhận dữ liệu đăng nhập
public class AuthenticationRequest {
    String username;
    String password;

    public String getUsername() {
      return this.username;
   }

    public String getPassword() {
        return password;
    }

    
}
