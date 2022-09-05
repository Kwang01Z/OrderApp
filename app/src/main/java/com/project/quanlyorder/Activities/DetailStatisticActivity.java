package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.quanlyorder.CustomAdapter.AdapterDisplayPayment;
import com.project.quanlyorder.DAO.BillInfoDAO;
import com.project.quanlyorder.DAO.CheckDAO;
import com.project.quanlyorder.DAO.TableFoodDAO;
import com.project.quanlyorder.DAO.UserInfoDAO;
import com.project.quanlyorder.DTO.BillInfoDTO;
import com.project.quanlyorder.DTO.UserInfoDTO;
import com.project.quanlyorder.OtherLibrary.DisplayBillInfoNoteActivity;
import com.project.quanlyorder.R;

import java.util.List;

public class DetailStatisticActivity extends AppCompatActivity {

    ImageView img_detailstatistic_backbtn, img_detailstatistic_note;
    TextView txt_detailstatistic_IDBill,txt_detailstatistic_DateCheckOut,txt_detailstatistic_NameTable
            ,txt_detailstatistic_NameStaff,txt_detailstatistic_Total;
    GridView gvDetailStatistic;
    int idBill, idStaff, idTable;
    String DateCheckOut;
    int totalMoney;
    UserInfoDAO userInfoDAO;
    TableFoodDAO tableFoodDAO;
    List<BillInfoDTO> billinfoDTOList;
    CheckDAO checkDAO;
    BillInfoDAO billInfoDAO;
    AdapterDisplayPayment adapterDisplayPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_statistic_layout);

        //region Thuộc tính bên view
        img_detailstatistic_backbtn = (ImageView)findViewById(R.id.img_detailstatistic_backbtn);
        txt_detailstatistic_IDBill = (TextView)findViewById(R.id.txt_detailstatistic_IDBill);
        txt_detailstatistic_DateCheckOut = (TextView)findViewById(R.id.txt_detailstatistic_DateCheckOut);
        txt_detailstatistic_NameTable = (TextView)findViewById(R.id.txt_detailstatistic_NameTable);
        txt_detailstatistic_NameStaff = (TextView)findViewById(R.id.txt_detailstatistic_NameStaff);
        txt_detailstatistic_Total = (TextView)findViewById(R.id.txt_detailstatistic_Total);
        gvDetailStatistic = (GridView)findViewById(R.id.gvDetailStatistic);
        img_detailstatistic_note = (ImageView)findViewById(R.id.btn_detailstatistic_note);
        //endregion

        //khởi tạo lớp dao mở kết nối csdl
        userInfoDAO = new UserInfoDAO(this);
        tableFoodDAO = new TableFoodDAO(this);
        checkDAO = new CheckDAO(this);
        billInfoDAO = new BillInfoDAO(this);

        //Lấy thông tin từ display statistic
        Intent intent = getIntent();
        idBill = intent.getIntExtra("idBill",0);
        idStaff = checkDAO.getCheckById(idBill).getIdStaff();
        idTable = intent.getIntExtra("idTable",0);
        DateCheckOut = checkDAO.getCheckById(idBill).getDateCheckOut();
        totalMoney = intent.getIntExtra("sumMoney",0);

        //chỉ hiển thị nếu lấy đc mã đơn đc chọn
        if (idBill !=0){
            txt_detailstatistic_IDBill.setText(getResources().getString(R.string.idBill)+idBill);
            txt_detailstatistic_DateCheckOut.setText(DateCheckOut);
            txt_detailstatistic_Total.setText(String.valueOf(totalMoney)+" VNĐ");

            UserInfoDTO userInfoDTO = userInfoDAO.getUserInfoById(idStaff);
            txt_detailstatistic_NameStaff.setText(userInfoDTO.getFullName());
            txt_detailstatistic_NameTable.setText(tableFoodDAO.getTableById(idTable).getName());

            DisplayDetailStatistic();
        }

        img_detailstatistic_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        img_detailstatistic_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = checkDAO.getCheckById(idBill).getNote();
                DisplayBillInfoNoteActivity displayBillInfoNoteActivity = new DisplayBillInfoNoteActivity(DetailStatisticActivity.this,note);
                displayBillInfoNoteActivity.show();
            }
        });
    }
    private void DisplayDetailStatistic(){
        billinfoDTOList = billInfoDAO.getListBillInfo(idBill);
        adapterDisplayPayment = new AdapterDisplayPayment(this,R.layout.custom_layout_paymentmenu,billinfoDTOList);
        gvDetailStatistic.setAdapter(adapterDisplayPayment);
        adapterDisplayPayment.notifyDataSetChanged();
    }
}