package javaSpring.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import javaSpring.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;    

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    
    // Lấy user đang hoạt động + phân trang
    Page<User> findByStatus(String status, Pageable pageable);
}
