package javaSpring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javaSpring.dto.request.BorrowSlipCreationRequest;
import javaSpring.dto.response.APIResponse;
import javaSpring.entity.BorrowSlip;
import javaSpring.entity.BorrowSlipDetail;
import javaSpring.service.BorrowSlipService;

@RestController
@RequestMapping("/api/borrowSlips")
public class BorrowSlipController {

    @Autowired
    private BorrowSlipService borrowSlipService;

    // Tạo phiếu mượn mới
    @PostMapping
    public APIResponse<BorrowSlip> createBorrowSlip(@RequestBody BorrowSlipCreationRequest request) {
        APIResponse<BorrowSlip> response = new APIResponse<>();
        response.setResult(borrowSlipService.createBorrowSlip(request));
        return response;
    }

    // Lấy danh sách tất cả phiếu mượn
    @GetMapping
    public APIResponse<List<BorrowSlip>> getAllBorrowSlips() {
        APIResponse<List<BorrowSlip>> response = new APIResponse<>();
        response.setResult(borrowSlipService.getAllBorrowSlips());
        return response;
    }

    // Lấy chi tiết 1 phiếu mượn
    @GetMapping("/{id}")
    public APIResponse<BorrowSlip> getBorrowSlip(@PathVariable Long id) {
        APIResponse<BorrowSlip> response = new APIResponse<>();
        response.setResult(borrowSlipService.getBorrowSlip(id));
        return response;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @DeleteMapping("/delete/{id}")
    public APIResponse<String> deleteBorrowSlip(@PathVariable Long id) {
        borrowSlipService.deleteBorrowSlip(id);
        
        APIResponse<String> response = new APIResponse<>();
        response.setMessage("Borrow slip deleted successfully (Inventory restored)");
        return response;
    }

    // Xóa tất cả phiếu mượn của 1 User
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @DeleteMapping("/user/{userId}")
    public APIResponse<String> deleteAllSlipsByUser(@PathVariable Long userId) {
        borrowSlipService.deleteAllByUserId(userId);
        
        APIResponse<String> response = new APIResponse<>();
        response.setMessage("All borrow slips for user " + userId + " have been deleted (Inventory restored)");
        return response;
    }

    // Lấy phiếu mượn theo userId
    @GetMapping("/user/{userId}")
    public APIResponse<List<BorrowSlip>> getBorrowSlipsByUser(@PathVariable Long userId) {
        APIResponse<List<BorrowSlip>> response = new APIResponse<>();
        response.setResult(borrowSlipService.getBorrowSlipsByUser(userId));  // Lấy phiếu mượn theo userId từ service
        return response;
    }

    // Lấy phiếu mượn theo bookId
    @GetMapping("/book/{bookId}")
    public APIResponse<BorrowSlip> getBorrowSlipByBook(@PathVariable Long bookId) {
        APIResponse<BorrowSlip> response = new APIResponse<>();
        response.setResult(borrowSlipService.getBorrowSlipByBook(bookId));  // Lấy phiếu mượn theo bookId từ service
        return response;
    }

    // Lấy phiếu mượn theo createdAt (ngày tạo phiếu mượn)
    @GetMapping("/createdAt")
    public APIResponse<List<BorrowSlip>> getBorrowSlipsByCreatedAt(@RequestParam String createdAt) {
        APIResponse<List<BorrowSlip>> response = new APIResponse<>();
        response.setResult(borrowSlipService.getBorrowSlipsByCreatedAt(createdAt));  // Lấy phiếu mượn theo createdAt từ service
        return response;
    }

    // Trả sách (Theo ID của chi tiết phiếu mượn)
    // Ví dụ: PUT /borrow-slips/return/10 (Trả cuốn sách có detailId = 10)
    @PutMapping("/return/{detailId}")
    public APIResponse<BorrowSlipDetail> returnBook(@PathVariable Long detailId) {
        APIResponse<BorrowSlipDetail> response = new APIResponse<>();
        response.setResult(borrowSlipService.returnBook(detailId));
        response.setMessage("Book returned successfully");
        return response;
    }

}
