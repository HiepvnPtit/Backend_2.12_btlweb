package javaSpring.configuration;

import javaSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity") // Đặt tên Bean để gọi trong @PreAuthorize
public class UserSecurity {

    @Autowired
    private UserRepository userRepository;

    /**
     * @param authentication: Thông tin người đang đăng nhập (tự động lấy)
     * @param userId: ID của user đang bị sửa (lấy từ URL)
     */
    public boolean hasUserId(Authentication authentication, Long userId) {
        // 1. Lấy username của người đang gọi API (từ Token)
        String currentUsername = authentication.getName();

        // 2. Tìm user trong DB dựa vào ID cần sửa
        return userRepository.findById(userId)
                .map(user -> {
                    // 3. Logic: Cho phép nếu là CHÍNH CHỦ hoặc là ADMIN
                    boolean isOwner = user.getUsername().equals(currentUsername);
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("SCOPE_ADMIN"));
                    
                    return isOwner || isAdmin;
                })
                .orElse(false); // Nếu không tìm thấy user ID -> Chặn
    }
}