package com.project.quanlyorder.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.project.quanlyorder.DTO.FoodDTO;
import com.project.quanlyorder.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FoodDAO {
    SQLiteDatabase database;

    public FoodDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }
    public boolean addFood(FoodDTO foodDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_FOOD_NAME,foodDTO.getName());
        contentValues.put(CreateDatabase.TBL_FOOD_IDCATEGORY,foodDTO.getIdCategory());
        contentValues.put(CreateDatabase.TBL_FOOD_PRICE,foodDTO.getPrice());
        contentValues.put(CreateDatabase.TBL_FOOD_IDSALE,foodDTO.getIdSale());
        contentValues.put(CreateDatabase.TBL_FOOD_STATUS,foodDTO.getStatus());
        contentValues.put(CreateDatabase.TBL_FOOD_IMG,foodDTO.getImg());
        long chk = database.insert(CreateDatabase.TBL_FOOD,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean updateFood(int idFood,FoodDTO foodDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_FOOD_NAME,foodDTO.getName());
        contentValues.put(CreateDatabase.TBL_FOOD_IDCATEGORY,foodDTO.getIdCategory());
        contentValues.put(CreateDatabase.TBL_FOOD_PRICE,foodDTO.getPrice());
        contentValues.put(CreateDatabase.TBL_FOOD_IDSALE,foodDTO.getIdSale());
        contentValues.put(CreateDatabase.TBL_FOOD_STATUS,foodDTO.getStatus());
        contentValues.put(CreateDatabase.TBL_FOOD_IMG,foodDTO.getImg());
        long chk = database.update(CreateDatabase.TBL_FOOD,contentValues,
                CreateDatabase.TBL_FOOD_ID+" =? ",
                new String[]{String.valueOf(idFood)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteFood(int idFood){
        long chk = database.delete(CreateDatabase.TBL_FOOD,
                CreateDatabase.TBL_FOOD_ID+" =? ",
                new String[]{String.valueOf(idFood)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    @SuppressLint("Range")
    public List<FoodDTO> getListFoodByIdCategory(int id){
        List<FoodDTO> foodDTOList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_FOOD+" WHERE "+CreateDatabase.TBL_FOOD_IDCATEGORY+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            FoodDTO foodDTO = new FoodDTO();
            foodDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_ID)));
            foodDTO.setIdCategory(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_IDCATEGORY)));
            foodDTO.setIdSale(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_IDSALE)));
            foodDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_NAME)));
            foodDTO.setPrice(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_PRICE)));
            foodDTO.setImg(cursor.getBlob(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_IMG)));
            foodDTO.setStatus(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_STATUS)));
            foodDTOList.add(foodDTO);
            cursor.moveToNext();
        }
        return foodDTOList;
    }
    @SuppressLint("Range")
    public FoodDTO getFoodById(int id){
        FoodDTO foodDTO = new FoodDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_FOOD+" WHERE "+CreateDatabase.TBL_FOOD_ID+" =? ";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            foodDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_ID)));
            foodDTO.setIdCategory(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_IDCATEGORY)));
            foodDTO.setIdSale(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_IDSALE)));
            foodDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_NAME)));
            foodDTO.setPrice(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_PRICE)));
            foodDTO.setImg(cursor.getBlob(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_IMG)));
            foodDTO.setStatus(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_STATUS)));
            cursor.moveToNext();
        }
        return foodDTO;
    }
    @SuppressLint("Range")
    public int CheckExist(String foodName){
        int id = 0;
        String query = "SELECT * FROM " +CreateDatabase.TBL_FOOD+ " WHERE "
                +CreateDatabase.TBL_FOOD_NAME +" = '"+ foodName+"'";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOOD_ID));
            cursor.moveToNext();
        }
        return id;
    }

}
