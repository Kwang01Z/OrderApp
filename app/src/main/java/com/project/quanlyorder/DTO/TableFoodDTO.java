package com.project.quanlyorder.DTO;

public class TableFoodDTO {
    int id, idZone, status;
    String name;
    boolean picked;

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public TableFoodDTO() {
    }

    public TableFoodDTO(int id, int idZone, int status, String name) {
        this.id = id;
        this.idZone = idZone;
        this.status = status;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdZone() {
        return idZone;
    }

    public void setIdZone(int idZone) {
        this.idZone = idZone;
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
}
