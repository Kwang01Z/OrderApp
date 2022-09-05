package com.project.quanlyorder.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.PlaybackState;
import android.util.Log;

import com.project.quanlyorder.DTO.FoodDTO;
import com.project.quanlyorder.DTO.SaleDTO;
import com.project.quanlyorder.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    SQLiteDatabase database;

    public SaleDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }
    public boolean addSale(SaleDTO saleDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_SALE_NAME,saleDTO.getName());
        contentValues.put(CreateDatabase.TBL_SALE_VALUESALE,saleDTO.getSalevalue());
        long chk = database.insert(CreateDatabase.TBL_SALE,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean updateSale(int idSale, SaleDTO saleDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_SALE_NAME,saleDTO.getName());
        contentValues.put(CreateDatabase.TBL_SALE_VALUESALE,saleDTO.getSalevalue());
        long chk = database.update(CreateDatabase.TBL_SALE,contentValues,
                CreateDatabase.TBL_SALE_ID+" =? ",
                new String[]{String.valueOf(idSale)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteSale(int idSale){
        long chk = database.delete(CreateDatabase.TBL_SALE,
                CreateDatabase.TBL_SALE_ID+" =? ",
                new String[]{String.valueOf(idSale)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    @SuppressLint("Range")
    public int getSaleValueById(int id){
        int salevalue = 0;
        String query = "SELECT * FROM "+CreateDatabase.TBL_SALE+" WHERE "+CreateDatabase.TBL_SALE_ID+ " =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            salevalue = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_SALE_VALUESALE));
            cursor.moveToNext();
        }
        return salevalue;
    }
    @SuppressLint("Range")
    public SaleDTO getSaleByName(String nameSale){
        SaleDTO saleDTO = new SaleDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_SALE+" WHERE "+CreateDatabase.TBL_SALE_NAME+ " =? ";
        Cursor cursor = database.rawQuery(query,new String[]{nameSale});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            saleDTO.setId( cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_SALE_ID)));
            saleDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_SALE_NAME)));
            saleDTO.setSalevalue(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_SALE_VALUESALE)));
            cursor.moveToNext();
        }
        return saleDTO;
    }
    @SuppressLint("Range")
    public SaleDTO getSaleById(int id){
        SaleDTO saleDTO = new SaleDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_SALE+" WHERE "+CreateDatabase.TBL_SALE_ID+ " =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            saleDTO.setId( cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_SALE_ID)));
            saleDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_SALE_NAME)));
            saleDTO.setSalevalue(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_SALE_VALUESALE)));
            cursor.moveToNext();
        }
        return saleDTO;
    }

    @SuppressLint("Range")
    public List<SaleDTO> getSaleList() {
        List<SaleDTO> saleDTOList = new ArrayList<SaleDTO>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_SALE;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            SaleDTO saleDTO = new SaleDTO();
            saleDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_SALE_ID)));
            saleDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_SALE_NAME)));
            saleDTO.setSalevalue(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_SALE_VALUESALE)));
            saleDTOList.add(saleDTO);
            cursor.moveToNext();
        }
        return saleDTOList;
    }
    @SuppressLint("Range")
    public List<String> getSaleNameList() {
        List<String> saleDTONameList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_SALE;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            saleDTONameList.add(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_SALE_NAME)));
            cursor.moveToNext();
        }
        return saleDTONameList;
    }
    @SuppressLint("Range")
    public int CheckExist(String saleName){
        int idSale = 0;
        String query = "SELECT * FROM " +CreateDatabase.TBL_SALE+ " WHERE "
                +CreateDatabase.TBL_SALE_NAME +" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{saleName});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            idSale = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_SALE_ID));
            cursor.moveToNext();
        }
        return idSale;
    }
}
