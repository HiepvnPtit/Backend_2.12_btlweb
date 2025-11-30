package javaSpring.dto.request;

import java.util.List;

public class BorrowSlipCreationRequest {
    private Long readerId;      // ID người mượn
    private List<Long> bookIds; // Danh sách ID sách muốn mượn
    private String note;        // Ghi chú

    // Getters and Setters
    public Long getReaderId() { return readerId; }
    public void setReaderId(Long readerId) { this.readerId = readerId; }
    public List<Long> getBookIds() { return bookIds; }
    public void setBookIds(List<Long> bookIds) { this.bookIds = bookIds; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}