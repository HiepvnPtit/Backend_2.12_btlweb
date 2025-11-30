package javaSpring.entity;

import java.util.Set;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "authors") // tac_gia

public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_name", nullable = false, length = 100)
    private String authorName; // ten_tac_gia

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography; // tieu_su

    // Quan hệ Many-to-Many với Book (mappedBy bên Book)
    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private Set<Book> books;

    //constructor
    public Author() {
    }

    public Author(String authorName, String biography, Set<Book> books, Long id) {
        this.authorName = authorName;
        this.biography = biography;
        this.books = books;
        this.id = id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

}