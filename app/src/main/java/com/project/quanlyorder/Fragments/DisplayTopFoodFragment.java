package com.project.quanlyorder.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.CustomAdapter.AdapterDisplayTopFood;
import com.project.quanlyorder.DAO.BillInfoDAO;
import com.project.quanlyorder.DAO.FoodDAO;
import com.project.quanlyorder.DTO.BillInfoDTO;
import com.project.quanlyorder.DTO.FoodDTO;
import com.project.quanlyorder.DTO.TopFoodDTO;
import com.project.quanlyorder.OtherLibrary.MyDatePickerDialog;
import com.project.quanlyorder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DisplayTopFoodFragment extends Fragment {

    FragmentManager fragmentManager;
    TextView txt_statistic_date;
    String today;
    ListView lvTopFood;
    List<TopFoodDTO>topFoodDTOList;
    AdapterDisplayTopFood adapterDisplayTopFood;
    BillInfoDAO billInfoDAO;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_top_food, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Thống kê</font>"));
        setHasOptionsMenu(true);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        today = dateFormat.format(calendar.getTime());
        fragmentManager = getActivity().getSupportFragmentManager();

        txt_statistic_date = (TextView) view.findViewById(R.id.txt_topfood_date);
        lvTopFood = (ListView) view.findViewById(R.id.lvTopFood);
        billInfoDAO = new BillInfoDAO(getActivity());
        DisplayFoodByDay(today);
        return view;
    }
    //khởi tạo nút tim kiem va chuc nang
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //Doi ngay
        MenuItem itAddCategory = menu.add(1,R.id.itchoose_date_range,1,R.string.findDate);
        itAddCategory.setIcon(R.drawable.ic_search_512);
        itAddCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        //Menu thong ke
        inflater.inflate(R.menu.menu_statistic,menu);
    }
    //xử lý nút tim kiem va chuc nang
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itchoose_date_range:

                MyDatePickerDialog.DatePickerListener datePickerListener = new MyDatePickerDialog.DatePickerListener() {
                    @Override
                    public void dateEntered(String status, String date, String datebegin, String dateend) {
                        String datechoosed;
                        switch (status){
                            case MyDatePickerDialog.CHOOSE_DATE:
                                DisplayFoodByDay(date);
                                if(date.equals(today)){
                                    datechoosed = "hôm nay";
                                } else {
                                    datechoosed = date;
                                }
                                txt_statistic_date.setText("Ngày: "+datechoosed);
                                break;
                            case MyDatePickerDialog.CHOOSE_DATE_RANGE:
                                datechoosed = "Từ ngày "+datebegin+" đến ngày "+dateend;
                                DisplayFoodByDay(datebegin,dateend);
                                txt_statistic_date.setText(datechoosed);
                                break;
                        }
                    }
                };
                MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(getActivity(),datePickerListener);
                myDatePickerDialog.show();
                break;
            case R.id.list_bill:
                DisplayStatisticFragment displayStatisticFragment = new DisplayStatisticFragment();
                FragmentTransaction transactionBill = fragmentManager.beginTransaction();
                transactionBill.replace(R.id.contentView,displayStatisticFragment);
                transactionBill.commit();
                break;
            case R.id.top_food:
                break;
            case R.id.turnover:
                DisplayTurnoverFragment displayTurnoverFragment = new DisplayTurnoverFragment();
                FragmentTransaction transactionTurnover = fragmentManager.beginTransaction();
                transactionTurnover.replace(R.id.contentView,displayTurnoverFragment);
                transactionTurnover.commit();
                break;
            case R.id.respone_list:
                DisplayResponeList displayResponeList = new DisplayResponeList();
                FragmentTransaction transactionResponeList = fragmentManager.beginTransaction();
                transactionResponeList.replace(R.id.contentView,displayResponeList);
                transactionResponeList.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DisplayFoodByDay(String date) {
        topFoodDTOList = billInfoDAO.getListTopFood(date);
        adapterDisplayTopFood = new AdapterDisplayTopFood(getActivity(),R.layout.custom_layout_displaytopfood,topFoodDTOList);
        lvTopFood.setAdapter(adapterDisplayTopFood);
        adapterDisplayTopFood.notifyDataSetChanged();
    }
    private void DisplayFoodByDay(String datebegin,String dateend) {
        topFoodDTOList = billInfoDAO.getListTopFood(datebegin,dateend);
        adapterDisplayTopFood = new AdapterDisplayTopFood(getActivity(),R.layout.custom_layout_displaytopfood,topFoodDTOList);
        lvTopFood.setAdapter(adapterDisplayTopFood);
        adapterDisplayTopFood.notifyDataSetChanged();
    }
}