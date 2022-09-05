package com.project.quanlyorder.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.quanlyorder.Activities.DetailStatisticActivity;
import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.CustomAdapter.AdapterDisplayStatistic;
import com.project.quanlyorder.DAO.BillDAO;
import com.project.quanlyorder.DTO.BillDTO;
import com.project.quanlyorder.OtherLibrary.MyDatePickerDialog;
import com.project.quanlyorder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DisplayStatisticFragment extends Fragment {

    ListView lvStatistic;
    List<BillDTO> billDTOList;
    BillDAO billDAO;
    AdapterDisplayStatistic adapterDisplayStatistic;
    int idBill, idTable;
    int sumMoney;
    FragmentManager fragmentManager;
    TextView txt_statistic_date;
    String today;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_statistic, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Thống kê</font>"));
        setHasOptionsMenu(true);

        lvStatistic = (ListView)view.findViewById(R.id.lvStatistic);
        txt_statistic_date = (TextView)view.findViewById(R.id.txt_statistic_date);
        billDAO = new BillDAO(getActivity());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        today = dateFormat.format(calendar.getTime());
        DisplayStatisticByDay(today);
        fragmentManager = getActivity().getSupportFragmentManager();
        lvStatistic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idBill = billDTOList.get(position).getId();
                idTable = billDTOList.get(position).getIdTable();
                sumMoney = billDTOList.get(position).getSummoney();

                Intent intent = new Intent(getActivity(), DetailStatisticActivity.class);
                intent.putExtra("idBill",idBill);
                intent.putExtra("idTable",idTable);
                intent.putExtra("sumMoney",sumMoney);
                startActivity(intent);
            }
        });

        return view;
    }
    public void DisplayStatisticByDay(String startDate, String endDate){
        billDTOList = billDAO.getListBill(startDate,endDate);
        adapterDisplayStatistic = new AdapterDisplayStatistic(getActivity(),R.layout.custom_layout_displaystatistic,billDTOList);
        lvStatistic.setAdapter(adapterDisplayStatistic);
        adapterDisplayStatistic.notifyDataSetChanged();
    }
    public void DisplayStatisticByDay(String Date){
        billDTOList = billDAO.getListBill(Date);
        adapterDisplayStatistic = new AdapterDisplayStatistic(getActivity(),R.layout.custom_layout_displaystatistic,billDTOList);
        lvStatistic.setAdapter(adapterDisplayStatistic);
        adapterDisplayStatistic.notifyDataSetChanged();
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
                                DisplayStatisticByDay(date);
                                if(date.equals(today)){
                                    datechoosed = "hôm nay";
                                } else {
                                    datechoosed = date;
                                }
                                txt_statistic_date.setText("Ngày: "+datechoosed);
                                break;
                            case MyDatePickerDialog.CHOOSE_DATE_RANGE:
                                datechoosed = "Từ ngày "+datebegin+" đến ngày "+dateend;
                                DisplayStatisticByDay(datebegin,dateend);
                                txt_statistic_date.setText(datechoosed);
                                break;
                        }
                    }
                };
                MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(getActivity(),datePickerListener);
                myDatePickerDialog.show();
                break;
            case R.id.list_bill:
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
                DisplayResponeList displayResponeList = new DisplayResponeList();
                FragmentTransaction transactionResponeList = fragmentManager.beginTransaction();
                transactionResponeList.replace(R.id.contentView,displayResponeList);
                transactionResponeList.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}