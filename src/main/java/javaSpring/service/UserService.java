package javaSpring.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javaSpring.dto.request.user.UserCreationRequest;
import javaSpring.dto.request.user.UserUpdateRequest;
import javaSpring.entity.User;
import javaSpring.enums.Role;
import javaSpring.exception.AppException;
import javaSpring.exception.ErrorCode;
import javaSpring.repository.UserRepository;

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

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

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
 
}