package javaSpring.dto.request;

import java.time.LocalDate;
import java.util.List;

public class BorrowSlipUpdateRequest {
    private Long readerId;       // Có thể đổi người mượn (như trong ảnh)
    private List<Long> bookIds;  // Danh sách ID sách MỚI (đã chọn trong modal)
    private String note;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    // Getters & Setters
    public Long getReaderId() { return readerId; }
    public void setReaderId(Long readerId) { this.readerId = readerId; }
    public List<Long> getBookIds() { return bookIds; }
    public void setBookIds(List<Long> bookIds) { this.bookIds = bookIds; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}