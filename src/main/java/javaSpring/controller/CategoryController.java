package javaSpring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import javaSpring.dto.request.theloai.CategoryCreationRequest;
import javaSpring.dto.request.theloai.CategoryUpdateRequest;
import javaSpring.dto.response.APIResponse;
import javaSpring.entity.Category;
import javaSpring.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService CategoryService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @PostMapping
    //tạo thể loại mới
    APIResponse<Category> createCategory(@RequestBody @Valid CategoryCreationRequest request){
        APIResponse<Category> apiResponse = new APIResponse<>();
            apiResponse.setResult(CategoryService.createCategory(request));
            return apiResponse;
    }

    @GetMapping
    //lấy danh sách thể loại
    APIResponse<List<Category>> getAllCategories(){
        APIResponse<List<Category>> apiResponse = new APIResponse<>();
            apiResponse.setResult(CategoryService.getAllCategories());
            return apiResponse;
    }

    @GetMapping("/{CategoryId}")
    //lấy thông tin thể loại theo id       
    APIResponse<Category> getCategory(@PathVariable Long CategoryId){
        APIResponse<Category> apiResponse = new APIResponse<>();
            apiResponse.setResult(CategoryService.getCategory(CategoryId));
            return apiResponse;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @PutMapping("/{CategoryId}")
    //cập nhật thông tin thể loại theo id
    APIResponse<Category> updateCategory(@PathVariable Long CategoryId, @RequestBody CategoryUpdateRequest request){
        APIResponse<Category> apiResponse = new APIResponse<>();
            apiResponse.setResult(CategoryService.updateCategory(CategoryId, request));
            return apiResponse;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @DeleteMapping("/{CategoryId}")
    //xóa thể loại theo id
    APIResponse<String> deleteCategory(@PathVariable Long CategoryId){
        APIResponse<String> apiResponse = new APIResponse<>();
            apiResponse.setMessage("Thể loại đã bị xóa");
            CategoryService.deleteCategory(CategoryId);
            return apiResponse;
}
}
