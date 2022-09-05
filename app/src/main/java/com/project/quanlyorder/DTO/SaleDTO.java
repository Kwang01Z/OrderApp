package com.project.quanlyorder.DTO;

public class SaleDTO {
    int id, salevalue;
    String name;

    public SaleDTO(int id, String name, int salevalue) {
        this.id = id;
        this.salevalue = salevalue;
        this.name = name;
    }

    public SaleDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalevalue() {
        return salevalue;
    }

    public void setSalevalue(int salevalue) {
        this.salevalue = salevalue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
