package com.project.quanlyorder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.project.quanlyorder.DAO.AccountDAO;
import com.project.quanlyorder.DAO.UserInfoDAO;
import com.project.quanlyorder.DTO.AccountDTO;
import com.project.quanlyorder.DTO.UserInfoDTO;
import com.project.quanlyorder.Database.CreateDatabase;
import com.project.quanlyorder.R;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        fragmentManager = getSupportFragmentManager();
        final PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataForOnBoarding());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_ob, paperOnboardingFragment);
        fragmentTransaction.commit();
        CreateDatabase createDatabase = new CreateDatabase(this);
        createDatabase.open();

    }

    private ArrayList<PaperOnboardingPage> getDataForOnBoarding() {
        PaperOnboardingPage src1 = new PaperOnboardingPage("Quản lý order và thanh toán", "Quản lý order tại nhà hàng thông qua App, tinh tiền, in hóa đơn nhanh chóng, quản lý hóa đơn, theo dõi báo cáo mọi lúc.", Color.parseColor("#ffffff"),
                R.drawable.orderslide1,R.drawable.circle_drawable);
        PaperOnboardingPage src2 = new PaperOnboardingPage("Đơn giản & Dễ sử dụng", "Giao diện đơn giản, thân thiện và thông minh. Nhân viên chỉ mất vài phút làm quen, dễ sử dụng.", Color.parseColor("#ffffff"),
                R.drawable.orderslide2,R.drawable.circle_drawable);
        PaperOnboardingPage src3 = new PaperOnboardingPage("Tiết kiệm chi phí", "Miễn phí cài đặt, triển khai, nâng cấp và hỗ trợ. Rẻ hơn một ly trà đá mỗi ngày.", Color.parseColor("#ffffff"),
                R.drawable.orderslide3,R.drawable.circle_drawable);

        ArrayList<PaperOnboardingPage> element = new ArrayList<>();
        element.add(src1);
        element.add(src2);
        element.add(src3);
        return element;
    }

    //chuyển sang trang đăng nhập
    public void callLoginFromWel(View view)
    {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.btn_login),"transition_login");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this,pairs);
            startActivity(intent,options.toBundle());
        }
        else {
            startActivity(intent);
            Toast toast =  Toast.makeText(WelcomeActivity.this, "Phiên bản người dùng không hỗ trợ!", Toast.LENGTH_SHORT);
            //toast.setGravity(Gravity.TOP | Gravity.CENTER, 20, 30);
            toast.show();
        }
    }

    // chuyển sang trang đăng ký
    public void callSignUpFromWel(View view)
    {
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.btn_signup),"transition_signup");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this,pairs);
            startActivity(intent,options.toBundle());
        }
        else {
            startActivity(intent);
        }
    }
}