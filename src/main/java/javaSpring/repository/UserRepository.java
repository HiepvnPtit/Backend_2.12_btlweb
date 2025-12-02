package javaSpring.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import javaSpring.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    
}
