package javaSpring.controller;

import javaSpring.dto.request.EbookPageRequest;
import javaSpring.dto.response.APIResponse;
import javaSpring.entity.Book;
import javaSpring.entity.EbookPage;
import javaSpring.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ebooks")
public class EbookController {
    @Autowired private EbookService ebookService;

    // Admin: Thêm trang cho sách
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
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
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
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

    // Lấy thông tin tất cả các trang sách
    @GetMapping
    public APIResponse<List<EbookPage>> getAllEbookPages() {
        APIResponse<List<EbookPage>> response = new APIResponse<>();
        response.setResult(ebookService.getAllEbookPages());
        return response;
    }

    // Cập nhật thông tin trang sách
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @PutMapping("/pages/{pageId}")
    public APIResponse<EbookPage> updatePage(@PathVariable Long pageId, @RequestBody EbookPageRequest request) {
        APIResponse<EbookPage> response = new APIResponse<>();
        response.setResult(ebookService.updatePage(pageId, request));
        return response;
    }
}