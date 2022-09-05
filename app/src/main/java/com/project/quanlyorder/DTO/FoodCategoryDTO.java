package com.project.quanlyorder.DTO;

public class FoodCategoryDTO {
    int id, idSale;
    String name;
    byte[] img;

    public FoodCategoryDTO() {
    }

    public FoodCategoryDTO(int id, int idSale, String name, byte[] img) {
        this.id = id;
        this.idSale = idSale;
        this.name = name;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getIdSale() {
        return idSale;
    }

    public void setIdSale(int idSale) {
        this.idSale = idSale;
    }
}
