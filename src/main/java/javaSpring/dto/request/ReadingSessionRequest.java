package javaSpring.dto.request;

public class ReadingSessionRequest {
    private Long bookId;
    private Long userId; // Có thể lấy từ Token, nhưng tạm thời để đây để test
    private Integer currentPage; // Trang hiện tại đang đọc
    private String deviceInfo;

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}