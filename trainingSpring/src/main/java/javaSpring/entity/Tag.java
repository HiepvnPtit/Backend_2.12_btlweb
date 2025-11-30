package javaSpring.entity;

import java.util.Set;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "tags") // tag
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name", nullable = false, length = 100)
    private String tagName; // ten_tag

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // mo_ta

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Book> books;

    //constructor
    public Tag() {      
    }

    public Tag(String tagName, String description) {
        this.tagName = tagName;
        this.description = description;
    }

    //getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

}