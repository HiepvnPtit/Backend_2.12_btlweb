package javaSpring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ebook_pages") // trang_sach_dien_tu
//Lưu trữ nội dung/hình ảnh của từng trang sách.

public class EbookPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với Book (sach_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber; // so_trang (thứ tự trang)

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl; // image_url (link ảnh)

    @Column(name = "content_text", columnDefinition = "TEXT")
    private String contentText; // Nếu sách dạng chữ (EPUB/Text) thay vì ảnh

    @Column(name = "width")
    private Integer width; // do_rong

    @Column(name = "height")
    private Integer height; // do_cao

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

}