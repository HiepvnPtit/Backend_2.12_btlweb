package javaSpring.entity;

import java.time.LocalDate;
import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "borrow_slip_details") // chi_tiet_phieu_muon

public class BorrowSlipDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "borrow_slip_id", nullable = false)
    private BorrowSlip borrowSlip;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "borrow_date")
    private LocalDate borrowDate; // ngay_muon

    @Column(name = "due_date")
    private LocalDate dueDate; // ngay_den_han

    @Column(name = "return_date")
    private LocalDate returnDate; // ngay_tra

    @Column(name = "status", length = 20)
    private String status; // trang_thai

    @Column(name = "fine_amount", precision = 12, scale = 2)
    private BigDecimal fineAmount; // tien_phat

    @Column(name = "fine_reason")
    private String fineReason; // ly_do_phat

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BorrowSlip getBorrowSlip() {
        return borrowSlip;
    }

    public void setBorrowSlip(BorrowSlip borrowSlip) {
        this.borrowSlip = borrowSlip;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getFineReason() {
        return fineReason;
    }

    public void setFineReason(String fineReason) {
        this.fineReason = fineReason;
    }

}