package javaSpring.dto.request;

public class AuthorCreationRequest {
    
    private String authorName;
    private String biography; // tiểu sử

    // Getter và Setter
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

}
