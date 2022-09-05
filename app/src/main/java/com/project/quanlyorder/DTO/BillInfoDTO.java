package com.project.quanlyorder.DTO;

public class BillInfoDTO {
    int id, idBill, idFood, count;
    String note;

    public BillInfoDTO() {
    }

    public BillInfoDTO(int id, int idBill, int idFood, int count, String note) {
        this.id = id;
        this.idBill = idBill;
        this.idFood = idFood;
        this.count = count;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public int getIdFood() {
        return idFood;
    }

    public void setIdFood(int idFood) {
        this.idFood = idFood;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
