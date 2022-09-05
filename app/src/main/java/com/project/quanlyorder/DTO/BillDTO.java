package com.project.quanlyorder.DTO;

public class BillDTO {
    int id, idTable, status, summoney;
    String dateCheckIn;

    public BillDTO() {
    }

    public int getSummoney() {
        return summoney;
    }

    public void setSummoney(int summoney) {
        this.summoney = summoney;
    }

    public BillDTO(int id, int idTable, int status, String dateCheckIn, int summoney) {
        this.id = id;
        this.idTable = idTable;
        this.status = status;
        this.dateCheckIn = dateCheckIn;
        this.summoney = summoney;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDateCheckIn() {
        return dateCheckIn;
    }

    public void setDateCheckIn(String dateCheckIn) {
        this.dateCheckIn = dateCheckIn;
    }
}
