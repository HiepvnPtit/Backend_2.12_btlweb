package javaSpring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javaSpring.dto.request.TagCreationRequest;
import javaSpring.dto.response.APIResponse;
import javaSpring.entity.Tag;
import javaSpring.service.TagService;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    // Create tag
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @PostMapping
    public APIResponse<Tag> createTag(@RequestBody TagCreationRequest request) {
        APIResponse<Tag> response = new APIResponse<>();
        response.setResult(tagService.createTag(request));
        return response;
    }

    // Get all tags
    @GetMapping
    public APIResponse<List<Tag>> getAllTags() {
        APIResponse<List<Tag>> response = new APIResponse<>();
        response.setResult(tagService.getAllTags());
        return response;
    }

    // Delete tag by ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @DeleteMapping("/{id}")
    public APIResponse<String> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        APIResponse<String> response = new APIResponse<>();
        response.setMessage("Tag deleted successfully");
        return response;
    }

    // Update tag
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") 
    @PutMapping("/{id}")
    public APIResponse<Tag> updateTag(@PathVariable Long id, @RequestBody TagCreationRequest request) {
        APIResponse<Tag> response = new APIResponse<>();
        response.setResult(tagService.updateTag(id, request));
        return response;
    }
}