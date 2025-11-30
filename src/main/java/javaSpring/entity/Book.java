package javaSpring.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_code", nullable = false, unique = true, length = 50)
    private String bookCode;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "publish_year")
    private Integer publishYear;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "isbn", length = 100)
    private String isbn;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ==========================================
    // RELATIONSHIPS
    // ==========================================

    // Many Books -> 1 Category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Many Books <-> Many Authors
    @ManyToMany
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    // Many Books <-> Many Tags
    @ManyToMany
    @JoinTable(
        name = "book_tags",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // One Book -> Many Borrow Slip Details
    @OneToMany(mappedBy = "book")
    @JsonIgnore // Ngăn vòng lặp vô tận khi lấy thông tin sách
    private Set<BorrowSlipDetail> borrowSlipDetails = new HashSet<>();

    // One Book -> Many Ebook Pages
    @OneToMany(mappedBy = "book")
    @JsonIgnore // Ngăn load hàng trăm trang sách khi chỉ xem danh sách book
    private Set<EbookPage> ebookPages = new HashSet<>();
    
    // One Book -> Many Reading Histories (Lịch sử đọc)
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private Set<ReadingHistory> readingHistories = new HashSet<>();

    // ==========================================
    // LIFECYCLE CALLBACKS
    // ==========================================

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Mặc định số lượng khả dụng = tổng số lượng khi tạo mới
        if (availableQuantity == null) {
            availableQuantity = totalQuantity;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<BorrowSlipDetail> getBorrowSlipDetails() {
        return borrowSlipDetails;
    }

    public void setBorrowSlipDetails(Set<BorrowSlipDetail> borrowSlipDetails) {
        this.borrowSlipDetails = borrowSlipDetails;
    }

    public Set<EbookPage> getEbookPages() {
        return ebookPages;
    }

    public void setEbookPages(Set<EbookPage> ebookPages) {
        this.ebookPages = ebookPages;
    }

    public Set<ReadingHistory> getReadingHistories() {
        return readingHistories;
    }

    public void setReadingHistories(Set<ReadingHistory> readingHistories) {
        this.readingHistories = readingHistories;
    }
}