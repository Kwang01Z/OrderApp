package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.project.quanlyorder.DAO.BillDAO;
import com.project.quanlyorder.DAO.BillInfoDAO;
import com.project.quanlyorder.DAO.TableFoodDAO;
import com.project.quanlyorder.DTO.BillDTO;
import com.project.quanlyorder.DTO.BillInfoDTO;
import com.project.quanlyorder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AmountMenuActivity extends AppCompatActivity {

    TextInputLayout TXTL_amountmenu_Count,TXTL_amountmenu_Note;
    Button BTN_amountmenu_OK;
    ChipGroup CG_amountmenu_function;
    int idTable, idFood,idBill;
    BillDAO billDAO;
    BillInfoDAO billinfoDAO;
    TableFoodDAO tableFoodDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amount_menu_layout);

        //Lấy đối tượng view
        TXTL_amountmenu_Count = (TextInputLayout)findViewById(R.id.txtl_amountmenu_Count);
        BTN_amountmenu_OK = (Button)findViewById(R.id.btn_amountmenu_OK);
        CG_amountmenu_function = (ChipGroup)findViewById(R.id.CG_addmount_function);
        TXTL_amountmenu_Note = (TextInputLayout)findViewById(R.id.txtl_amountmenu_Note);

        //khởi tạo kết nối csdl
        billDAO = new BillDAO(this);
        billinfoDAO = new BillInfoDAO(this);
        tableFoodDAO = new TableFoodDAO(this);

        //Lấy thông tin từ bàn được chọn
        Intent intent = getIntent();
        idTable = intent.getIntExtra("idTable",0);
        idFood = intent.getIntExtra("idFood",0);

        BTN_amountmenu_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateAmount() && !validateFunction()){
                    return;
                }
                String billinfo_note = TXTL_amountmenu_Note.getEditText().getText().toString().trim();
                idBill = billDAO.getIDBillByIdTable(idTable);
                boolean chk = billinfoDAO.checkFoodExist(idBill,idFood);
                if(chk){
                    CG_amountmenu_function.setVisibility(View.VISIBLE);
                    //update số lượng món đã chọn
                    int count_old = billinfoDAO.getBillInfoById(idBill,idFood).getCount();
                    int count_new = Integer.parseInt(TXTL_amountmenu_Count.getEditText().getText().toString());
                    int idBillInfo = billinfoDAO.getBillInfoById(idBill,idFood).getId();

                    BillInfoDTO billInfoDTO = new BillInfoDTO();
                    billInfoDTO.setIdBill(idBill);
                    billInfoDTO.setIdFood(idFood);
                    billInfoDTO.setNote(billinfo_note);
                    switch (CG_amountmenu_function.getCheckedChipId()){
                        case R.id.chip_addmount_add:
                            billInfoDTO.setCount(count_old+count_new);
                            boolean updateBillInfo = billinfoDAO.updateBillInfo(idBillInfo,billInfoDTO);
                            if(updateBillInfo){
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.add_sucessful),Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.add_failed),Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.id.chip_addmount_sub:
                            billInfoDTO.setCount(count_old-count_new);
                            if(billInfoDTO.getCount() == 0){
                                //billinfoDAO.updateBillInfo(idBillInfo,billInfoDTO);
                                boolean subbillinfo = billinfoDAO.deleteBillInfo(idBillInfo);
                                if(subbillinfo){
                                    List<BillInfoDTO> list = billinfoDAO.getListBillInfo(idBill);
                                    if(list.isEmpty() || list == null) {
                                        tableFoodDAO.updateStatusTable(idTable,0);
                                    }
                                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.sub_sucessful),Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.sub_failed),Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                boolean updateBillInfo2 = billinfoDAO.updateBillInfo(idBillInfo,billInfoDTO);
                                if(updateBillInfo2){
                                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.sub_sucessful),Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.sub_failed),Toast.LENGTH_SHORT).show();
                                }
                            }
                            break;
                        case R.id.chip_addmount_cancel:
                            boolean del = billinfoDAO.deleteBillInfo(billinfoDAO.getBillInfoById(idBill,idFood).getId());
                            if(del){
                                List<BillInfoDTO> list = billinfoDAO.getListBillInfo(idBill);
                                if(list.isEmpty() || list == null) {
                                    tableFoodDAO.updateStatusTable(idTable,0);
                                }
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.delete_sucessful),Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.delete_failed),Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }

                }else {
                    int status = tableFoodDAO.getTableById(idTable).getStatus();
                    if(status == 0){
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String dateCheckIn= dateFormat.format(calendar.getTime());
                        //Thêm bảng gọi món và update tình trạng bàn
                        BillDTO billDTO = new BillDTO();

                        billDTO.setIdTable(idTable);
                        billDTO.setDateCheckIn(dateCheckIn);
                        billDTO.setStatus(0);

                        boolean addBill = billDAO.addBill(billDTO);
                        boolean updateStatusTable = tableFoodDAO.updateStatusTable(idTable,1);
                        if(!addBill || !updateStatusTable){ Toast.makeText(getApplicationContext(),getResources().getString(R.string.add_failed),Toast.LENGTH_SHORT).show(); }
                    }
                    idBill = billDAO.getIDBillByIdTable(idTable);
                    CG_amountmenu_function.setVisibility(View.GONE);
                    //thêm số lượng món nếu chưa chọn món này
                    int count = Integer.parseInt(TXTL_amountmenu_Count.getEditText().getText().toString());
                    BillInfoDTO billInfoDTO = new BillInfoDTO();
                    billInfoDTO.setIdBill(idBill);
                    billInfoDTO.setIdFood(idFood);
                    billInfoDTO.setCount(count);
                    billInfoDTO.setNote(billinfo_note);
                    boolean addBillInfo = billinfoDAO.addBillInfo(billInfoDTO);
                    if(addBillInfo){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.add_sucessful),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.add_failed),Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
            }
        });
    }
    //validate số lượng
    private boolean validateAmount(){
        String val = TXTL_amountmenu_Count.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_amountmenu_Count.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(!val.matches(("\\d+(?:\\.\\d+)?")) && Integer.parseInt(val)<0){
            TXTL_amountmenu_Count.setError("Số lượng không hợp lệ");
            return false;
        }else {
            TXTL_amountmenu_Count.setError(null);
            TXTL_amountmenu_Count.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validateFunction(){
        if (!CG_amountmenu_function.isSelectionRequired()){
            Toast.makeText(getApplicationContext(),"Hãy chọn chức năng",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}