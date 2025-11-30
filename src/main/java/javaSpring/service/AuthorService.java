package javaSpring.service;

import javaSpring.dto.request.AuthorCreationRequest;
import javaSpring.entity.Author;
import javaSpring.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthorService {
    @Autowired 
    private AuthorRepository authorRepository;

    // Tạo tác giả mới
    public Author createAuthor(AuthorCreationRequest request) {
        Author author = new Author();
        author.setAuthorName(request.getAuthorName());
        author.setBiography(request.getBiography());
        return authorRepository.save(author);
    }
    
    // Cập nhật thông tin tác giả theo id
    public Author updateAuthor(Long authorId, AuthorCreationRequest request) {
        Author author = getAuthor(authorId); // Tái sử dụng hàm getAuthor ở dưới để code gọn hơn
        author.setAuthorName(request.getAuthorName());
        author.setBiography(request.getBiography());
        return authorRepository.save(author);
    }

    // Xóa tác giả theo id
    public void deleteAuthor(Long authorId) {
        authorRepository.deleteById(authorId);
    }

    // Lấy thông tin tác giả theo id
    public Author getAuthor(Long AuthorId) {
        return authorRepository.findById(AuthorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    // Lấy danh sách tác giả
    public List<Author> getAllAuthors() { return authorRepository.findAll(); }
}