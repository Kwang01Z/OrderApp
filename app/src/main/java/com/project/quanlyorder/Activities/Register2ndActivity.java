package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.project.quanlyorder.DAO.AccountDAO;
import com.project.quanlyorder.DAO.UserInfoDAO;
import com.project.quanlyorder.DTO.AccountDTO;
import com.project.quanlyorder.DTO.UserInfoDTO;
import com.project.quanlyorder.R;

import java.util.Calendar;

public class Register2ndActivity extends AppCompatActivity {

    RadioGroup RG_signup_Gender;
    DatePicker DT_signup_Birthday;
    Button BTN_signup_ok;
    EditText edUserPos;
    String fullName,userName,eMail,phoneNum,passWord,gender,userPosition;
    AccountDAO accountDAO;
    UserInfoDAO userInfoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register2nd_layout);
        //lấy đối tượng view
        RG_signup_Gender = (RadioGroup)findViewById(R.id.rg_signup_Gender);
        DT_signup_Birthday = (DatePicker)findViewById(R.id.dt_signup_Birthday);
        BTN_signup_ok = (Button)findViewById(R.id.btn_signup_ok);
        edUserPos = (EditText)findViewById(R.id.edUserPosition);

        //lấy dữ liệu từ bundle của register1
        Bundle bundle = getIntent().getBundleExtra(RegisterActivity.BUNDLE);
        if(bundle != null) {
            fullName = bundle.getString("FullName");
            userName = bundle.getString("UserName");
            eMail = bundle.getString("Email");
            phoneNum = bundle.getString("PhoneNum");
            passWord = bundle.getString("PassWord");
        }

        //khởi tạo kết nối csdl
        accountDAO = new AccountDAO(this);
        userInfoDAO = new UserInfoDAO(this);


        BTN_signup_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateAge() | !validateGender()){
                    return;
                }

                //lấy các thông tin còn lại
                switch (RG_signup_Gender.getCheckedRadioButtonId()){
                    case R.id.rd_signup_Man:
                        gender = getResources().getString(R.string.man); break;
                    case R.id.rd_signup_Woman:
                        gender = getResources().getString(R.string.woman); break;
                    case R.id.rd_signup_Diff:
                        gender = getResources().getString(R.string.difference); break;
                }
                String birthday = DT_signup_Birthday.getDayOfMonth() + "/" + (DT_signup_Birthday.getMonth() + 1)
                        +"/"+DT_signup_Birthday.getYear();

                //Thêm dữ liệu vào database
                AccountDTO accountDTO = new AccountDTO();
                accountDTO.setUserName(userName);
                accountDTO.setPassWord(passWord);
                accountDTO.setTypeAcc(1);

                boolean addAcc = accountDAO.addAccount(accountDTO);
                if(addAcc){
                    Log.e("---","Them tai khoan thanh cong");
                    int idAcc = accountDAO.getIdByUserName(userName);
                    UserInfoDTO userInfoDTO = new UserInfoDTO();
                    userInfoDTO.setIdAcc(idAcc);
                    userInfoDTO.setFullName(fullName);
                    userInfoDTO.setGender(gender);
                    userInfoDTO.setBirthday(birthday);
                    userInfoDTO.setEmail(eMail);
                    userInfoDTO.setPhoneNumber(phoneNum);
                    userInfoDTO.setUserPosition(edUserPos.getText().toString());

                    boolean addUsInfo = userInfoDAO.addUserInfo(userInfoDTO);
                    if(addUsInfo){
                        Toast.makeText(Register2ndActivity.this,getResources().getString(R.string.add_sucessful),Toast.LENGTH_SHORT).show();
                        callLoginFromRegister();
                    } else {
                        Toast.makeText(Register2ndActivity.this,getResources().getString(R.string.add_failed),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    //Hàm quay về màn hình trước
    public void backFromRegister2nd(View view){
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    //Hàm chuyển màn hình khi hoàn thành
    public void callLoginFromRegister(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //region Validate field
    private boolean validateGender(){
        if(RG_signup_Gender.getCheckedRadioButtonId() == -1){
            Toast.makeText(this,getResources().getString(R.string.let_choose_gender),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateAge(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = DT_signup_Birthday.getYear();
        int isAgeValid = currentYear - userAge;

        if(isAgeValid < 16){
            Toast.makeText(this,getResources().getString(R.string.not_enough_old),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    //endregion
}