package javaSpring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    //tạo user mới
    APIResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){
        APIResponse<User> apiResponse = new APIResponse<>();
            apiResponse.setResult(userService.createUser(request));
            return apiResponse;
    }

    @GetMapping
    //lấy danh sách user
    APIResponse<List<User>> getUsers(){
        APIResponse<List<User>> apiResponse = new APIResponse<>();
            apiResponse.setResult(userService.getUsers());
            return apiResponse;
    }

    @GetMapping("/{userId}")
    //lấy thông tin user theo id
    APIResponse<User> getUser(@PathVariable Long userId){
        APIResponse<User> apiResponse = new APIResponse<>();
            apiResponse.setResult(userService.getUser(userId));
            return apiResponse;
    }

    @PutMapping("/{userId}")
    //cập nhật thông tin user theo id
    APIResponse<User> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request){
        APIResponse<User> apiResponse = new APIResponse<>();
            apiResponse.setResult(userService.updateUser(userId, request));
            return apiResponse;
    }

    @DeleteMapping("/{userId}")
    //xóa user theo id
    APIResponse<String> deleteUser(@PathVariable Long userId){
        APIResponse<String> apiResponse = new APIResponse<>();
            apiResponse.setMessage("User has been deleted");
            userService.deleteUser(userId);
            return apiResponse;
    }
}