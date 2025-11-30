package javaSpring.dto.request.user;

//class request string token để kiểm tra tính hợp lệ của token
public class IntrospectRequest {
    private String token;

    public IntrospectRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
