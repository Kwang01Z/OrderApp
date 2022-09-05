package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.project.quanlyorder.DAO.TableFoodDAO;
import com.project.quanlyorder.DTO.TableFoodDTO;
import com.project.quanlyorder.R;

public class AddTableActivity extends AppCompatActivity {

    TextInputLayout TXTL_addtable_NameTable;
    Button BTN_addtable_AddTable;
    TableFoodDAO tableFoodDAO;
    int idTable = 0;
    int idZone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_table_layout);

        //region Lấy đối tượng trong view
        TXTL_addtable_NameTable = (TextInputLayout)findViewById(R.id.txtl_addtable_NameTable);
        BTN_addtable_AddTable = (Button)findViewById(R.id.btn_addtable_AddTable);

        tableFoodDAO = new TableFoodDAO(this);
        setTitle(R.string.addTable);
        //region Hiển thị trang sửa nếu được chọn từ context menu sửa
        idTable = getIntent().getIntExtra("idTable",0);
        if(idTable != 0){
            TableFoodDTO tableFoodDTO = tableFoodDAO.getTableById(idTable);
            setTitle(R.string.edittable);
            //Hiển thị lại thông tin từ csdl
            TXTL_addtable_NameTable.getEditText().setText(tableFoodDTO.getName());
            BTN_addtable_AddTable.setText(getResources().getString(R.string.edittable));
        }
        //endregion
        BTN_addtable_AddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName()){
                    return;
                }
                String function;
                boolean resultSale;
                String sNameTable = TXTL_addtable_NameTable.getEditText().getText().toString();
                idZone = getIntent().getIntExtra("idZone",1);
                TableFoodDTO tableFoodDTO = new TableFoodDTO();
                tableFoodDTO.setName(sNameTable);
                tableFoodDTO.setIdZone(idZone);
                tableFoodDTO.setStatus(0);
                if(idTable != 0){
                    resultSale = tableFoodDAO.updateTableFood(idTable,tableFoodDTO);
                    function = "updateTable";
                } else {
                    if(!validateName2()){
                        return;
                    }
                    resultSale = tableFoodDAO.addTableFood(tableFoodDTO);
                    function = "addTable";
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
        String val = TXTL_addtable_NameTable.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addtable_NameTable.setError(getResources().getString(R.string.not_empty));
            return false;
        }else{
            TXTL_addtable_NameTable.setError(null);
            TXTL_addtable_NameTable.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateName2(){
        String val = TXTL_addtable_NameTable.getEditText().getText().toString().trim();
        if(tableFoodDAO.CheckExist(val) != 0){
            TXTL_addtable_NameTable.setError(getResources().getString(R.string.table_exist));
            return false;
        } else{
            TXTL_addtable_NameTable.setError(null);
            TXTL_addtable_NameTable.setErrorEnabled(false);
            return true;
        }
    }
}