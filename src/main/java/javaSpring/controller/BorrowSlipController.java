package javaSpring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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