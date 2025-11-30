package javaSpring.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_code", nullable = false, unique = true, length = 50)
    private String userCode;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "roles", nullable = false, length = 20)
    private Set<String> roles;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "book_quota", nullable = false)
    private Integer bookQuota = 5;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ==========================================
    // RELATIONSHIPS
    // ==========================================

    // 1 Reader -> many Borrow Slips (Danh sách phiếu mượn của độc giả)
    @OneToMany(mappedBy = "reader")
    @JsonIgnore // Ngăn chặn vòng lặp JSON khi API trả về User
    private List<BorrowSlip> borrowSlips = new ArrayList<>();

    // 1 Staff -> many Confirmed Slips (Danh sách phiếu do admin xử lý)
    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<BorrowSlip> confirmedSlips = new ArrayList<>();

    // 1 User -> many Reading Histories (Lịch sử đọc sách online)
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ReadingHistory> readingHistories = new ArrayList<>();

    // ==========================================
    // LIFECYCLE CALLBACKS
    // ==========================================
    @PrePersist
    public void prePersist() {
        if (this.userCode == null || this.userCode.isBlank()) {
            String randomPart = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 8)
                    .toUpperCase();
            this.userCode = "USR-" + randomPart;
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) this.createdAt = now;
        if (this.updatedAt == null) this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBookQuota() {
        return bookQuota;
    }

    public void setBookQuota(Integer bookQuota) {
        this.bookQuota = bookQuota;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<BorrowSlip> getBorrowSlips() {
        return borrowSlips;
    }

    public void setBorrowSlips(List<BorrowSlip> borrowSlips) {
        this.borrowSlips = borrowSlips;
    }

    public List<BorrowSlip> getConfirmedSlips() {
        return confirmedSlips;
    }

    public void setConfirmedSlips(List<BorrowSlip> confirmedSlips) {
        this.confirmedSlips = confirmedSlips;
    }

    public List<ReadingHistory> getReadingHistories() {
        return readingHistories;
    }

    public void setReadingHistories(List<ReadingHistory> readingHistories) {
        this.readingHistories = readingHistories;
    }
}