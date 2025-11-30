package javaSpring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javaSpring.dto.request.theloai.CategoryCreationRequest;
import javaSpring.dto.request.theloai.CategoryUpdateRequest;
import javaSpring.entity.Category;
import javaSpring.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Tạo thể loại mới
    public Category createCategory(CategoryCreationRequest request) {
        // Kiểm tra tên thể loại tồn tại
        if (categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new RuntimeException("Category already exists");
        }

        Category category = new Category();

        // Mapping dữ liệu từ Request sang Entity
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription()); 

        return categoryRepository.save(category);
    }

    // Cập nhật thông tin thể loại theo id
    public Category updateCategory(Long categoryId, CategoryUpdateRequest request) {
        Category category = getCategory(categoryId); // Tái sử dụng hàm getCategory ở dưới để code gọn hơn

        // Cập nhật thông tin thể loại
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());

        return categoryRepository.save(category);
    }

    // Xóa thể loại theo id
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    // Lấy thông tin thể loại theo id
    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // Lấy danh sách thể loại
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}