package com.project.quanlyorder.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.project.quanlyorder.DTO.ZoneDTO;
import com.project.quanlyorder.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class ZoneDAO {
    SQLiteDatabase database;

    public ZoneDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }
    public boolean addZone(ZoneDTO zoneDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_ZONE_NAME,zoneDTO.getName());
        long chk = database.insert(CreateDatabase.TBL_ZONE,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean addZone(String nameZone){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_ZONE_NAME,nameZone);
        long chk = database.insert(CreateDatabase.TBL_ZONE,null,contentValues);
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean updateZone(int idZone,ZoneDTO zoneDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_ZONE_NAME,zoneDTO.getName());
        long chk = database.update(CreateDatabase.TBL_ZONE,contentValues,
                CreateDatabase.TBL_ZONE_ID+" =? ",
                new String[]{String.valueOf(idZone)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteZone(int idZone){
        long chk = database.delete(CreateDatabase.TBL_ZONE,
                CreateDatabase.TBL_ZONE_ID+" =? ",
                new String[]{String.valueOf(idZone)});
        if(chk!=0){
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("Range")
    public int CheckExist(String zoneName){
        int idZone = 0;
        String query = "SELECT * FROM " +CreateDatabase.TBL_ZONE+ " WHERE "
                +CreateDatabase.TBL_ZONE_NAME +" = '"+ zoneName+"'";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            idZone = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ZONE_ID));
            cursor.moveToNext();
        }
        return idZone;
    }
    @SuppressLint("Range")
    public List<ZoneDTO> getZoneList() {
        List<ZoneDTO> zoneDTOList = new ArrayList<ZoneDTO>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_ZONE;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            ZoneDTO zoneDTO = new ZoneDTO();
            zoneDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ZONE_ID)));
            zoneDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_ZONE_NAME)));
            zoneDTOList.add(zoneDTO);
            cursor.moveToNext();
        }
        return zoneDTOList;
    }
    @SuppressLint("Range")
    public List<String> getZoneNameList() {
        List<String> zoneNameList = new ArrayList<>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_ZONE;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String nameZone = cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_ZONE_NAME));
            zoneNameList.add(nameZone);
            cursor.moveToNext();
        }
        return zoneNameList;
    }
    @SuppressLint("Range")
    public ZoneDTO getZoneById(int idZone) {
        ZoneDTO zoneDTO = new ZoneDTO();
        String query = "SELECT * FROM " +CreateDatabase.TBL_ZONE+ " WHERE "
                +CreateDatabase.TBL_ZONE_ID +" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(idZone)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            zoneDTO.setId(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ZONE_ID)));
            zoneDTO.setName(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_ZONE_NAME)));
            cursor.moveToNext();
        }
        return zoneDTO;
    }
    @SuppressLint("Range")
    public int getIdZoneByName(String nameZone) {
        int zoneID = 1;
        String query = "SELECT * FROM " +CreateDatabase.TBL_ZONE+ " WHERE "
                +CreateDatabase.TBL_ZONE_NAME +" =? ";
        Cursor cursor = database.rawQuery(query,new String[]{nameZone});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            zoneID = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_ZONE_ID));
            cursor.moveToNext();
        }
        Log.e("---id:",String.valueOf(zoneID));
        return zoneID;
    }
}
