package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.quanlyorder.CustomAdapter.AdapterDisplayPayment;
import com.project.quanlyorder.DAO.BillDAO;
import com.project.quanlyorder.DAO.BillInfoDAO;
import com.project.quanlyorder.DAO.CheckDAO;
import com.project.quanlyorder.DAO.FoodDAO;
import com.project.quanlyorder.DAO.SaleDAO;
import com.project.quanlyorder.DAO.TableFoodDAO;
import com.project.quanlyorder.DTO.BillDTO;
import com.project.quanlyorder.DTO.BillInfoDTO;
import com.project.quanlyorder.DTO.CheckDTO;
import com.project.quanlyorder.DTO.FoodDTO;
import com.project.quanlyorder.OtherLibrary.InputNoteDialog;
import com.project.quanlyorder.OtherLibrary.InputNoteDialog.*;
import com.project.quanlyorder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView IMG_payment_backbtn;
    TextView TXT_payment_TableName, TXT_payment_DateCheckout, TXT_payment_MoneySum;
    Button BTN_payment_Payment;
    ImageButton BTN_payment_Note;
    GridView gvDisplayPayment;
    BillDAO billDAO;
    TableFoodDAO tablefoodDAO;
    CheckDAO checkDAO;
    BillInfoDAO billInfoDAO;
    List<BillInfoDTO> billInfoDTOList;
    AdapterDisplayPayment adapterDisplayPayment;
    long moneysum = 0;
    int idTable,idBill,idAcc;
    FragmentManager fragmentManager;
    String txtnote = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);

        //region thuộc tính view
        gvDisplayPayment= (GridView)findViewById(R.id.gvDisplayPayment);

        IMG_payment_backbtn = (ImageView)findViewById(R.id.img_payment_backbtn);
        TXT_payment_TableName = (TextView)findViewById(R.id.txt_payment_TableName);
        TXT_payment_DateCheckout = (TextView)findViewById(R.id.txt_payment_DateCheckOut);
        TXT_payment_MoneySum = (TextView)findViewById(R.id.txt_payment_MoneyTotal);
        BTN_payment_Payment = (Button)findViewById(R.id.btn_payment_Check);
        BTN_payment_Note = (ImageButton) findViewById(R.id.btn_payment_note);
        //endregion

        //khởi tạo kết nối csdl
        billDAO = new BillDAO(getApplicationContext());
        checkDAO = new CheckDAO(getApplicationContext());
        tablefoodDAO = new TableFoodDAO(getApplicationContext());
        billInfoDAO = new BillInfoDAO(getApplicationContext());

        fragmentManager = getSupportFragmentManager();

        //lấy data từ mã bàn đc chọn
        Intent intent = getIntent();
        idTable = intent.getIntExtra("idTable",0);
        idAcc = intent.getIntExtra("idAcc",0);
        String nameTable = tablefoodDAO.getTableById(idTable).getName();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateCheckOut= dateFormat.format(calendar.getTime());
        idBill =  billDAO.getIDBillByIdTable(idTable);
        TXT_payment_TableName.setText(nameTable);
        TXT_payment_DateCheckout.setText(dateCheckOut);

        //ktra mã bàn tồn tại thì hiển thị
        if(idBill !=0 ){
            DisplayBeforeCheck();
            for (int i=0;i<billInfoDTOList.size();i++){
                int count = billInfoDTOList.get(i).getCount();
                FoodDAO foodDAO = new FoodDAO(getApplicationContext());
                FoodDTO foodDTO = foodDAO.getFoodById(billInfoDTOList.get(i).getIdFood());
                SaleDAO saleDAO = new SaleDAO(getApplicationContext());
                int sale = saleDAO.getSaleValueById(foodDTO.getIdSale());
                int price = foodDTO.getPrice();
                moneysum += (count * price)*(100-sale)/100;

            }
            String unitMoney = getApplicationContext().getString(R.string.unitMoney);


            TXT_payment_MoneySum.setText(String.valueOf(moneysum) + unitMoney);
        }
        BTN_payment_Note.setOnClickListener(this);
        BTN_payment_Payment.setOnClickListener(this);
        IMG_payment_backbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

            case R.id.btn_payment_note:
                InputNoteDialog.NoteListener listener = new InputNoteDialog.NoteListener() {
                    @Override
                    public void noteEntered(String note) {
                        txtnote = note;
                    }
                };
                final InputNoteDialog dialog = new InputNoteDialog(this,listener);
                dialog.show();
                break;
            case R.id.btn_payment_Check:
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateCheckout= dateFormat.format(calendar.getTime());
                CheckDTO checkDTO = new CheckDTO();
                checkDTO.setIdBill(idBill);
                checkDTO.setIdStaff(idAcc);
                checkDTO.setTotal((int) moneysum);
                checkDTO.setDateCheckOut(dateCheckout);
                checkDTO.setIdSale(0);
                checkDTO.setPaid((int)moneysum);
                checkDTO.setNote(txtnote);
                boolean addCheck = checkDAO.addCheck(checkDTO);
                if(addCheck){
                    tablefoodDAO.updateStatusTable(idTable,0);
                    billDAO.updateStatus(idBill,1);
                    billDAO.updateSummoneyBill(idBill,(int) moneysum);
                    TXT_payment_MoneySum.setText("0 VNĐ");
                    DisplayBeforeCheck();
                    Toast.makeText(getApplicationContext(),"Thanh toán thành công!",Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(getApplicationContext(),"Lỗi thanh toán!",Toast.LENGTH_SHORT);
                }
                break;

            case R.id.img_payment_backbtn:
                finish();
                break;
        }
    }

    //hiển thị data lên gridview
    private void DisplayBeforeCheck(){
        idBill =  billDAO.getIDBillByIdTable(idTable);
        billInfoDTOList = billInfoDAO.getListBillInfo(idBill);
        adapterDisplayPayment = new AdapterDisplayPayment(this,R.layout.custom_layout_paymentmenu,billInfoDTOList);
        gvDisplayPayment.setAdapter(adapterDisplayPayment);
        adapterDisplayPayment.notifyDataSetChanged();
    }
}