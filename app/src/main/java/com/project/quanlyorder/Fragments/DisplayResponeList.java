package com.project.quanlyorder.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.CustomAdapter.AdapterDisplayRespone;
import com.project.quanlyorder.DAO.CheckDAO;
import com.project.quanlyorder.DTO.CheckDTO;
import com.project.quanlyorder.OtherLibrary.MyDatePickerDialog;
import com.project.quanlyorder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class DisplayResponeList extends Fragment {

    FragmentManager fragmentManager;
    TextView txt_respone_date;
    String today;
    ListView lvResponeList;
    CheckDAO checkDAO;
    AdapterDisplayRespone adapterDisplayRespone;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_respone_list, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Thống kê</font>"));
        setHasOptionsMenu(true);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        today = dateFormat.format(calendar.getTime());
        fragmentManager = getActivity().getSupportFragmentManager();

        checkDAO = new CheckDAO(getActivity());
        txt_respone_date = (TextView)view.findViewById(R.id.txt_respone_date);
        lvResponeList = (ListView) view.findViewById(R.id.lvResponeList);
        DisplayResponeListByDay(today);
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
                                DisplayResponeListByDay(date);
                                if(date.equals(today)){
                                    datechoosed = "hôm nay";
                                } else {
                                    datechoosed = date;
                                }
                                txt_respone_date.setText("Ngày: "+datechoosed);
                                break;
                            case MyDatePickerDialog.CHOOSE_DATE_RANGE:
                                datechoosed = "Từ ngày "+datebegin+" đến ngày "+dateend;
                                DisplayResponeListByDay(datebegin,dateend);
                                txt_respone_date.setText(datechoosed);
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
                DisplayTopFoodFragment displayTopFoodFragment = new DisplayTopFoodFragment();
                FragmentTransaction transactionTopFood = fragmentManager.beginTransaction();
                transactionTopFood.replace(R.id.contentView,displayTopFoodFragment);
                transactionTopFood.commit();
                break;
            case R.id.turnover:
                DisplayTurnoverFragment displayTurnoverFragment = new DisplayTurnoverFragment();
                FragmentTransaction transactionTurnover = fragmentManager.beginTransaction();
                transactionTurnover.replace(R.id.contentView,displayTurnoverFragment);
                transactionTurnover.commit();
                break;
            case R.id.respone_list:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void DisplayResponeListByDay(String date) {
        List<CheckDTO> checkDTOList = checkDAO.getListCheckByDay(date);
        adapterDisplayRespone = new AdapterDisplayRespone(getActivity(),R.layout.custom_layout_displayrespone,checkDTOList);
        lvResponeList.setAdapter(adapterDisplayRespone);
        adapterDisplayRespone.notifyDataSetChanged();

    }
    private void DisplayResponeListByDay(String datebegin,String dateend) {
        List<CheckDTO> checkDTOList = checkDAO.getListCheckByDay(datebegin,dateend);
        adapterDisplayRespone = new AdapterDisplayRespone(getActivity(),R.layout.custom_layout_displayrespone,checkDTOList);
        lvResponeList.setAdapter(adapterDisplayRespone);
        adapterDisplayRespone.notifyDataSetChanged();
    }
}