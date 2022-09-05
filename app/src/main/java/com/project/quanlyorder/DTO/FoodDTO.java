package com.project.quanlyorder.DTO;

public class FoodDTO {
    int id, idCategory, price, idSale, status;
    String name;
    byte[] img;

    public FoodDTO() {
    }

    public FoodDTO(int id, int idCategory, int price, int idSale, int status, String name, byte[] img) {
        this.id = id;
        this.idCategory = idCategory;
        this.price = price;
        this.idSale = idSale;
        this.status = status;
        this.name = name;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIdSale() {
        return idSale;
    }

    public void setIdSale(int idSale) {
        this.idSale = idSale;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
