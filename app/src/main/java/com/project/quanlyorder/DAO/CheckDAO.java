package com.project.quanlyorder.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.quanlyorder.DTO.CheckDTO;
import com.project.quanlyorder.DTO.CheckDTO;
import com.project.quanlyorder.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class CheckDAO {
    SQLiteDatabase database;

    public CheckDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }
    public boolean addCheck(CheckDTO checkDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_CHECK_IDBILL,checkDTO.getIdBill());
        contentValues.put(CreateDatabase.TBL_CHECK_IDSTAFF,checkDTO.getIdStaff());
        contentValues.put(CreateDatabase.TBL_CHECK_DATECHECKOUT,checkDTO.getDateCheckOut());
        contentValues.put(CreateDatabase.TBL_CHECK_TOTAL,checkDTO.getTotal());
        contentValues.put(CreateDatabase.TBL_CHECK_IDSALE,checkDTO.getIdSale());
        contentValues.put(CreateDatabase.TBL_CHECK_PAID,checkDTO.getPaid());
        contentValues.put(CreateDatabase.TBL_CHECK_NOTE,checkDTO.getNote());
        long chk = database.insert(CreateDatabase.TBL_CHECK,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean updateCheck(int idBill, CheckDTO checkDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_CHECK_IDSTAFF,checkDTO.getIdStaff());
        contentValues.put(CreateDatabase.TBL_CHECK_DATECHECKOUT,checkDTO.getDateCheckOut());
        contentValues.put(CreateDatabase.TBL_CHECK_TOTAL,checkDTO.getTotal());
        contentValues.put(CreateDatabase.TBL_CHECK_IDSALE,checkDTO.getIdSale());
        contentValues.put(CreateDatabase.TBL_CHECK_PAID,checkDTO.getPaid());
        contentValues.put(CreateDatabase.TBL_CHECK_NOTE,checkDTO.getNote());
        long chk = database.update(CreateDatabase.TBL_CHECK, contentValues,
                CreateDatabase.TBL_CHECK_IDBILL+" =? ",
                new String[]{String.valueOf(idBill)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteCheck(int idBill){
        long chk = database.delete(CreateDatabase.TBL_CHECK,
                CreateDatabase.TBL_CHECK_IDBILL+" =? ",
                new String[]{String.valueOf(idBill)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    @SuppressLint("Range")
    public List<CheckDTO> getListCheck(){
        List<CheckDTO> checkDTOList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_CHECK;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            CheckDTO checkDTO = new CheckDTO();
            checkDTO.setIdBill(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDBILL)));
            checkDTO.setIdStaff(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDSTAFF)));
            checkDTO.setIdSale(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDSALE)));
            checkDTO.setDateCheckOut(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_DATECHECKOUT)));
            checkDTO.setTotal(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_TOTAL)));
            checkDTO.setNote(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_NOTE)));
            checkDTO.setPaid(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_PAID)));
            checkDTOList.add(checkDTO);
            cursor.moveToNext();
        }
        return checkDTOList;
    }
    @SuppressLint("Range")
    public CheckDTO getCheckById(int idCheck){
        CheckDTO checkDTO = new CheckDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_CHECK+" WHERE "+CreateDatabase.TBL_CHECK_IDBILL+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(idCheck)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            checkDTO.setIdBill(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDBILL)));
            checkDTO.setIdStaff(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDSTAFF)));
            checkDTO.setIdSale(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDSALE)));
            checkDTO.setDateCheckOut(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_DATECHECKOUT)));
            checkDTO.setTotal(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_TOTAL)));
            checkDTO.setNote(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_NOTE)));
            checkDTO.setPaid(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_PAID)));
            cursor.moveToNext();
        }
        return checkDTO;
    }

    @SuppressLint("Range")
    public int getTurnoverByDay(String date) {
        int sum = 0;
        final String SUMTURNOVER = "SUMTURNOVER";
        String query = "SELECT SUM("+CreateDatabase.TBL_CHECK_TOTAL+") AS " +SUMTURNOVER+
                " FROM "+CreateDatabase.TBL_CHECK+
                " WHERE "+CreateDatabase.TBL_CHECK_DATECHECKOUT+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(date)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            sum = cursor.getInt(cursor.getColumnIndex(SUMTURNOVER));
            cursor.moveToNext();
        }
        return sum;
    }
    @SuppressLint("Range")
    public ArrayList<Integer> getListTurnover(String datebegin, String dateend) {
        ArrayList<Integer> listTurnover = new ArrayList<>();
        final String SUMTURNOVER = "SUMTURNOVER";
        String query = "SELECT SUM("+CreateDatabase.TBL_CHECK_TOTAL+") AS " +SUMTURNOVER+
                " FROM "+CreateDatabase.TBL_CHECK+
                " WHERE "+CreateDatabase.TBL_CHECK_DATECHECKOUT+" BETWEEN ? AND ? " +
                " GROUP BY "+CreateDatabase.TBL_CHECK_DATECHECKOUT;
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(datebegin),String.valueOf(dateend)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            listTurnover.add(cursor.getInt(cursor.getColumnIndex(SUMTURNOVER)));
            cursor.moveToNext();
        }
        return listTurnover;
    }

    @SuppressLint("Range")
    public List<CheckDTO> getListCheckByDay(String date) {
        List<CheckDTO> checkDTOList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_CHECK+" WHERE "+
                CreateDatabase.TBL_CHECK_NOTE+" IS NOT NULL AND "+CreateDatabase.TBL_CHECK_NOTE+" <>'' AND "+CreateDatabase.TBL_CHECK_DATECHECKOUT+" =? ";
        Cursor cursor = database.rawQuery(query,new  String[]{date});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            CheckDTO checkDTO = new CheckDTO();
            checkDTO.setIdBill(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDBILL)));
            checkDTO.setIdStaff(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDSTAFF)));
            checkDTO.setIdSale(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDSALE)));
            checkDTO.setDateCheckOut(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_DATECHECKOUT)));
            checkDTO.setTotal(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_TOTAL)));
            checkDTO.setNote(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_NOTE)));
            checkDTO.setPaid(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_PAID)));
            checkDTOList.add(checkDTO);
            cursor.moveToNext();
        }
        return checkDTOList;

    }
    @SuppressLint("Range")
    public List<CheckDTO> getListCheckByDay(String datebegin, String dateend) {
        List<CheckDTO> checkDTOList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_CHECK+" WHERE "+
                CreateDatabase.TBL_CHECK_NOTE+" IS NOT NULL AND "+CreateDatabase.TBL_CHECK_NOTE+" <>'' AND "+CreateDatabase.TBL_CHECK_DATECHECKOUT+" BETWEEN ? AND ? ";
        Cursor cursor = database.rawQuery(query,new  String[]{datebegin,dateend});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            CheckDTO checkDTO = new CheckDTO();
            checkDTO.setIdBill(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDBILL)));
            checkDTO.setIdStaff(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDSTAFF)));
            checkDTO.setIdSale(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_IDSALE)));
            checkDTO.setDateCheckOut(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_DATECHECKOUT)));
            checkDTO.setTotal(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_TOTAL)));
            checkDTO.setNote(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_NOTE)));
            checkDTO.setPaid(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHECK_PAID)));
            checkDTOList.add(checkDTO);
            cursor.moveToNext();
        }
        return checkDTOList;

    }
}
