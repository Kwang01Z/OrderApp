package com.project.quanlyorder.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.project.quanlyorder.DTO.FoodCategoryDTO;
import com.project.quanlyorder.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FoodCategoryDAO {
    SQLiteDatabase database;
    public FoodCategoryDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }
    public boolean addFoodCategory(FoodCategoryDTO foodCategoryDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_FOODCATEGORY_IDSALE,foodCategoryDTO.getIdSale());
        contentValues.put(CreateDatabase.TBL_FOODCATEGORY_NAME,foodCategoryDTO.getName());
        contentValues.put(CreateDatabase.TBL_FOODCATEGORY_IMG,foodCategoryDTO.getImg());
        long chk = database.insert(CreateDatabase.TBL_FOODCATEGORY,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean updateFoodCategory(int idFoodCategory,FoodCategoryDTO foodCategoryDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_FOODCATEGORY_IDSALE,foodCategoryDTO.getIdSale());
        contentValues.put(CreateDatabase.TBL_FOODCATEGORY_NAME,foodCategoryDTO.getName());
        contentValues.put(CreateDatabase.TBL_FOODCATEGORY_IMG,foodCategoryDTO.getImg());
        long chk = database.update(CreateDatabase.TBL_FOODCATEGORY,contentValues,
                CreateDatabase.TBL_FOODCATEGORY_ID+" =? ",
                new String[]{String.valueOf(idFoodCategory)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteFoodCtegory(int idFoodCategory){
        long chk = database.delete(CreateDatabase.TBL_FOODCATEGORY,
                CreateDatabase.TBL_FOODCATEGORY_ID+" =? ",
                new String[]{String.valueOf(idFoodCategory)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("Range")
    public List<FoodCategoryDTO> getCategoryFoodList() {
        List<FoodCategoryDTO> foodCategoryDTOList = new ArrayList<FoodCategoryDTO>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_FOODCATEGORY;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            FoodCategoryDTO foodCategoryDTO = new FoodCategoryDTO();
            foodCategoryDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_ID)));
            foodCategoryDTO.setIdSale(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_IDSALE)));
            foodCategoryDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_NAME)));;
            foodCategoryDTO.setImg(cursor.getBlob(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_IMG)));
            foodCategoryDTOList.add(foodCategoryDTO);
            cursor.moveToNext();
        }
        return foodCategoryDTOList;
    }
    @SuppressLint("Range")
    public FoodCategoryDTO getFoodCategoryById(int idCategory){
        FoodCategoryDTO foodCategoryDTO = new FoodCategoryDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_FOODCATEGORY+" WHERE "+CreateDatabase.TBL_FOODCATEGORY_ID+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(idCategory)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            foodCategoryDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_ID)));
            foodCategoryDTO.setIdSale(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_IDSALE)));
            foodCategoryDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_NAME)));
            foodCategoryDTO.setImg(cursor.getBlob(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_IMG)));
            cursor.moveToNext();
        }
        return foodCategoryDTO;
    }
    @SuppressLint("Range")
    public FoodCategoryDTO getFoodCategoryByName(String nameCategory){
        FoodCategoryDTO foodCategoryDTO = new FoodCategoryDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_FOODCATEGORY+" WHERE "+CreateDatabase.TBL_FOODCATEGORY_NAME+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{nameCategory});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            foodCategoryDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_ID)));
            foodCategoryDTO.setIdSale(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_IDSALE)));
            foodCategoryDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_NAME)));
            foodCategoryDTO.setImg(cursor.getBlob(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_IMG)));
            cursor.moveToNext();
        }
        return foodCategoryDTO;
    }
    @SuppressLint("Range")
    public int CheckExist(String categoryName){
        int idCategory = 0;
        String query = "SELECT * FROM " +CreateDatabase.TBL_FOODCATEGORY+ " WHERE "
                +CreateDatabase.TBL_FOODCATEGORY_NAME +" = '"+ categoryName+"'";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            idCategory = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_ID));
            cursor.moveToNext();
        }
        return idCategory;
    }
    @SuppressLint("Range")
    public List<String> getCategoryNameList() {
        List<String> categoryNameList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_FOODCATEGORY;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String nameCategory = cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_NAME));
            categoryNameList.add(nameCategory);
            cursor.moveToNext();
        }
        return categoryNameList;
    }

    @SuppressLint("Range")
    public int getIdCategoryByName(String nameCategory) {
        int categoryId = 1;
        String query = "SELECT * FROM " +CreateDatabase.TBL_FOODCATEGORY+ " WHERE "
                +CreateDatabase.TBL_FOODCATEGORY_NAME +" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{nameCategory});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            categoryId = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_FOODCATEGORY_ID));
            cursor.moveToNext();
        }
        Log.e("---id:",String.valueOf(categoryId));
        return categoryId;
    }
}
