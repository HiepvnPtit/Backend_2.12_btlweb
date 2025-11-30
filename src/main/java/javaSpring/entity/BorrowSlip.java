package javaSpring.entity;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;


@Entity
@Table(name = "borrow_slips") // phieu_muon

public class BorrowSlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slip_code", unique = true, length = 50)
    private String slipCode; // ma_phieu

    // Người mượn (doc_gia_id)
    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private User reader; 

    // Nhân viên tạo phiếu (nhan_vien_id)
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff; //admin

    @Column(name = "status", length = 20)
    private String status; // trang_thai (PENDING, BORROWED, RETURNED...)

    @Column(name = "note", columnDefinition = "TEXT")
    private String note; // ghi_chu

    @Column(name = "created_at")
    private LocalDateTime createdAt; // ngay_lap

    // 1 Phiếu mượn có nhiều chi tiết
    @OneToMany(mappedBy = "borrowSlip", cascade = CascadeType.ALL)
    private List<BorrowSlipDetail> details;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlipCode() {
        return slipCode;
    }

    public void setSlipCode(String slipCode) {
        this.slipCode = slipCode;
    }

    public User getReader() {
        return reader;
    }

    public void setReader(User reader) {
        this.reader = reader;
    }

    public User getStaff() {
        return staff;
    }

    public void setStaff(User staff) {
        this.staff = staff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<BorrowSlipDetail> getDetails() {
        return details;
    }

    public void setDetails(List<BorrowSlipDetail> details) {
        this.details = details;
    }

}