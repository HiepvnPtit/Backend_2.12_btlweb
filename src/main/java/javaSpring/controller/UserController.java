package javaSpring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// 1. Import PreAuthorize
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import javaSpring.dto.request.user.UserCreationRequest;
import javaSpring.dto.request.user.UserUpdateRequest;
import javaSpring.dto.response.APIResponse;
import javaSpring.entity.User;
import javaSpring.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    // Tạo user mới (Đăng ký): Thường là public (đã config trong SecurityConfig)
    APIResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){
        APIResponse<User> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    // Lấy danh sách user: CHỈ ADMIN MỚI ĐƯỢC XEM
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    APIResponse<List<User>> getUsers(){
        APIResponse<List<User>> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.getUsers());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    // Lấy thông tin user: CHÍNH CHỦ HOẶC ADMIN
    @PreAuthorize("@userSecurity.hasUserId(authentication, #userId)")
    APIResponse<User> getUser(@PathVariable Long userId){
        APIResponse<User> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.getUser(userId));
        return apiResponse;
    }

    @GetMapping("/myInfo")
    // API phụ: Lấy thông tin của chính người đang đăng nhập (Tiện lợi cho Frontend)
    APIResponse<User> getMyInfo(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        APIResponse<User> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.getUserByUsername(username));
        return apiResponse;
    }

    //tìm user theo username
    @GetMapping("/username/{username}")
    APIResponse<User> getUserByUsername(@PathVariable String username){
        APIResponse<User> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.getUserByUsername(username));
        return apiResponse;
    }

    @PutMapping("/{userId}")
    // Cập nhật: CHÍNH CHỦ HOẶC ADMIN MỚI ĐƯỢC SỬA
    // @userSecurity: Gọi đến bean UserSecurity đã tạo
    // authentication: Tự động lấy user đang đăng nhập
    // #userId: Lấy tham số userId từ URL
    @PreAuthorize("@userSecurity.hasUserId(authentication, #userId)")
    APIResponse<User> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request){
        APIResponse<User> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.updateUser(userId, request));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    // Xóa user: CHỈ ADMIN MỚI ĐƯỢC XÓA
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    APIResponse<String> deleteUser(@PathVariable Long userId){
        APIResponse<String> apiResponse = new APIResponse<>();
        apiResponse.setMessage("User has been deleted");
        userService.deleteUser(userId);
        return apiResponse;
    }

    // Trao quyền admin cho user
    @PutMapping("/addAdmin/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    APIResponse<User> addAdmin(@PathVariable Long userId){
        APIResponse<User> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.addAdmin(userId));
        return apiResponse;
    }

    // Trảm quyền admin
    @PutMapping("/removeAdmin/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    APIResponse<User> removeAdmin(@PathVariable Long userId){
        APIResponse<User> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.removeAdmin(userId));
        return apiResponse;
    }
}
