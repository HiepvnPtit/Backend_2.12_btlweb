package javaSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import javaSpring.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByAuthorName(String authorName);
    List<Author> findByAuthorNameContainingIgnoreCase(String authorName);
}
