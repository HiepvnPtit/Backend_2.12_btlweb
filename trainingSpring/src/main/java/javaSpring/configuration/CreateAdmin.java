package javaSpring.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javaSpring.entity.User;
import javaSpring.enums.Role;
import javaSpring.repository.UserRepository;

@Configuration
//tạo Admin khi chạy server We are not the same:)
public class CreateAdmin {

    private PasswordEncoder passwordEncoder;

    //constructor
    public CreateAdmin(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {

            //ktra xem có admin chưa
            if (userRepository.findByUsername("admin").isEmpty()){

                //thêm role admin
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                //tạo Admin + set thông tin
                User user = new User();
                user.setUsername("admin");
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode("admin"));

                //lưu thông tin 
                userRepository.save(user);
            }
        };
    }
}
