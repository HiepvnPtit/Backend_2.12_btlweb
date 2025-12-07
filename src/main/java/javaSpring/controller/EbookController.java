package javaSpring.controller;

import javaSpring.dto.request.EbookPageRequest;
import javaSpring.dto.response.APIResponse;
import javaSpring.entity.Book;
import javaSpring.entity.EbookPage;
import javaSpring.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ebooks")
public class EbookController {
    @Autowired private EbookService ebookService;

    // Admin: Thêm trang cho sách
    @PostMapping("/pages")
    public APIResponse<EbookPage> addPage(@RequestBody EbookPageRequest request) {
        APIResponse<EbookPage> response = new APIResponse<>();
        response.setResult(ebookService.addPage(request));
        return response;
    }

    // User: Lấy nội dung sách để đọc (List các trang ảnh/text)
    @GetMapping("/{bookId}/content")
    public APIResponse<List<EbookPage>> getBookContent(@PathVariable Long bookId) {
        APIResponse<List<EbookPage>> response = new APIResponse<>();
        response.setResult(ebookService.getBookContent(bookId));
        return response;
    }

    // Admin: Xoá trang sách
    @DeleteMapping("/pages/{pageId}")
    public APIResponse<Void> deletePage(@PathVariable Long pageId) {
        ebookService.deletePage(pageId);
        return new APIResponse<>();
    }

    // Lấy thông tin trang sách theo ID
    @GetMapping("/pages/{pageId}")
    public APIResponse<EbookPage> getPageById(@PathVariable Long pageId) {
        APIResponse<EbookPage> response = new APIResponse<>();
        response.setResult(ebookService.getPageById(pageId));
        return response;
    }
}