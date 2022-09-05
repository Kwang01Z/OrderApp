package com.project.quanlyorder.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.project.quanlyorder.DTO.BillDTO;
import com.project.quanlyorder.DTO.BillInfoDTO;
import com.project.quanlyorder.DTO.BillInfoDTO;
import com.project.quanlyorder.DTO.TopFoodDTO;
import com.project.quanlyorder.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class BillInfoDAO {
    SQLiteDatabase database;

    public BillInfoDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }
    public boolean addBillInfo(BillInfoDTO billInfoDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_BILLINFO_IDBILL,billInfoDTO.getIdBill());
        contentValues.put(CreateDatabase.TBL_BILLINFO_IDFOOD,billInfoDTO.getIdFood());
        contentValues.put(CreateDatabase.TBL_BILLINFO_COUNT,billInfoDTO.getCount());
        contentValues.put(CreateDatabase.TBL_BILLINFO_NOTE,billInfoDTO.getNote());
        long chk = database.insert(CreateDatabase.TBL_BILLINFO,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean updateBillInfo(int id,BillInfoDTO billInfoDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_BILLINFO_IDBILL,billInfoDTO.getIdBill());
        contentValues.put(CreateDatabase.TBL_BILLINFO_IDFOOD,billInfoDTO.getIdFood());
        contentValues.put(CreateDatabase.TBL_BILLINFO_COUNT,billInfoDTO.getCount());
        contentValues.put(CreateDatabase.TBL_BILLINFO_NOTE,billInfoDTO.getNote());
        long chk = database.update(CreateDatabase.TBL_BILLINFO,contentValues,
                CreateDatabase.TBL_BILLINFO_ID+" =? ",
                new String[]{String.valueOf(id)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteBillInfo(int id){
        long chk = database.delete(CreateDatabase.TBL_BILLINFO,
                CreateDatabase.TBL_BILLINFO_ID+" =? ",
                new String[]{String.valueOf(id)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    @SuppressLint("Range")
    public List<BillInfoDTO> getListBillInfo(int idBill){
        List<BillInfoDTO> billDTOList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_BILLINFO+" WHERE "+CreateDatabase.TBL_BILLINFO_IDBILL+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(idBill)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            BillInfoDTO billInfoDTO = new BillInfoDTO();
            Log.e("idBillInfo",String.valueOf(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_ID))));
            billInfoDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_ID)));
            billInfoDTO.setIdBill(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_IDBILL)));
            billInfoDTO.setIdFood(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_IDFOOD)));
            billInfoDTO.setCount(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_COUNT)));
            billInfoDTO.setNote(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_NOTE)));
            billDTOList.add(billInfoDTO);
            cursor.moveToNext();
        }
        return billDTOList;
    }
    @SuppressLint("Range")
    public BillInfoDTO getBillInfoById(int idBill, int idFood){
        BillInfoDTO billInfoDTO = new BillInfoDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_BILLINFO+" WHERE "+CreateDatabase.TBL_BILLINFO_IDBILL+" =? AND "+CreateDatabase.TBL_BILLINFO_IDFOOD+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(idBill),String.valueOf(idFood)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            billInfoDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_ID)));
            billInfoDTO.setIdBill(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_IDBILL)));
            billInfoDTO.setIdFood(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_IDFOOD)));
            billInfoDTO.setCount(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_COUNT)));
            billInfoDTO.setNote(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_NOTE)));
            cursor.moveToNext();
        }
        return billInfoDTO;
    }

    public boolean checkFoodExist(int idBill, int idFood) {
        String query = "SELECT * FROM " +CreateDatabase.TBL_BILLINFO+ " WHERE " +CreateDatabase.TBL_BILLINFO_IDBILL+
                " =? AND " +CreateDatabase.TBL_BILLINFO_IDFOOD+ " =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(idBill),String.valueOf(idFood)});
        if(cursor.getCount() != 0){
            return true;
        }else {
            return false;
        }
    }

    @SuppressLint("Range")
    public List<TopFoodDTO> getListTopFood(String date) {
        final String BILLCOUNT = "BILLCOUNT";
        List<TopFoodDTO> topFoodDTOList = new ArrayList<>();
        String query = "SELECT "+CreateDatabase.TBL_BILLINFO_IDFOOD+" , COUNT("+CreateDatabase.TBL_BILLINFO_ID+") AS " +BILLCOUNT+
                " FROM "+CreateDatabase.TBL_BILLINFO+" WHERE "+CreateDatabase.TBL_BILLINFO_IDBILL+
                " IN (SELECT "+CreateDatabase.TBL_BILL_ID+" FROM "+CreateDatabase.TBL_BILL+" WHERE "+CreateDatabase.TBL_BILL_DATECHECKIN+" =? )" +
                " GROUP BY "+CreateDatabase.TBL_BILLINFO_IDFOOD+
                " ORDER BY COUNT("+CreateDatabase.TBL_BILLINFO_ID+") DESC"+
                " LIMIT 20";
        Cursor cursor = database.rawQuery(query,new String[]{date});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            TopFoodDTO topFoodDTO = new TopFoodDTO();
            topFoodDTO.setIdFood(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_IDFOOD)));
            topFoodDTO.setCount(cursor.getInt(cursor.getColumnIndex(BILLCOUNT)));
            topFoodDTOList.add(topFoodDTO);
            cursor.moveToNext();
        }
        return topFoodDTOList;
    }
    @SuppressLint("Range")
    public List<TopFoodDTO> getListTopFood(String datebegin, String dateend) {
        final String BILLCOUNT = "BILLCOUNT";
        List<TopFoodDTO> topFoodDTOList = new ArrayList<>();
        String query = "SELECT "+CreateDatabase.TBL_BILLINFO_IDFOOD+" , COUNT("+CreateDatabase.TBL_BILLINFO_ID+") AS " +BILLCOUNT+
                " FROM "+CreateDatabase.TBL_BILLINFO+" WHERE "+CreateDatabase.TBL_BILLINFO_IDBILL+
                " IN (SELECT "+CreateDatabase.TBL_BILL_ID+" FROM "+CreateDatabase.TBL_BILL+" WHERE "+CreateDatabase.TBL_BILL_DATECHECKIN+" BETWEEN ? AND ? )" +
                " GROUP BY "+CreateDatabase.TBL_BILLINFO_IDFOOD+
                " ORDER BY COUNT("+CreateDatabase.TBL_BILLINFO_ID+") DESC"+
                " LIMIT 20";
        Cursor cursor = database.rawQuery(query,new String[]{datebegin,dateend});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            TopFoodDTO topFoodDTO = new TopFoodDTO();
            topFoodDTO.setIdFood(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILLINFO_IDFOOD)));
            topFoodDTO.setCount(cursor.getInt(cursor.getColumnIndex(BILLCOUNT)));
            topFoodDTOList.add(topFoodDTO);
            cursor.moveToNext();
        }
        return topFoodDTOList;
    }

}
