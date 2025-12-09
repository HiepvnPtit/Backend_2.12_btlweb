package javaSpring.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javaSpring.dto.request.user.UserCreationRequest;
import javaSpring.dto.request.user.UserUpdateRequest;
import javaSpring.entity.User;
import javaSpring.enums.Role;
import javaSpring.exception.AppException;
import javaSpring.exception.ErrorCode;
import javaSpring.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Inject PasswordEncoder (Best practice: Constructor Injection or Autowired)
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public User createUser(UserCreationRequest request){
        // Kiểm tra username tồn tại
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = new User();

        // Mapping dữ liệu từ Request sang Entity
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBirthDate(request.getBirthDate()); 
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setLocation(request.getLocation());

        // Set role mặc định là USER
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    // Xoá user theo ID
public void deleteUser(Long id) {
        // 1. Tìm user trong DB, nếu không thấy thì báo lỗi
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 2. Kiểm tra xem trong danh sách roles có chứa chữ "ADMIN" không
        if (user.getRoles() != null && user.getRoles().contains("ADMIN")) {
            throw new AppException(ErrorCode.CANNOT_DELETE_ADMIN);
        }

        // 3. Nếu không phải Admin thì xóa
        userRepository.deleteById(id);
    }

    // Lấy tất cả user
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    // Lấy user theo ID
    public User getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Lấy user theo username
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Cập nhật thông tin user
    public User updateUser(Long id, UserUpdateRequest request){
        User user = getUser(id);

        // Mapping dữ liệu update
        user.setBirthDate(request.getBirthDate()); // Cũ: setDate
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setLocation(request.getLocation());

        // Kiểm tra nếu có password mới thì mới encode và set lại
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(user);
    }
    
    // Trao quyền admin cho user
    public User addAdmin(Long id) {
        User user = getUser(id);
        Set<String> roles = new HashSet<>();
        roles.add(Role.ADMIN.name());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    // Trảm quyền admin
    public User removeAdmin(Long id) {
        User user = getUser(id);
        Set<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);
        return userRepository.save(user);
    }

     // API lấy 10 user mới nhất
    public List<User> getTop10NewestUsers() {
        // Lấy 10 người, sắp xếp theo ngày tạo mới nhất (hoặc id)
        Pageable limit = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        
        // Chỉ lấy user đang ACTIVE
        return userRepository.findByStatus("ACTIVE", limit).getContent();
    }
}
