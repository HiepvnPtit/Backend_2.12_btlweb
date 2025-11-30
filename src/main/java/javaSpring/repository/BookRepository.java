package javaSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javaSpring.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByBookCode(String bookCode);
}
