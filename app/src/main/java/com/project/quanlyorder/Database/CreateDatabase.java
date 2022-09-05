package com.project.quanlyorder.Database;

import static com.project.quanlyorder.R.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.project.quanlyorder.R;

import java.io.File;

public class CreateDatabase extends SQLiteOpenHelper {

    public static String TBL_ACCOUNT = "ACCOUNT";
    public static String TBL_USERINFO = "USERINFO";
    public static String TBL_ZONE = "ZONE";
    public static String TBL_TABLEFOOD = "TABLEFOOD";
    public static String TBL_FOODCATEGORY = "FOODCATEGORY";
    public static String TBL_FOOD = "FOOD";
    public static String TBL_BILL = "BILL";
    public static String TBL_BILLINFO = "BILLINFO";
    public static String TBL_CHECK = "CHECKOUT";
    public static String TBL_SALE = "SALE";

    //Bảng tài khoản
    public static String TBL_ACCOUNT_ID = "ID";
    public static String TBL_ACCOUNT_USERNAME = "USERNAME";
    public static String TBL_ACCOUNT_PASSWORD = "PASSWORD";
    public static String TBL_ACCOUNT_TYPEACC = "TYPEACC";

    //Bảng thông tin người dùng
    public static String TBL_USERINFO_IDACCOUNT = "IDACCOUNT";
    public static String TBL_USERINFO_FULLNAME = "FULLNAME";
    public static String TBL_USERINFO_GENDER = "GENDER";
    public static String TBL_USERINFO_BIRTHDAY = "BIRTHDAY";
    public static String TBL_USERINFO_EMAIL = "EMAIL";
    public static String TBL_USERINFO_PHONENUMBER = "PHONENUMBER";
    public static String TBL_USERINFO_USERPOSITION = "USERPOSITION";

    //Bảng khu vực
    public static String TBL_ZONE_ID = "ID";
    public static String TBL_ZONE_NAME = "NAME";

    //Bảng bàn ăn
    public static String TBL_TABLEFOOD_ID = "ID";
    public static String TBL_TABLEFOOD_IDZONE = "IDZONE";
    public static String TBL_TABLEFOOD_NAME = "NAME";
    public static String TBL_TABLEFOOD_STATUS = "STATUS";

    //Bảng loại món
    public static String TBL_FOODCATEGORY_ID = "ID";
    public static String TBL_FOODCATEGORY_NAME = "NAME";
    public static String TBL_FOODCATEGORY_IDSALE = "IDSALE";
    public static String TBL_FOODCATEGORY_IMG = "IMG";

    //Bảng món ăn
    public static String TBL_FOOD_ID = "ID";
    public static String TBL_FOOD_NAME = "NAME";
    public static String TBL_FOOD_IDCATEGORY = "IDCATEGORY";
    public static String TBL_FOOD_PRICE = "PRICE";
    public static String TBL_FOOD_IDSALE = "IDSALE";
    public static String TBL_FOOD_STATUS = "STATUS";
    public static String TBL_FOOD_IMG = "IMG";

    //Bảng hóa đơn
    public static String TBL_BILL_ID = "ID";
    public static String TBL_BILL_IDTABLE = "IDTABLE";
    public static String TBL_BILL_DATECHECKIN = "DATECHECKIN";
    public static String TBL_BILL_STATUS = "STATUS";
    public static String TBL_BILL_TOTAL = "TOTAL";

    //Bảng chi tiết hóa đơn
    public static String TBL_BILLINFO_ID = "ID";
    public static String TBL_BILLINFO_IDBILL = "IDBILL";
    public static String TBL_BILLINFO_IDFOOD = "IDFOOD";
    public static String TBL_BILLINFO_COUNT = "COUNT";
    public static String TBL_BILLINFO_NOTE = "NOTE";

    //Bảng thanh toán
    public static String TBL_CHECK_IDBILL = "IDBILL";
    public static String TBL_CHECK_IDSTAFF = "IDSTAFF";
    public static String TBL_CHECK_DATECHECKOUT = "DATECHECKOUT";
    public static String TBL_CHECK_TOTAL = "TOTAL";
    public static String TBL_CHECK_IDSALE = "IDSALE";
    public static String TBL_CHECK_PAID = "PAID";
    public static String TBL_CHECK_NOTE = "NOTE";

    //Bảng mã giảm giá
    public static String TBL_SALE_ID = "ID";
    public static String TBL_SALE_NAME = "NAME";
    public static String TBL_SALE_VALUESALE = "VALUESALE";

