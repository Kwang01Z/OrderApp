package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.project.quanlyorder.DAO.SaleDAO;
import com.project.quanlyorder.DTO.SaleDTO;
import com.project.quanlyorder.R;

public class AddSaleActivity extends AppCompatActivity {

    TextInputLayout TXTL_addsale_NameSale, TXTL_addsale_ValueSale;
    Button BTN_addsale_AddSale;
    SaleDAO saleDAO;
    int idSale = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sale_layout);

        //region Lấy đối tượng trong view
        TXTL_addsale_ValueSale = (TextInputLayout)findViewById(R.id.txtl_addsale_ValueSale);
        TXTL_addsale_NameSale = (TextInputLayout)findViewById(R.id.txtl_addsale_NameSale);
        BTN_addsale_AddSale = (Button)findViewById(R.id.btn_addsale_AddSale);

        saleDAO = new SaleDAO(this);
        //region Hiển thị trang sửa nếu được chọn từ context menu sửa
        idSale = getIntent().getIntExtra("idSale",0);
        if(idSale != 0){
            SaleDTO saleDTO = saleDAO.getSaleById(idSale);
            setTitle(R.string.editsale);
            //Hiển thị lại thông tin từ csdl
            TXTL_addsale_ValueSale.getEditText().setText(String.valueOf(saleDTO.getSalevalue()));
            TXTL_addsale_NameSale.getEditText().setText(saleDTO.getName());
            BTN_addsale_AddSale.setText(getResources().getString(R.string.editsale));
        }
        //endregion
        BTN_addsale_AddSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName() || !validateValueSale()){
                    return;
                }
                String function;
                boolean resultSale;
                String sNameSale = TXTL_addsale_NameSale.getEditText().getText().toString();
                int sValueSale = Integer.parseInt(TXTL_addsale_ValueSale.getEditText().getText().toString()) ;
                SaleDTO saleDTO = new SaleDTO();
                saleDTO.setName(sNameSale);
                saleDTO.setSalevalue(sValueSale);
                if(idSale != 0){
                    resultSale = saleDAO.updateSale(idSale,saleDTO);
                    function = "updateSale";
                } else {
                    if(!validateName2()){
                        return;
                    }
                    resultSale = saleDAO.addSale(saleDTO);
                    function = "addSale";
                }
                //trả về result cho displaytable
                Intent intent = new Intent();
                intent.putExtra("result",resultSale);
                intent.putExtra("function",function);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    //validate dữ liệu
    private boolean validateName(){
        String val = TXTL_addsale_NameSale.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addsale_NameSale.setError(getResources().getString(R.string.not_empty));
            return false;
        }else{
            TXTL_addsale_NameSale.setError(null);
            TXTL_addsale_NameSale.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateName2(){
        String val = TXTL_addsale_NameSale.getEditText().getText().toString().trim();
        if(saleDAO.CheckExist(val) != 0) {
            TXTL_addsale_NameSale.setError(getResources().getString(R.string.sale_exist));
            return false;
        } else {
            TXTL_addsale_NameSale.setError(null);
            TXTL_addsale_NameSale.setErrorEnabled(false);
            return true;
        }
    }
    //validate dữ liệu
    private boolean validateValueSale(){
        String val = TXTL_addsale_ValueSale.getEditText().getText().toString();
        if(val.isEmpty() || val.equals("")){
            TXTL_addsale_ValueSale.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(Integer.parseInt(val) < 0 || Integer.parseInt(val) > 100){
            TXTL_addsale_ValueSale.setError(getResources().getString(R.string.valuesale_error));
            return false;
        }{
            TXTL_addsale_ValueSale.setError(null);
            TXTL_addsale_ValueSale.setErrorEnabled(false);
            return true;
        }
    }
}