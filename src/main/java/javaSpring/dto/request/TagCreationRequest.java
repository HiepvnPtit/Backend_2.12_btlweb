package javaSpring.dto.request;

public class TagCreationRequest {
    private String tagName;
    private String description;

    // Getter và Setter
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
