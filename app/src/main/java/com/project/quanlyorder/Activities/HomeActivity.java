package com.project.quanlyorder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.google.android.material.navigation.NavigationView;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.project.quanlyorder.Fragments.DisplayAccountFragment;
import com.project.quanlyorder.Fragments.DisplayInfomationFragment;
import com.project.quanlyorder.Fragments.DisplayMenuFragment;
import com.project.quanlyorder.Fragments.DisplayStatisticFragment;
import com.project.quanlyorder.Fragments.DisplayTableFragment;
import com.project.quanlyorder.R;

import java.lang.reflect.Field;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    SpaceNavigationView spaceNavigationView;
    int typeAcc, idAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        //region thuộc tính bên view
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view_home);
        View view = navigationView.getHeaderView(0);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //endregion

        //hiện thị fragment home mặc định
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();
        DisplayTableFragment displayHomeFragment = new DisplayTableFragment();
        tranDisplayHome.replace(R.id.contentView, displayHomeFragment);
        tranDisplayHome.commit();

        //Khởi tạo và thiết lập bottom navigation
        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        setBottomNav();
        //spaceNavigationView.setCentreButtonColor(ContextCompat.getColor(HomeActivity.this, R.color.teal_200));

        //Lấy dữ liệu từ trang login
        Intent intent = getIntent();
        typeAcc = intent.getIntExtra("typeAcc",0);
        idAcc = intent.getIntExtra("idAcc",0);
        //xử lý toolbar và navigation
        setSupportActionBar(toolbar); //tạo toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //tạo nút mở navigation
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar
                ,R.string.opentoggle,R.string.closetoggle){
            @Override
            public void onDrawerOpened(View drawerView) {    super.onDrawerOpened(drawerView); }

            @Override
            public void onDrawerClosed(View drawerView) {    super.onDrawerClosed(drawerView); }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
//        AndroidNetworking.initialize(getApplicationContext());
    }

    //Hàm thiết lập bottom navigation
    public void setBottomNav(){
        spaceNavigationView.changeCurrentItem(-1);
        spaceNavigationView.addSpaceItem(new SpaceItem("Danh mục", R.drawable.ic_menu));
        spaceNavigationView.addSpaceItem(new SpaceItem("Thống kê", R.drawable.ic_baseline_event_24));
        spaceNavigationView.addSpaceItem(new SpaceItem("Quản lý thông tin", R.drawable.user_cog_solid));
        spaceNavigationView.addSpaceItem(new SpaceItem("Thiết lập", R.drawable.ic_baseline_person_24));
        spaceNavigationView.setSpaceBackgroundColor(ContextCompat.getColor(this, R.color.grown));
        spaceNavigationView.setCentreButtonIcon(R.drawable.ic_baseline_restaurant_menu_24);
        spaceNavigationView.setActiveSpaceItemColor(ContextCompat.getColor(this, R.color.teal_200));
        spaceNavigationView.setCentreButtonColor(ContextCompat.getColor(this, R.color.teal_700));

        spaceNavigationView.showIconOnly();
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                spaceNavigationView.changeCurrentItem(-1);
                spaceNavigationView.setCentreButtonColor(ContextCompat.getColor(HomeActivity.this, R.color.teal_200));
                fragmentManager = getSupportFragmentManager();

                DisplayTableFragment displayTABLEFragment = new DisplayTableFragment();
                Bundle bundleDisplayInfo = new Bundle();
                bundleDisplayInfo.putInt("idAccount",idAcc);
                displayTABLEFragment.setArguments(bundleDisplayInfo);
                FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();
                tranDisplayHome.replace(R.id.contentView,displayTABLEFragment);
                tranDisplayHome.commit();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex){
                    case 0:             // Menu
                        if(typeAcc != 0){
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction tranDisplayMenu = fragmentManager.beginTransaction();
                        DisplayMenuFragment displayMenuFragment = new DisplayMenuFragment();

                        tranDisplayMenu.replace(R.id.contentView,displayMenuFragment);
                        tranDisplayMenu.commit();
                        }else {
                            Toast.makeText(getApplicationContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:             // Thống kê
                        if(typeAcc != 0){
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction tranDisplayReport = fragmentManager.beginTransaction();
                        DisplayStatisticFragment displayReportFragment = new DisplayStatisticFragment();

                        tranDisplayReport.replace(R.id.contentView,displayReportFragment);
                        tranDisplayReport.commit();
                        }else {
                            Toast.makeText(getApplicationContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:             //Quản lý tài khoản
                        if(typeAcc != 0){
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction tranDisplayManager = fragmentManager.beginTransaction();
                            DisplayAccountFragment displayAccountFragment = new DisplayAccountFragment();
                            Bundle bundleDisplayInfo = new Bundle();
                            bundleDisplayInfo.putInt("current_idAccount",idAcc);
                            displayAccountFragment.setArguments(bundleDisplayInfo);
                            tranDisplayManager.replace(R.id.contentView,displayAccountFragment);
                            tranDisplayManager.commit();
                        }else {
                            Toast.makeText(getApplicationContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:             //Thiết lập chung
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction tranDisplayUserInfo = fragmentManager.beginTransaction();
                        DisplayInfomationFragment displayInfoFragment = new DisplayInfomationFragment();
                        Bundle bundleDisplayInfo = new Bundle();
                        bundleDisplayInfo.putInt("current_idAccount",idAcc);
                        bundleDisplayInfo.putInt("idAccount",0);
                        displayInfoFragment.setArguments(bundleDisplayInfo);
                        tranDisplayUserInfo.replace(R.id.contentView,displayInfoFragment);
                        tranDisplayUserInfo.commit();
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Toast.makeText(HomeActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}