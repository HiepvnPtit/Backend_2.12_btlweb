package javaSpring.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookCreationRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Book code is required")
    private String bookCode;

    private Integer publishYear;

    @Min(value = 0, message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Total quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer totalQuantity;

    private String isbn;
    private String description;

    // Quan hệ: Chỉ cần gửi ID lên
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Set<Long> authorIds;
    private Set<Long> tagIds;

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Set<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(Set<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public Set<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }
}