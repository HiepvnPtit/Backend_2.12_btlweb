package javaSpring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javaSpring.dto.request.TagCreationRequest;
import javaSpring.entity.Tag;
import javaSpring.repository.TagRepository;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    // Tạo tag mới
    public Tag createTag(TagCreationRequest request) {
        Tag tag = new Tag();
        tag.setTagName(request.getTagName());
        tag.setDescription(request.getDescription());
        return tagRepository.save(tag);
    }

    // Lấy danh sách tất cả tag
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    // Xoá tag theo ID
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    // Cập nhật thông tin tag
    public Tag updateTag(Long id, TagCreationRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        tag.setTagName(request.getTagName());
        tag.setDescription(request.getDescription());
        return tagRepository.save(tag);
    }
}