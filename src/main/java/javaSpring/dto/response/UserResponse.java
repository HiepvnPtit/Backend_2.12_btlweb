package javaSpring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String userCode;
    private String username;
    private String email;
    private String phoneNumber;
    private String location;
    private LocalDate birthDate;
    private List<String> roles;
    private Integer bookQuota;
    private String status;
    private LocalDate createdAt;
}