package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.project.quanlyorder.DAO.AccountDAO;
import com.project.quanlyorder.R;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Button BTN_signup_next;
    TextView TXT_signup_title;
    TextInputLayout TXTL_signup_FullName, TXTL_signup_UserName, TXTL_signup_Email, TXTL_signup_PhoneNum, TXTL_signup_Password, TXTL_signup_rePassword;
    public static final String BUNDLE = "BUNDLE";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 6 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        //region lấy đối tượng view
        BTN_signup_next = (Button)findViewById(R.id.btn_signup_next);
        TXT_signup_title = (TextView)findViewById(R.id.txt_signup_title);
        TXTL_signup_FullName = (TextInputLayout)findViewById(R.id.txtl_signup_FullName);
        TXTL_signup_UserName = (TextInputLayout)findViewById(R.id.txtl_signup_UserName);
        TXTL_signup_Email = (TextInputLayout)findViewById(R.id.txtl_signup_Email);
        TXTL_signup_PhoneNum = (TextInputLayout)findViewById(R.id.txtl_signup_PhoneNum);
        TXTL_signup_Password = (TextInputLayout)findViewById(R.id.txtl_signup_Password);
        TXTL_signup_rePassword = (TextInputLayout)findViewById(R.id.txtl_signup_rePassword);
        //endregion

        BTN_signup_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiểm tra validate false => phải thỏa đk validate
                if(!validateFullName() | !validateUserName() | !validateEmail() | !validatePhone() | !validatePassWord()){
                    return;
                }
                String fullName = TXTL_signup_FullName.getEditText().getText().toString();
                String userName = TXTL_signup_UserName.getEditText().getText().toString();
                String eMail = TXTL_signup_Email.getEditText().getText().toString();
                String phoneNum = TXTL_signup_PhoneNum.getEditText().getText().toString();
                String passWord = TXTL_signup_Password.getEditText().getText().toString();

                byBundleNextSignupScreen(fullName,userName,eMail,phoneNum,passWord);
            }
        });

    }

    public void backFromRegister(View view) {
        Intent intent = new Intent(getApplicationContext(),WelcomeActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.layoutRegister),"transition_signup");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
        startActivity(intent,options.toBundle());
    }

    //truyền dữ liệu qua trang đk thứ 2 bằng bundle
    public void byBundleNextSignupScreen(String FullName, String UserName, String Email, String PhoneNum, String PassWord){

        Intent intent = new Intent(getApplicationContext(),Register2ndActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("FullName",FullName);
        bundle.putString("UserName",UserName);
        bundle.putString("Email",Email);
        bundle.putString("PhoneNum",PhoneNum);
        bundle.putString("PassWord",PassWord);
        intent.putExtra(BUNDLE,bundle);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //region Validate field
    private boolean validateFullName(){
        String val = TXTL_signup_FullName.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_signup_FullName.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_signup_FullName.setError(null);
            TXTL_signup_FullName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName(){
        String val = TXTL_signup_UserName.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,50}\\z";

        if(val.isEmpty()){
            TXTL_signup_UserName.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(val.length()>50){
            TXTL_signup_UserName.setError(getResources().getString(R.string.enterAcc_errLength));
            return false;
        }else if(!val.matches(checkspaces)){
            TXTL_signup_UserName.setError(getResources().getString(R.string.not_space));
            return false;
        }else if((new AccountDAO(this).CheckExist(val))!=0){
            TXTL_signup_UserName.setError(getResources().getString(R.string.acc_exist));
            return false;
        }
        else {
            TXTL_signup_UserName.setError(null);
            TXTL_signup_UserName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(){
        String val = TXTL_signup_Email.getEditText().getText().toString().trim();
        String checkspaces = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if(val.isEmpty()){
            TXTL_signup_Email.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(!val.matches(checkspaces)){
            TXTL_signup_Email.setError(getResources().getString(R.string.enterEmail_failMatch));
            return false;
        }
        else {
            TXTL_signup_Email.setError(null);
            TXTL_signup_Email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone(){
        String val = TXTL_signup_PhoneNum.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_signup_PhoneNum.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(val.length() != 10){
            TXTL_signup_PhoneNum.setError(getResources().getString(R.string.enterPhone_errLength));
            return false;
        }
        else {
            TXTL_signup_PhoneNum.setError(null);
            TXTL_signup_PhoneNum.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassWord(){
        String val = TXTL_signup_Password.getEditText().getText().toString().trim();
        String val2 = TXTL_signup_rePassword.getEditText().getText().toString().trim();

        if(val.isEmpty()||val2.isEmpty()){
            TXTL_signup_Password.setError(getResources().getString(R.string.not_empty));
            return false;
        }
        else if(!val.equals(val2)){
            TXTL_signup_rePassword.setError(getResources().getString(R.string.rePass_fail));
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(val).matches()){
            TXTL_signup_Password.setError(getResources().getString(R.string.enterPass_failMatch));
            return false;
        }
        else {
            TXTL_signup_Password.setError(null);
            TXTL_signup_Password.setErrorEnabled(false);
            TXTL_signup_rePassword.setError(null);
            TXTL_signup_rePassword.setErrorEnabled(false);
            return true;
        }
    }
    //endregion
}