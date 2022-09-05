package com.project.quanlyorder.DTO;

public class UserInfoDTO {
    int idAcc;
    String fullName, gender, birthday, email, phoneNumber, userPosition;

    public UserInfoDTO(int idAcc, String fullName, String gender, String birthday, String email, String phoneNumber, String userPosition) {
        this.idAcc = idAcc;
        this.fullName = fullName;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userPosition = userPosition;
    }

    public UserInfoDTO() {
    }

    public int getIdAcc() {
        return idAcc;
    }

    public void setIdAcc(int idAcc) {
        this.idAcc = idAcc;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }
}
