package com.project.quanlyorder.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.quanlyorder.DTO.AccountDTO;
import com.project.quanlyorder.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    SQLiteDatabase database;
    public AccountDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public boolean addAccount(AccountDTO accountDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_ACCOUNT_USERNAME,accountDTO.getUserName());
        contentValues.put(CreateDatabase.TBL_ACCOUNT_PASSWORD,accountDTO.getPassWord());
        contentValues.put(CreateDatabase.TBL_ACCOUNT_TYPEACC,accountDTO.getTypeAcc());
        long chk = database.insert(CreateDatabase.TBL_ACCOUNT,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }

    public boolean updateAccount(int idAcc, AccountDTO accountDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_ACCOUNT_USERNAME,accountDTO.getUserName());
        contentValues.put(CreateDatabase.TBL_ACCOUNT_PASSWORD,accountDTO.getPassWord());
        contentValues.put(CreateDatabase.TBL_ACCOUNT_TYPEACC,accountDTO.getTypeAcc());
        long chk = database.update(CreateDatabase.TBL_ACCOUNT,contentValues,
                CreateDatabase.TBL_ACCOUNT_ID+" =? ",
                new String[]{String.valueOf(idAcc)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAccount(int idAcc){
        long chk = database.delete(CreateDatabase.TBL_ACCOUNT,
                CreateDatabase.TBL_ACCOUNT_ID+" =? ",
                new String[]{String.valueOf(idAcc)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("Range")
    public int CheckExist(String userName){
        int idStaff = 0;
        String query = "SELECT * FROM " +CreateDatabase.TBL_ACCOUNT+ " WHERE "
                +CreateDatabase.TBL_ACCOUNT_USERNAME +" = '"+ userName+"'";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            idStaff = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_ID));
            cursor.moveToNext();
        }
        return idStaff;
    }

    @SuppressLint("Range")
    public int CheckExist(String userName, String passWd){
        int idStaff = 0;
        String query = "SELECT * FROM " +CreateDatabase.TBL_ACCOUNT+ " WHERE "
                +CreateDatabase.TBL_ACCOUNT_USERNAME +" = '"+ userName+"' AND "+CreateDatabase.TBL_ACCOUNT_PASSWORD +" = '" +passWd +"'";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            idStaff = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_ID));
            cursor.moveToNext();
        }
            return idStaff;
    }

    @SuppressLint("Range")
    public int getTypeAcc(int idAcc) {
        int typeAcc = -1;
        String query = "SELECT * FROM "+CreateDatabase.TBL_ACCOUNT+" WHERE "+CreateDatabase.TBL_ACCOUNT_ID+" = "+idAcc;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            typeAcc = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_TYPEACC));
            cursor.moveToNext();
        }
            return typeAcc;
    }
    @SuppressLint("Range")
    public int getIdByUserName(String usName){
        int  id = 0;
        String query = "SELECT * FROM "+CreateDatabase.TBL_ACCOUNT+" WHERE "+CreateDatabase.TBL_ACCOUNT_USERNAME+" = '"+usName+"'";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_ID));
            cursor.moveToNext();
        }
        return id;
    }

    @SuppressLint("Range")
    public List<AccountDTO> getAccountList() {
        List<AccountDTO> accountDTOList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_ACCOUNT;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_ID)));
            accountDTO.setUserName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_USERNAME)));
            accountDTO.setPassWord(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_PASSWORD)));
            accountDTO.setTypeAcc(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_TYPEACC)));
            accountDTOList.add(accountDTO);
            cursor.moveToNext();
        }
        return accountDTOList;
    }

    @SuppressLint("Range")
    public AccountDTO getAccountById(int idAcc) {
        AccountDTO accountDTO = new AccountDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_ACCOUNT+" WHERE "+CreateDatabase.TBL_ACCOUNT_ID+" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(idAcc)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            accountDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_ID)));
            accountDTO.setUserName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_USERNAME)));
            accountDTO.setPassWord(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_PASSWORD)));
            accountDTO.setTypeAcc(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ACCOUNT_TYPEACC)));
            cursor.moveToNext();
        }
        return accountDTO;
    }
}