    public CreateDatabase(Context context) {
        super(context, "OrderApp", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tblACCOUNT = "CREATE TABLE " +TBL_ACCOUNT+ " ( " +TBL_ACCOUNT_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_ACCOUNT_USERNAME+ " TEXT, "+TBL_ACCOUNT_PASSWORD+" TEXT, " +TBL_ACCOUNT_TYPEACC+" INTEGER )";
        String tbl_USERINFO = "CREATE TABLE "+TBL_USERINFO+" ( "+TBL_USERINFO_IDACCOUNT+" INTEGER PRIMARY KEY, "
                +TBL_USERINFO_FULLNAME+" TEXT, "+TBL_USERINFO_GENDER+" TEXT, "+TBL_USERINFO_BIRTHDAY+" TEXT, "
                +TBL_USERINFO_EMAIL+" TEXT, "+TBL_USERINFO_PHONENUMBER+" TEXT, "+TBL_USERINFO_USERPOSITION+" TEXT )";
        String tbl_ZONE = "CREATE TABLE "+TBL_ZONE+" ( "+TBL_ZONE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_ZONE_NAME+" TEXT )";
        String tbl_TABLEFOOD = "CREATE TABLE "+TBL_TABLEFOOD+" ( "+TBL_TABLEFOOD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_TABLEFOOD_IDZONE+" INTEGER, "+TBL_TABLEFOOD_NAME+" TEXT, "+TBL_TABLEFOOD_STATUS+" INTEGER ) ";
        String tbl_FOODCATEGORY = "CREATE TABLE "+TBL_FOODCATEGORY+" ( "+TBL_FOODCATEGORY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_FOODCATEGORY_NAME+" TEXT, "+TBL_FOODCATEGORY_IDSALE+" INTEGER, "+TBL_FOODCATEGORY_IMG+" BLOB) ";
        String tbl_FOOD = "CREATE TABLE "+TBL_FOOD+" ( "+TBL_FOOD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_FOOD_NAME+" TEXT, "+TBL_FOOD_IDCATEGORY+" INTEGER, "+TBL_FOOD_PRICE+" INTEGER, "
                +TBL_FOOD_IDSALE+" INTEGER, "+TBL_FOOD_STATUS+" INTEGER, "+TBL_FOOD_IMG+" BLOB )";
        String tbl_BILL = "CREATE TABLE "+TBL_BILL+" ( "+TBL_BILL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_BILL_IDTABLE+" INTEGER, "+TBL_BILL_DATECHECKIN+" TEXT, "+TBL_BILL_STATUS+" INTEGER, "+TBL_BILL_TOTAL+" INTEGER ) ";
        String tbl_BILLINFO = "CREATE TABLE "+TBL_BILLINFO +" ( "+TBL_BILLINFO_ID+" INTEGER PRIMARY KEY,"
                +TBL_BILLINFO_IDBILL+" INTEGER, "+TBL_BILLINFO_IDFOOD+" INTEGER, "+TBL_BILLINFO_COUNT+" INTEGER,"+TBL_BILLINFO_NOTE+" TEXT ) ";
        String tbl_CHECK = "CREATE TABLE "+TBL_CHECK+" ( "+TBL_CHECK_IDBILL+" INTEGER PRIMARY KEY, "
                +TBL_CHECK_IDSTAFF+" INTEGER, "+TBL_CHECK_DATECHECKOUT+" TEXT, "+TBL_CHECK_TOTAL+" INTEGER, "
                +TBL_CHECK_IDSALE+" INTEGER, "+TBL_CHECK_PAID+" INTEGER, "+TBL_CHECK_NOTE+ " TEXT ) ";
        String tbl_SALE = "CREATE TABLE "+TBL_SALE+" ( "+TBL_SALE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_SALE_NAME+" TEXT, "+TBL_SALE_VALUESALE+" INTEGER ) ";

        db.execSQL(tblACCOUNT);
        db.execSQL(tbl_USERINFO);
        db.execSQL(tbl_ZONE);
        db.execSQL(tbl_TABLEFOOD);
        db.execSQL(tbl_FOODCATEGORY);
        db.execSQL(tbl_FOOD);
        db.execSQL(tbl_BILL);
        db.execSQL(tbl_BILLINFO);
        db.execSQL(tbl_CHECK);
        db.execSQL(tbl_SALE);

        String insSale = "INSERT INTO "+TBL_SALE+" VALUES(1,'Gia goc',0)";
        String insSale2 = "INSERT INTO "+TBL_SALE+" VALUES(2,'Giam 8/3',10)";
        String insAcc = "INSERT INTO "+TBL_ACCOUNT+" VALUES(1,'admin','AAaa@@',1)";
        String insUsInf = "INSERT INTO "+TBL_USERINFO+" VALUES(1,'ADMIN','NAM','01/01/2000','admin@gmail.com','0123456789','Quan ly tang 1')";
        String insZon01 = "INSERT INTO "+TBL_ZONE+" VALUES(1,'ZONE A')";
        String insZon02 = "INSERT INTO "+TBL_ZONE+" VALUES(2,'ZONE B')";
        String insTable1 = "INSERT INTO "+TBL_TABLEFOOD+" VALUES(1,1,'TABLE A1',0)";
        String insTable2 = "INSERT INTO "+TBL_TABLEFOOD+" VALUES(2,1,'TABLE A2',0)";
        db.execSQL(insSale);
        db.execSQL(insAcc);
        db.execSQL(insUsInf);
        db.execSQL(insSale2);
        db.execSQL(insZon01);
        db.execSQL(insZon02);
        db.execSQL(insTable1);
        db.execSQL(insTable2);
        Log.e("","Khoi tao database thanh cong");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    //mở kết nối csdl
    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }
}
