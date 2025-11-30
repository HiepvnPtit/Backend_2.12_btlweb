package javaSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javaSpring.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByTagName(String tagName);
}