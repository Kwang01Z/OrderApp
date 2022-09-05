package com.project.quanlyorder.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.project.quanlyorder.DTO.TableFoodDTO;
import com.project.quanlyorder.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class TableFoodDAO {
    SQLiteDatabase database;

    public TableFoodDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }
    public boolean addTableFood(TableFoodDTO tableFoodDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_TABLEFOOD_IDZONE,tableFoodDTO.getIdZone());
        contentValues.put(CreateDatabase.TBL_TABLEFOOD_NAME,tableFoodDTO.getName());
        contentValues.put(CreateDatabase.TBL_TABLEFOOD_STATUS,tableFoodDTO.getStatus());
        long chk = database.insert(CreateDatabase.TBL_TABLEFOOD,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean updateTableFood(int idTableFood, TableFoodDTO tableFoodDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_TABLEFOOD_IDZONE,tableFoodDTO.getIdZone());
        contentValues.put(CreateDatabase.TBL_TABLEFOOD_NAME,tableFoodDTO.getName());
        contentValues.put(CreateDatabase.TBL_TABLEFOOD_STATUS,tableFoodDTO.getStatus());
        long chk = database.update(CreateDatabase.TBL_TABLEFOOD,contentValues,
                CreateDatabase.TBL_TABLEFOOD_ID+" =? ",
                new String[]{String.valueOf(idTableFood)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteTableFood(int idTableFood){
        long chk = database.delete(CreateDatabase.TBL_TABLEFOOD,
                CreateDatabase.TBL_TABLEFOOD_ID+" =? ",
                new String[]{String.valueOf(idTableFood)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("Range")
    public List<TableFoodDTO> getTableList(int idZone) {
        List<TableFoodDTO> tableFoodDTOList = new ArrayList<TableFoodDTO>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_TABLEFOOD+" WHERE "+CreateDatabase.TBL_TABLEFOOD_IDZONE+" =? ";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(idZone)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            TableFoodDTO tableFoodDTO = new TableFoodDTO();
            tableFoodDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_TABLEFOOD_ID)));
            tableFoodDTO.setIdZone(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_TABLEFOOD_IDZONE)));
            tableFoodDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_TABLEFOOD_NAME)));
            tableFoodDTO.setStatus(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_TABLEFOOD_STATUS)));
            tableFoodDTOList.add(tableFoodDTO);
            cursor.moveToNext();
        }
        return tableFoodDTOList;
    }

    @SuppressLint("Range")
    public TableFoodDTO getTableById(int idTable) {
        TableFoodDTO tableFoodDTO = new TableFoodDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_TABLEFOOD+" WHERE "+CreateDatabase.TBL_TABLEFOOD_ID+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(idTable)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            tableFoodDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_TABLEFOOD_ID)));
            tableFoodDTO.setIdZone(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_TABLEFOOD_IDZONE)));
            tableFoodDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_TABLEFOOD_NAME)));
            tableFoodDTO.setStatus(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_TABLEFOOD_STATUS)));
            cursor.moveToNext();
        }
        return tableFoodDTO;
    }

    @SuppressLint("Range")
    public int CheckExist(String val) {
        int idTable = 0;
        String query = "SELECT * FROM " +CreateDatabase.TBL_TABLEFOOD+ " WHERE "
                +CreateDatabase.TBL_TABLEFOOD_NAME +" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{val});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            idTable = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_TABLEFOOD_ID));
            cursor.moveToNext();
        }
        return idTable;
    }

    public boolean updateStatusTable(int idTable, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_TABLEFOOD_STATUS,i);
        long chk = database.update(CreateDatabase.TBL_TABLEFOOD,contentValues,CreateDatabase.TBL_TABLEFOOD_ID+" =? ",new String[]{String.valueOf(idTable)});
        if(chk != 0){
            return true;
        } else {
            return false;
        }
    }
}
