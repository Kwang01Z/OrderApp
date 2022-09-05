package com.project.quanlyorder.OtherLibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.quanlyorder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDatePickerDialog extends Dialog implements View.OnClickListener{

    public final static String CHOOSE_DATE = "CHOOSE_DATE";
    public final static String CHOOSE_DATE_RANGE = "CHOOSE_DATE_RANGE";
    public interface DatePickerListener{
        public void dateEntered(String status,String date,String datebegin,String dateend);
    }

    public Context context;
    private String statusDatePick;

    private TextView tvDate, tvDateBegin, tvDateEnd;
    private Button buttonChooseDate, buttonChooseDateBegin,buttonChooseDateEnd;
    private Button buttonOK;
    private Button buttonCancel;
    private RadioGroup rgFunction;
    LinearLayout layoutChooseDate, getLayoutChooseDateRange;
    private MyDatePickerDialog.DatePickerListener datePickerListener;

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    public MyDatePickerDialog(@NonNull Context context, MyDatePickerDialog.DatePickerListener datePickerListener) {
        super(context);
        this.datePickerListener = datePickerListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_date_picker_dialog);

        tvDate = (TextView) findViewById(R.id.mydatepicker_tvDate);
        tvDateBegin = (TextView) findViewById(R.id.mydatepicker_tvDateBegin);
        tvDateEnd = (TextView) findViewById(R.id.mydatepicker_tvDateEnd);
        buttonChooseDate = (Button) findViewById(R.id.mydatepicker_btnChooseDate);
        buttonChooseDateBegin = (Button) findViewById(R.id.mydatepicker_btnChooseDateBegin);
        buttonChooseDateEnd = (Button) findViewById(R.id.mydatepicker_btnChooseDateEnd);
        buttonOK = (Button) findViewById(R.id.mydatepicker_btnok);
        buttonCancel = (Button) findViewById(R.id.mydatepicker_btncancel);
        rgFunction = (RadioGroup) findViewById(R.id.mydatepicker_rgFuntion);
        layoutChooseDate = (LinearLayout) findViewById(R.id.mydatepicker_layoutChooseDate);
        getLayoutChooseDateRange = (LinearLayout) findViewById(R.id.mydatepicker_layoutChooseDateRange);

        buttonChooseDate.setOnClickListener(this);
        buttonChooseDateBegin.setOnClickListener(this);
        buttonChooseDateEnd.setOnClickListener(this);
        buttonOK.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH)+1;
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        String strMonth;
        switch (lastSelectedMonth){
            case 10: case 11 : case 12:
                strMonth=String.valueOf(lastSelectedMonth);
                break;
            default: strMonth="0"+String.valueOf(lastSelectedMonth);
        }

        tvDate.setText(this.lastSelectedDayOfMonth + "-" + strMonth + "-" + this.lastSelectedYear);
        tvDateBegin.setText(this.lastSelectedDayOfMonth + "-" + strMonth + "-" + this.lastSelectedYear);
        tvDateEnd.setText(this.lastSelectedDayOfMonth + "-" + strMonth + "-" + this.lastSelectedYear);
        this.statusDatePick = CHOOSE_DATE;
        rgFunction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.mydatepicker_radChooseDate:
                        layoutChooseDate.setVisibility(View.VISIBLE);
                        getLayoutChooseDateRange.setVisibility(View.GONE);
                        statusDatePick = CHOOSE_DATE;
                        break;
                    case R.id.mydatepicker_radChooseDateRanger:
                        layoutChooseDate.setVisibility(View.GONE);
                        getLayoutChooseDateRange.setVisibility(View.VISIBLE);
                        statusDatePick = CHOOSE_DATE_RANGE;
                        break;
                }
            }
        });

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        DatePickerDialog datePickerDialog;
        switch (id){
            case R.id.mydatepicker_btnChooseDate:
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String strMonth;
                        switch (month){
                            case 10: case 11 : case 12:
                                strMonth=String.valueOf(month);
                                break;
                            default: strMonth="0"+String.valueOf(month);
                        }
                        String strDate= dayOfMonth+"-"+strMonth+"-"+year;
                        tvDate.setText(strDate);
                        lastSelectedYear = year;
                        lastSelectedMonth = month;
                        lastSelectedDayOfMonth = dayOfMonth;
                    }
                };
                 datePickerDialog = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                         dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
                 datePickerDialog.show();
                break;
            case R.id.mydatepicker_btnChooseDateBegin:
                DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String strMonth;
                        switch (month){
                            case 10: case 11 : case 12:
                                strMonth=String.valueOf(month);
                                break;
                            default: strMonth="0"+String.valueOf(month);
                        }
                        String strDate= dayOfMonth+"-"+strMonth+"-"+year;
                        tvDateBegin.setText(strDate);
                        lastSelectedYear = year;
                        lastSelectedMonth = month;
                        lastSelectedDayOfMonth = dayOfMonth;
                    }
                };
                datePickerDialog = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        dateSetListener2, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
                datePickerDialog.show();
                break;
            case R.id.mydatepicker_btnChooseDateEnd:
                DatePickerDialog.OnDateSetListener dateSetListener3 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String strMonth;
                        switch (month){
                            case 10: case 11 : case 12:
                                strMonth=String.valueOf(month);
                                break;
                            default: strMonth="0"+String.valueOf(month);
                        }
                        String strDate= dayOfMonth+"-"+strMonth+"-"+year;
                        tvDateEnd.setText(strDate);
                        lastSelectedYear = year;
                        lastSelectedMonth = month;
                        lastSelectedDayOfMonth = dayOfMonth;
                    }
                };
                datePickerDialog = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        dateSetListener3, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
                datePickerDialog.show();
                break;
            case R.id.mydatepicker_btnok:
                String txtDate = this.tvDate.getText().toString().trim();
                String txtDateBegin = this.tvDateBegin.getText().toString().trim();
                String txtDateEnd = this.tvDateEnd.getText().toString().trim();
                Log.e("dateaa",txtDate);
                Log.e("begin",txtDateBegin);
                Log.e("end",txtDateEnd);
                this.dismiss(); // Close Dialog
                if(this.datePickerListener!= null)  {
                    this.datePickerListener.dateEntered(statusDatePick,txtDate,txtDateBegin,txtDateEnd);
                }
                break;
            case R.id.mydatepicker_btncancel:
                this.dismiss();
                break;
        }
    }
}