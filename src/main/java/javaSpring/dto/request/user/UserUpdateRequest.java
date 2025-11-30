package javaSpring.dto.request.user;

import java.time.LocalDate;

//lớp dùng để nhận dữ liệu cập nhật thông tin user
public class UserUpdateRequest {
    private String password;
    private LocalDate birthDate; 
    private String email;        
    private String phoneNumber;  
    private String location;     


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}