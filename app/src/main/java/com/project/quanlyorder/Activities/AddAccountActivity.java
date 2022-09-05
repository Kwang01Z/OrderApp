package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.project.quanlyorder.DAO.AccountDAO;
import com.project.quanlyorder.DAO.UserInfoDAO;
import com.project.quanlyorder.DTO.AccountDTO;
import com.project.quanlyorder.DTO.UserInfoDTO;
import com.project.quanlyorder.R;

import java.util.Calendar;
import java.util.regex.Pattern;

public class AddAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 6 characters
                    "$");

    ImageView IMG_addaccount_back;
    TextView TXT_addaccount_title;
    EditText Edit_addaccount_UserPos;
    TextInputLayout TXTL_addaccount_FullName, TXTL_addaccount_UserName, TXTL_addaccount_Email, TXTL_addaccount_PhoneNumber, TXTL_addaccount_PassWord,TXTL_addaccount_rePassWord;
    RadioGroup RG_addaccount_Gender,rg_addaccount_TypeAcc;
    RadioButton RD_addaccount_Man,RD_addaccount_Woman,RD_addaccount_Diff,rd_addaccount_Manager,rd_addaccount_Staff;
    DatePicker DT_addaccount_Birthday;
    Button BTN_addaccount_AddAccount;
    AccountDAO accountDAO;
    UserInfoDAO userInfoDAO;
    String fullName,userName,eMail,phoneNumber,passWord,gender,birthday,posAcc;
    int idAcc = 0,typeAcc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account_layout);

        //region Lấy đối tượng trong view
        Edit_addaccount_UserPos = (EditText)findViewById(R.id.txt_addaccount_UserPosition);
        TXT_addaccount_title = (TextView)findViewById(R.id.txt_addaccount_title);
        IMG_addaccount_back = (ImageView)findViewById(R.id.img_addaccount_back);
        TXTL_addaccount_FullName = (TextInputLayout)findViewById(R.id.txtl_addaccount_FullName);
        TXTL_addaccount_UserName = (TextInputLayout)findViewById(R.id.txtl_addaccount_UserName);
        TXTL_addaccount_Email = (TextInputLayout)findViewById(R.id.txtl_addaccount_Email);
        TXTL_addaccount_PhoneNumber = (TextInputLayout)findViewById(R.id.txtl_addaccount_PhoneNumber);
        TXTL_addaccount_PassWord = (TextInputLayout)findViewById(R.id.txtl_addaccount_PassWord);
        RG_addaccount_Gender = (RadioGroup)findViewById(R.id.rg_addaccount_Gender);
        rg_addaccount_TypeAcc = (RadioGroup)findViewById(R.id.rg_addaccount_TypeAcc);
        RD_addaccount_Man = (RadioButton)findViewById(R.id.rd_addaccount_Man);
        RD_addaccount_Woman = (RadioButton)findViewById(R.id.rd_addaccount_Woman);
        RD_addaccount_Diff = (RadioButton)findViewById(R.id.rd_addaccount_Diff);
        rd_addaccount_Manager = (RadioButton)findViewById(R.id.rd_addaccount_Manager);
        rd_addaccount_Staff = (RadioButton)findViewById(R.id.rd_addaccount_Staff);
        DT_addaccount_Birthday = (DatePicker)findViewById(R.id.dt_addaccount_Birthday);
        BTN_addaccount_AddAccount = (Button)findViewById(R.id.btn_addaccount_AddAccount);
        TXTL_addaccount_rePassWord = (TextInputLayout)findViewById(R.id.txtl_addaccount_rePassWord);

        //endregion

        accountDAO = new AccountDAO(this);
        userInfoDAO = new UserInfoDAO(this);

        //region Hiển thị trang sửa nếu được chọn từ context menu sửa
        idAcc = getIntent().getIntExtra("idAccount",0);   //lấy idAcc từ display account
        if(idAcc != 0){
            TXT_addaccount_title.setText(getResources().getString(R.string.editAcc));
            UserInfoDTO userInfoDTO = userInfoDAO.getUserInfoById(idAcc);
            AccountDTO accountDTO = accountDAO.getAccountById(idAcc);

            //Hiển thị thông tin từ csdl
            TXTL_addaccount_FullName.getEditText().setText(userInfoDTO.getFullName());
            TXTL_addaccount_UserName.getEditText().setText(accountDTO.getUserName());
            TXTL_addaccount_Email.getEditText().setText(userInfoDTO.getEmail());
            TXTL_addaccount_PhoneNumber.getEditText().setText(userInfoDTO.getPhoneNumber());
            TXTL_addaccount_PassWord.getEditText().setText(accountDTO.getPassWord());
            Edit_addaccount_UserPos.setText(userInfoDTO.getUserPosition());

            //Hiển thị giới tính từ csdl
            String genderUser = userInfoDTO.getGender();
            if(genderUser.equals(getResources().getString(R.string.man))){
                RD_addaccount_Man.setChecked(true);
            }else if (genderUser.equals(getResources().getString(R.string.woman))){
                RD_addaccount_Woman.setChecked(true);
            }else {
                RD_addaccount_Diff.setChecked(true);
            }

            if(accountDTO.getTypeAcc() == 1){
                rd_addaccount_Manager.setChecked(true);
            }else {
                rd_addaccount_Staff.setChecked(true);
            }

            //Hiển thị ngày sinh từ csdl
            String birthdayUser = userInfoDTO.getBirthday();
            String[] items = birthdayUser.split("/");
            int day = Integer.parseInt(items[0]);
            int month = Integer.parseInt(items[1]) - 1;
            int year = Integer.parseInt(items[2]);
            DT_addaccount_Birthday.updateDate(year,month,day);
            BTN_addaccount_AddAccount.setText(getResources().getString(R.string.editAcc));
        }
        //endregion

        BTN_addaccount_AddAccount.setOnClickListener(this);
        IMG_addaccount_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        String funct = null;
        boolean result = false;
        switch (id){
            case R.id.btn_addaccount_AddAccount:
                if( !validateAge() | !validateEmail() | !validateFullName() | !validateGender() | !validatePassWord() |
                        !validatePermission() | !validatePhone() | !validateUserName()){
                    return;
                }
                //Lấy dữ liệu từ view
                fullName = TXTL_addaccount_FullName.getEditText().getText().toString();
                userName = TXTL_addaccount_UserName.getEditText().getText().toString();
                eMail = TXTL_addaccount_Email.getEditText().getText().toString();
                phoneNumber = TXTL_addaccount_PhoneNumber.getEditText().getText().toString();
                passWord = TXTL_addaccount_PassWord.getEditText().getText().toString();
                posAcc = Edit_addaccount_UserPos.getText().toString();

                switch (RG_addaccount_Gender.getCheckedRadioButtonId()){
                    case R.id.rd_addaccount_Man: gender = getResources().getString(R.string.man); break;
                    case R.id.rd_addaccount_Woman: gender = getResources().getString(R.string.woman); break;
                    case R.id.rd_addaccount_Diff: gender = getResources().getString(R.string.difference); break;
                }
                switch (rg_addaccount_TypeAcc.getCheckedRadioButtonId()){
                    case R.id.rd_addaccount_Manager: typeAcc = 1; break;
                    case R.id.rd_addaccount_Staff: typeAcc = 0; break;
                }

                birthday = DT_addaccount_Birthday.getDayOfMonth() + "/" + (DT_addaccount_Birthday.getMonth() + 1)
                        +"/"+DT_addaccount_Birthday.getYear();

                //truyền dữ liệu vào obj nhanvienDTO
                AccountDTO accountDTO = new AccountDTO();
                accountDTO.setUserName(userName);
                accountDTO.setPassWord(passWord);
                accountDTO.setTypeAcc(typeAcc);


                if(idAcc != 0){
                    boolean addAcc = accountDAO.updateAccount(idAcc,accountDTO);
                    if (addAcc){
                        UserInfoDTO userInfoDTO = new UserInfoDTO();
                        userInfoDTO.setIdAcc(accountDAO.getIdByUserName(userName));
                        userInfoDTO.setFullName(fullName);
                        userInfoDTO.setGender(gender);
                        userInfoDTO.setEmail(eMail);
                        userInfoDTO.setPhoneNumber(phoneNumber);
                        userInfoDTO.setBirthday(birthday);
                        userInfoDTO.setUserPosition(posAcc);
                        boolean addUsInfo = userInfoDAO.updateUserInfo(accountDAO.getIdByUserName(userName),userInfoDTO);
                        if (addUsInfo){
                            funct = "updateAccount";
                            result = true;
                        }
                    }

                }else {
                    if(!validateUserName2()){
                        return;
                    }
                    boolean addAcc = accountDAO.addAccount(accountDTO);
                    if (addAcc){
                        UserInfoDTO userInfoDTO = new UserInfoDTO();
                        userInfoDTO.setIdAcc(accountDAO.getIdByUserName(userName));
                        userInfoDTO.setFullName(fullName);
                        userInfoDTO.setGender(gender);
                        userInfoDTO.setEmail(eMail);
                        userInfoDTO.setPhoneNumber(phoneNumber);
                        userInfoDTO.setBirthday(birthday);
                        userInfoDTO.setUserPosition(posAcc);
                        boolean addUsInfo = userInfoDAO.addUserInfo(userInfoDTO);
                        if (addUsInfo){
                            funct = "addAccount";
                            result = true;
                        }
                    }
                }
                //Thêm, sửa nv dựa theo obj nhanvienDTO
                Intent intent = new Intent();
                intent.putExtra("result",result);
                intent.putExtra("function",funct);
                setResult(RESULT_OK,intent);
                finish();
                break;

            case R.id.img_addaccount_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
        }
    }

    //region Validate field
    private boolean validateFullName(){
        String val = TXTL_addaccount_FullName.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_addaccount_FullName.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_addaccount_FullName.setError(null);
            TXTL_addaccount_FullName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName(){
        String val = TXTL_addaccount_UserName.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,50}\\z";

        if(val.isEmpty()){
            TXTL_addaccount_UserName.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(val.length()>50){
            TXTL_addaccount_UserName.setError(getResources().getString(R.string.enterAcc_errLength));
            return false;
        }else if(!val.matches(checkspaces)){
            TXTL_addaccount_UserName.setError(getResources().getString(R.string.not_space));
            return false;
        }
        else {
            TXTL_addaccount_UserName.setError(null);
            TXTL_addaccount_UserName.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateUserName2(){
        String val = TXTL_addaccount_UserName.getEditText().getText().toString().trim();
        if((new AccountDAO(this).CheckExist(val))!=0){
            TXTL_addaccount_UserName.setError(getResources().getString(R.string.acc_exist));
            return false;
        } else {
            TXTL_addaccount_UserName.setError(null);
            TXTL_addaccount_UserName.setErrorEnabled(false);
            return true;
        }
    }


    private boolean validateEmail(){
        String val = TXTL_addaccount_Email.getEditText().getText().toString().trim();
        String checkspaces = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if(val.isEmpty()){
            TXTL_addaccount_Email.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(!val.matches(checkspaces)){
            TXTL_addaccount_Email.setError(getResources().getString(R.string.enterEmail_failMatch));
            return false;
        }
        else {
            TXTL_addaccount_Email.setError(null);
            TXTL_addaccount_Email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone(){
        String val = TXTL_addaccount_PhoneNumber.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_addaccount_PhoneNumber.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(val.length() != 10){
            TXTL_addaccount_PhoneNumber.setError(getResources().getString(R.string.enterPhone_errLength));
            return false;
        }
        else {
            TXTL_addaccount_PhoneNumber.setError(null);
            TXTL_addaccount_PhoneNumber.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassWord(){
        String val = TXTL_addaccount_PassWord.getEditText().getText().toString().trim();
        String val2 = TXTL_addaccount_rePassWord.getEditText().getText().toString().trim();

        if(val.isEmpty()||val2.isEmpty()){
            TXTL_addaccount_PassWord.setError(getResources().getString(R.string.not_empty));
            return false;
        }
        else if(!val.equals(val2)){
            TXTL_addaccount_rePassWord.setError(getResources().getString(R.string.rePass_fail));
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(val).matches()){
            TXTL_addaccount_PassWord.setError(getResources().getString(R.string.enterPass_failMatch));
            return false;
        }
        else {
            TXTL_addaccount_PassWord.setError(null);
            TXTL_addaccount_PassWord.setErrorEnabled(false);
            TXTL_addaccount_rePassWord.setError(null);
            TXTL_addaccount_rePassWord.setErrorEnabled(false);
            return true;
        }
    }


    private boolean validatePermission(){
        if(rg_addaccount_TypeAcc.getCheckedRadioButtonId() == -1){
            Toast.makeText(this,getResources().getString(R.string.choose_permission),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateGender(){
        if(RG_addaccount_Gender.getCheckedRadioButtonId() == -1){
            Toast.makeText(this,getResources().getString(R.string.let_choose_gender),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateAge(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = DT_addaccount_Birthday.getYear();
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