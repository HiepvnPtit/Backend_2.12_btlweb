package javaSpring.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories") // "categories" = Thể loại", VD: Truyện ngắn, Tiểu thuyết, Văn học,...
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_code", nullable = false, unique = true, length = 50)
    private String categoryCode; // Cũ: maTheLoai

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName; // Cũ: tenTheLoai

    @Column(name = "description", length = 255)
    private String description; // Cũ: moTa

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    // One Category -> Many Books
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Set<Book> books;

    @PrePersist
    public void prePersist() {
        // Auto-generate category code if missing
        if (this.categoryCode == null || this.categoryCode.isBlank()) {
            String randomPart = UUID.randomUUID()
                    .toString()
                    .replace("-", "")       // Remove dashes
                    .substring(0, 8)       // Take first 8 chars
                    .toUpperCase();         // Uppercase

            // Đổi prefix từ "USR-" thành "CAT-" (Category) để tránh trùng với User
            this.categoryCode = "CAT-" + randomPart; 
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) this.createdAt = now;
        if (this.updatedAt == null) this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}