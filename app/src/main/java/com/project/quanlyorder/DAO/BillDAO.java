package com.project.quanlyorder.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.quanlyorder.DTO.BillDTO;
import com.project.quanlyorder.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    SQLiteDatabase database;
    public BillDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public boolean addBill(BillDTO billDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_BILL_IDTABLE,billDTO.getIdTable());
        contentValues.put(CreateDatabase.TBL_BILL_DATECHECKIN,billDTO.getDateCheckIn());
        contentValues.put(CreateDatabase.TBL_BILL_STATUS,billDTO.getStatus());
        contentValues.put(CreateDatabase.TBL_BILL_TOTAL,0);
        long chk = database.insert(CreateDatabase.TBL_BILL,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean updateBill(int idBill, BillDTO billDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_BILL_IDTABLE,billDTO.getIdTable());
        contentValues.put(CreateDatabase.TBL_BILL_DATECHECKIN,billDTO.getDateCheckIn());
        contentValues.put(CreateDatabase.TBL_BILL_STATUS,billDTO.getStatus());
        contentValues.put(CreateDatabase.TBL_BILL_TOTAL,billDTO.getSummoney());
        long chk = database.update(CreateDatabase.TBL_BILL, contentValues,
                CreateDatabase.TBL_BILL_ID+" =? ",
                new String[]{String.valueOf(idBill)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteBill(int idBill){
        long chk = database.delete(CreateDatabase.TBL_BILL,
                CreateDatabase.TBL_BILL_ID+" =? ",
                new String[]{String.valueOf(idBill)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("Range")
    public BillDTO getBillById(int idBill){
        BillDTO billDTO = new BillDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_BILL+" WHERE "+CreateDatabase.TBL_BILL_ID+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(idBill)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            billDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_ID)));
            billDTO.setIdTable(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_IDTABLE)));
            billDTO.setDateCheckIn(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_BILL_DATECHECKIN)));
            billDTO.setStatus(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_STATUS)));
            billDTO.setSummoney(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_TOTAL)));
            cursor.moveToNext();
        }
        return billDTO;
    }
    @SuppressLint("Range")
    public int getIDBillByIdTable(int idTable){
        int id = 0;
        String query = "SELECT * FROM "+CreateDatabase.TBL_BILL+" WHERE "+CreateDatabase.TBL_BILL_IDTABLE+" =? AND "
                +CreateDatabase.TBL_BILL_STATUS+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(idTable),String.valueOf(0)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            id = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_ID));
            cursor.moveToNext();
        }
        return id;
    }

    public void updateSummoneyBill(int idBill, int moneysum) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_BILL_TOTAL,moneysum);
        database.update(CreateDatabase.TBL_BILL, contentValues,
                CreateDatabase.TBL_BILL_ID+" =? ",
                new String[]{String.valueOf(idBill)});
    }

    public void updateStatus(int idBill, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_BILL_STATUS,i);
        database.update(CreateDatabase.TBL_BILL, contentValues,
                CreateDatabase.TBL_BILL_ID+" =? ",
                new String[]{String.valueOf(idBill)});
    }

    @SuppressLint("Range")
    public List<BillDTO> getListBill(){
        List<BillDTO> billDTOList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_BILL;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            BillDTO billDTO = new BillDTO();
            billDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_ID)));
            billDTO.setIdTable(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_IDTABLE)));
            billDTO.setDateCheckIn(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_BILL_DATECHECKIN)));
            billDTO.setStatus(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_STATUS)));
            billDTO.setSummoney(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_TOTAL)));
            billDTOList.add(billDTO);
            cursor.moveToNext();
        }
        return billDTOList;
    }
    @SuppressLint("Range")
    public List<BillDTO> getListBill(String startDate, String endDate) {
        List<BillDTO> billDTOList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_BILL
                +" WHERE "+CreateDatabase.TBL_BILL_DATECHECKIN+" BETWEEN ? AND ?"
                +" ORDER BY "+CreateDatabase.TBL_BILL_ID;
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(startDate),String.valueOf(endDate)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            BillDTO billDTO = new BillDTO();
            billDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_ID)));
            billDTO.setIdTable(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_IDTABLE)));
            billDTO.setDateCheckIn(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_BILL_DATECHECKIN)));
            billDTO.setStatus(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_STATUS)));
            billDTO.setSummoney(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_TOTAL)));
            billDTOList.add(billDTO);
            cursor.moveToNext();
        }
        return billDTOList;
    }
    @SuppressLint("Range")
    public List<BillDTO> getListBill(String Date) {
        List<BillDTO> billDTOList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_BILL
                +" WHERE "+CreateDatabase.TBL_BILL_DATECHECKIN+" =? "
                +" ORDER BY "+CreateDatabase.TBL_BILL_ID+" DESC";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(Date)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            BillDTO billDTO = new BillDTO();
            billDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_ID)));
            billDTO.setIdTable(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_IDTABLE)));
            billDTO.setDateCheckIn(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_BILL_DATECHECKIN)));
            billDTO.setStatus(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_STATUS)));
            billDTO.setSummoney(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BILL_TOTAL)));
            billDTOList.add(billDTO);
            cursor.moveToNext();
        }
        return billDTOList;
    }
    @SuppressLint("Range")
    public int getTotalBillByDay(String date) {
        int count = 0;
        final String BILLCOUNT = "BILLCOUNT";
        String query = "SELECT COUNT("+CreateDatabase.TBL_BILL_ID+") AS " +BILLCOUNT+
                " FROM "+CreateDatabase.TBL_BILL+
                " WHERE "+CreateDatabase.TBL_BILL_DATECHECKIN+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(date)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            count = cursor.getInt(cursor.getColumnIndex(BILLCOUNT));
            cursor.moveToNext();
        }
        return count;
    }
    @SuppressLint("Range")
    public ArrayList<Integer> getListCountBill(String datebegin, String dateend) {
        ArrayList<Integer> listCountBill = new ArrayList<>();
        final String BILLCOUNT = "BILLCOUNT";
        String query = "SELECT COUNT("+CreateDatabase.TBL_BILL_ID+") AS " +BILLCOUNT+
                " FROM "+CreateDatabase.TBL_BILL+
                " WHERE "+CreateDatabase.TBL_BILL_DATECHECKIN+" BETWEEN ? AND ? " +
                " GROUP BY "+CreateDatabase.TBL_BILL_DATECHECKIN;
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(datebegin),String.valueOf(dateend)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            listCountBill.add(cursor.getInt(cursor.getColumnIndex(BILLCOUNT)));
            cursor.moveToNext();
        }
        return listCountBill;
    }

    public void cancelBillifTableFree() {
        String query = "DELETE FROM "+CreateDatabase.TBL_BILL+" WHERE "+
                CreateDatabase.TBL_BILL_IDTABLE+" IN (SELECT "+CreateDatabase.TBL_TABLEFOOD_ID+
                " FROM "+CreateDatabase.TBL_TABLEFOOD+" WHERE "+CreateDatabase.TBL_TABLEFOOD_STATUS+" = 0) AND " +
                CreateDatabase.TBL_BILL_ID+" NOT IN (SELECT "+CreateDatabase.TBL_BILLINFO_IDBILL+" FROM "+CreateDatabase.TBL_BILLINFO+" GROUP BY "+CreateDatabase.TBL_BILLINFO_IDBILL+")";
        database.execSQL(query);
    }
}
