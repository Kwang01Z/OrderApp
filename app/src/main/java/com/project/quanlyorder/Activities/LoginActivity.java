package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.project.quanlyorder.DAO.AccountDAO;
import com.project.quanlyorder.R;

public class LoginActivity extends AppCompatActivity {

    Button BTN_login_Login, BTN_login_noAcc;
    TextInputLayout TXTL_login_Username, TXTL_login_Password;
    CheckBox chkbox_Savelog;
    AccountDAO accountDAO;
    SharedPreferences sh_chkSavelog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //thuộc tính view
        TXTL_login_Username = (TextInputLayout)findViewById(R.id.txtl_login_Username);
        TXTL_login_Password = (TextInputLayout)findViewById(R.id.txtl_login_Password);
        BTN_login_Login = (Button)findViewById(R.id.btn_login_Login);
        BTN_login_noAcc = (Button)findViewById(R.id.btn_login_noAcc);
        chkbox_Savelog = (CheckBox)findViewById(R.id.chkbox_login_Savelog);

        accountDAO = new AccountDAO(this);
        BTN_login_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateUserName() | !validatePassWord()){
                    return;
                }
                String txtUsername = TXTL_login_Username.getEditText().getText().toString();
                String txtPassword = TXTL_login_Password.getEditText().getText().toString();
                int checkExist = accountDAO.CheckExist(txtUsername,txtPassword);


                if(checkExist != 0){
                    int typeAcc = accountDAO.getTypeAcc(checkExist);
                    int idAcc = accountDAO.getIdByUserName(txtUsername);

                    //gửi dữ liệu user qua trang chủ
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    intent.putExtra("idAcc",idAcc);
                    intent.putExtra("typeAcc",typeAcc);

                    //Thiết lập lưu đăng nhập
                    boolean chk_savelog_state = chkbox_Savelog.isChecked();
                    if (chk_savelog_state){
                        sh_chkSavelog = getSharedPreferences("last_chk", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sh_chkSavelog.edit();
                        editor2.putBoolean("last_chk",true);
                        editor2.commit();
                    }
                    else {
                        sh_chkSavelog = getSharedPreferences("last_chk", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sh_chkSavelog.edit();
                        editor2.putBoolean("last_chk",false);
                        editor2.commit();
                    }
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Đăng nhập thất bại!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        BTN_login_noAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
        //Set account nếu lưu đăng nhập trước
        sh_chkSavelog = getSharedPreferences("last_chk", Context.MODE_PRIVATE);
        Boolean last_chk = sh_chkSavelog.getBoolean("last_chk",false);
        if (last_chk) {
            SharedPreferences sh2 = getSharedPreferences("LastAcc", MODE_PRIVATE);

            String username = sh2.getString("UserName", "");
            String passwd = sh2.getString("PassWord", "");
            TXTL_login_Username.getEditText().setText(username);
            TXTL_login_Password.getEditText().setText(passwd);
            chkbox_Savelog.setChecked(true);
        } else {
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sh_lastAcc = getSharedPreferences("LastAcc", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh_lastAcc.edit();

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("UserName", TXTL_login_Username.getEditText().getText().toString());
        myEdit.putString("PassWord", TXTL_login_Password.getEditText().getText().toString());
        myEdit.commit();

    }

    //Hàm quay lại màn hình chính
    public void backFromLogin(View view)
    {
        Intent intent = new Intent(getApplicationContext(),WelcomeActivity.class);
        //tạo animation cho thành phần
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.layoutLogin),"transition_login");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
            startActivity(intent,options.toBundle());
        }
        else {
            startActivity(intent);
        }
    }

    //region Validate field
    private boolean validateUserName(){
        String val = TXTL_login_Username.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_login_Username.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_login_Username.setError(null);
            TXTL_login_Username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassWord(){
        String val = TXTL_login_Password.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_login_Password.setError(getResources().getString(R.string.not_empty));
            return false;
        }else{
            TXTL_login_Password.setError(null);
            TXTL_login_Password.setErrorEnabled(false);
            return true;
        }
    }
    //endregion
}