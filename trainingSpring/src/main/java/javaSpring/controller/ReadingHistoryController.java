package javaSpring.controller;

import javaSpring.dto.request.ReadingSessionRequest;
import javaSpring.dto.response.APIResponse;
import javaSpring.entity.ReadingHistory;
import javaSpring.service.ReadingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reading-history")
public class ReadingHistoryController {
    @Autowired private ReadingHistoryService historyService;

    // Gọi API này khi user lật trang để lưu tiến độ
    @PostMapping("/progress")
    public APIResponse<ReadingHistory> saveProgress(@RequestBody ReadingSessionRequest request) {
        APIResponse<ReadingHistory> response = new APIResponse<>();
        response.setResult(historyService.saveProgress(request));
        return response;
    }

    // Xem lịch sử đọc của mình
    @GetMapping("/user/{userId}")
    public APIResponse<List<ReadingHistory>> getUserHistory(@PathVariable Long userId) {
        APIResponse<List<ReadingHistory>> response = new APIResponse<>();
        response.setResult(historyService.getUserHistory(userId));
        return response;
    }
}