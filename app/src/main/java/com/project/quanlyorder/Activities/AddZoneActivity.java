package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.project.quanlyorder.DAO.ZoneDAO;
import com.project.quanlyorder.DTO.ZoneDTO;
import com.project.quanlyorder.R;

public class AddZoneActivity extends AppCompatActivity {

    TextInputLayout TXTL_addzone_NameZone;
    Button BTN_addzone_AddZone;
    ZoneDAO zoneDAO;
    int idZone = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_zone_layout);

        //region Lấy đối tượng trong view
        TXTL_addzone_NameZone = (TextInputLayout)findViewById(R.id.txtl_addzone_NameZone);
        BTN_addzone_AddZone = (Button)findViewById(R.id.btn_addzone_AddZone);

        zoneDAO = new ZoneDAO(this);
        //region Hiển thị trang sửa nếu được chọn từ context menu sửa
        idZone = getIntent().getIntExtra("idZone",0);
        if(idZone != 0){
            ZoneDTO zoneDTO = zoneDAO.getZoneById(idZone);
            setTitle(R.string.editzone);
            //Hiển thị lại thông tin từ csdl
            TXTL_addzone_NameZone.getEditText().setText(zoneDTO.getName());
            BTN_addzone_AddZone.setText(getResources().getString(R.string.editzone));
        }
        //endregion
        BTN_addzone_AddZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName()){
                    return;
                }
                String function;
                boolean resultZone;
                String sNameZone = TXTL_addzone_NameZone.getEditText().getText().toString();
                ZoneDTO zoneDTO = new ZoneDTO();
                zoneDTO.setName(sNameZone);
                if(idZone != 0){
                    resultZone = zoneDAO.updateZone(idZone,zoneDTO);
                    function = "updateZone";
                } else {
                    if(!validateName2()){
                        return;
                    }
                    resultZone = zoneDAO.addZone(zoneDTO);
                    function = "addZone";
                }
                //trả về result cho displaytable
                Intent intent = new Intent();
                intent.putExtra("result",resultZone);
                intent.putExtra("function",function);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    //validate dữ liệu
    private boolean validateName(){
        String val = TXTL_addzone_NameZone.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addzone_NameZone.setError(getResources().getString(R.string.not_empty));
            return false;
        }else{
            TXTL_addzone_NameZone.setError(null);
            TXTL_addzone_NameZone.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateName2(){
        String val = TXTL_addzone_NameZone.getEditText().getText().toString().trim();
        if(zoneDAO.CheckExist(val) != 0){
            TXTL_addzone_NameZone.setError(getResources().getString(R.string.zone_exist));
            return false;
        } else{
            TXTL_addzone_NameZone.setError(null);
            TXTL_addzone_NameZone.setErrorEnabled(false);
            return true;
        }
    }
}