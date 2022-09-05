package com.project.quanlyorder.DTO;

public class TopFoodDTO {
    int idFood, count;

    public TopFoodDTO(int idFood, int count) {
        this.idFood = idFood;
        this.count = count;
    }

    public TopFoodDTO() {
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
}
