package com.project.quanlyorder.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.quanlyorder.DTO.UserInfoDTO;
import com.project.quanlyorder.Database.CreateDatabase;

import java.security.cert.Certificate;

public class UserInfoDAO {
    SQLiteDatabase database;

    public UserInfoDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public boolean addUserInfo(UserInfoDTO userInfoDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_USERINFO_IDACCOUNT,userInfoDTO.getIdAcc());
        contentValues.put(CreateDatabase.TBL_USERINFO_FULLNAME,userInfoDTO.getFullName());
        contentValues.put(CreateDatabase.TBL_USERINFO_GENDER,userInfoDTO.getGender());
        contentValues.put(CreateDatabase.TBL_USERINFO_BIRTHDAY,userInfoDTO.getBirthday());
        contentValues.put(CreateDatabase.TBL_USERINFO_EMAIL,userInfoDTO.getEmail());
        contentValues.put(CreateDatabase.TBL_USERINFO_PHONENUMBER,userInfoDTO.getPhoneNumber());
        contentValues.put(CreateDatabase.TBL_USERINFO_USERPOSITION,userInfoDTO.getUserPosition());
        long chk = database.insert(CreateDatabase.TBL_USERINFO,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUserInfo(int id, UserInfoDTO userInfoDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_USERINFO_FULLNAME,userInfoDTO.getFullName());
        contentValues.put(CreateDatabase.TBL_USERINFO_GENDER,userInfoDTO.getGender());
        contentValues.put(CreateDatabase.TBL_USERINFO_BIRTHDAY,userInfoDTO.getBirthday());
        contentValues.put(CreateDatabase.TBL_USERINFO_EMAIL,userInfoDTO.getEmail());
        contentValues.put(CreateDatabase.TBL_USERINFO_PHONENUMBER,userInfoDTO.getPhoneNumber());
        contentValues.put(CreateDatabase.TBL_USERINFO_USERPOSITION,userInfoDTO.getUserPosition());
        long chk = database.update(CreateDatabase.TBL_USERINFO,contentValues,
                CreateDatabase.TBL_USERINFO_IDACCOUNT+" =? ",
                new String[]{String.valueOf(id)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUserInfo(int id){
        long chk = database.delete(CreateDatabase.TBL_USERINFO,
                CreateDatabase.TBL_USERINFO_IDACCOUNT+" =? ",
                new String[]{String.valueOf(id)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    @SuppressLint("Range")
    public UserInfoDTO getUserInfoById(int id){
        String query = "SELECT * FROM "+CreateDatabase.TBL_USERINFO+" WHERE "+CreateDatabase.TBL_USERINFO_IDACCOUNT+" =? ";
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            userInfoDTO.setIdAcc(id);
            userInfoDTO.setFullName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_USERINFO_FULLNAME)));
            userInfoDTO.setGender(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_USERINFO_GENDER)));
            userInfoDTO.setBirthday(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_USERINFO_BIRTHDAY)));
            userInfoDTO.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_USERINFO_PHONENUMBER)));
            userInfoDTO.setEmail(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_USERINFO_EMAIL)));
            userInfoDTO.setUserPosition(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_USERINFO_USERPOSITION)));
            cursor.moveToNext();
        }
        return userInfoDTO;
    }
}
