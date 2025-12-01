package javaSpring.service;

import javaSpring.dto.request.EbookPageRequest;
import javaSpring.entity.Book;
import javaSpring.entity.EbookPage;
import javaSpring.repository.BookRepository;
import javaSpring.repository.EbookPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {
    @Autowired private EbookPageRepository ebookPageRepository;
    @Autowired private BookRepository bookRepository;

    // Thêm trang cho sách
    public EbookPage addPage(EbookPageRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        EbookPage page = new EbookPage();
            page.setBook(book);
            page.setPageNumber(request.getPageNumber());
            page.setImageUrl(request.getImageUrl());
            page.setContentText(request.getContentText());
            page.setWidth(request.getWidth());
            page.setHeight(request.getHeight());

        return ebookPageRepository.save(page);
    }

    // Lấy nội dung sách để đọc
    public List<EbookPage> getBookContent(Long bookId) {
        return ebookPageRepository.findByBookIdOrderByPageNumberAsc(bookId);
    }

    // Xoá trang sách
    public void deletePage(Long pageId) {
        ebookPageRepository.deleteById(pageId);
    }

    // Lấy thông tin trang sách theo ID
    public EbookPage getPageById(Long pageId) {
        return ebookPageRepository.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Ebook page not found"));
    }

    // Cập nhật thông tin trang sách
    public EbookPage updatePage(Long pageId, EbookPageRequest request) {
        EbookPage page = ebookPageRepository.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Ebook page not found"));

        page.setPageNumber(request.getPageNumber());
        page.setImageUrl(request.getImageUrl());
        page.setContentText(request.getContentText());
        page.setWidth(request.getWidth());
        page.setHeight(request.getHeight());

        return ebookPageRepository.save(page);
    }
}