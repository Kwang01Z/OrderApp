package com.project.quanlyorder.DTO;

public class AccountDTO {
    int id, typeAcc;
    String userName, passWord;

    public AccountDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeAcc() {
        return typeAcc;
    }

    public void setTypeAcc(int typeAcc) {
        this.typeAcc = typeAcc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public AccountDTO(int id, int typeAcc, String userName, String passWord) {
        this.id = id;
        this.typeAcc = typeAcc;
        this.userName = userName;
        this.passWord = passWord;
    }
}
