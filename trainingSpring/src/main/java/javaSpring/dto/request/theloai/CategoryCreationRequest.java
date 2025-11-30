package javaSpring.dto.request.theloai;

import jakarta.validation.constraints.Size;

public class CategoryCreationRequest {

    @Size(min = 1, message = "NAME_INVALID")
    private String categoryName;
    
    private String description;

    // Getter và Setter
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
