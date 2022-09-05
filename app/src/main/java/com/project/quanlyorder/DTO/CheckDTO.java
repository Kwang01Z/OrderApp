package com.project.quanlyorder.DTO;

public class CheckDTO {
    int idBill, idStaff, total, idSale, paid;
    String dateCheckOut, note;

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public int getIdStaff() {
        return idStaff;
    }

    public void setIdStaff(int idStaff) {
        this.idStaff = idStaff;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getIdSale() {
        return idSale;
    }

    public void setIdSale(int idSale) {
        this.idSale = idSale;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public String getDateCheckOut() {
        return dateCheckOut;
    }

    public void setDateCheckOut(String dateCheckOut) {
        this.dateCheckOut = dateCheckOut;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CheckDTO(int idBill, int idStaff, int total, int idSale, int paid, String dateCheckOut, String note) {
        this.idBill = idBill;
        this.idStaff = idStaff;
        this.total = total;
        this.idSale = idSale;
        this.paid = paid;
        this.dateCheckOut = dateCheckOut;
        this.note = note;
    }

    public CheckDTO() {
    }
}
