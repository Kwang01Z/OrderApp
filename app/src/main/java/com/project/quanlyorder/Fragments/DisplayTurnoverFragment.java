package com.project.quanlyorder.Fragments;

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
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.DAO.BillDAO;
import com.project.quanlyorder.DAO.CheckDAO;
import com.project.quanlyorder.OtherLibrary.MyDatePickerDialog;
import com.project.quanlyorder.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DisplayTurnoverFragment extends Fragment {

    FragmentManager fragmentManager;
    String today;
    TextView txt_turnover_date, txt_turnover_totalbillcount, txt_turnover_totalturnover;
    CombinedChart chartBill, charTurnover;
    BillDAO billDAO;
    CheckDAO checkDAO;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_turnover, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Thống kê</font>"));
        setHasOptionsMenu(true);
        fragmentManager = getActivity().getSupportFragmentManager();

        billDAO = new BillDAO(getActivity());
        checkDAO = new CheckDAO(getActivity());

        txt_turnover_date = (TextView) view.findViewById(R.id.tv_turnover_datechoosed);
        txt_turnover_totalbillcount = (TextView) view.findViewById(R.id.txt_turnover_totalbillcount);
        txt_turnover_totalturnover = (TextView) view.findViewById(R.id.txt_turnover_totalturnover);
        chartBill = (CombinedChart)view.findViewById(R.id.chart_turnover_bill);
        charTurnover = (CombinedChart)view.findViewById(R.id.chart_turnover_turnover);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        today = dateFormat.format(calendar.getTime());
        DisplayTurnoverByDay(today);

        return view;
    }
    private ArrayList<String> getXAxisArr(String datebegin, String dateend){
        ArrayList<String> DAYS = new ArrayList<>();
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date dateBegin = new SimpleDateFormat("dd-MM-yyyy").parse(datebegin);
            Date dateEnd = new SimpleDateFormat("dd-MM-yyyy").parse(dateend);
            Date date = dateBegin;

            while (date.getTime() <= dateEnd.getTime()){
                DAYS.add(dateFormat.format(date.getTime()));
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();
            }
        }catch (Exception e){
            Log.e("Error===",e.toString());
        }
        return DAYS;
    }
    private void configureChartAppearance(CombinedChart chart, String date, float granuX, float granuY) {
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add(date);
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(granuX);
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(arrayList));

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setGranularity(granuY);
        axisLeft.setAxisMinimum(0);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setGranularity(granuY);
        axisRight.setAxisMinimum(0);
    }

    private void configureChartAppearance(CombinedChart chart, String datebegin, String dateend, float granuX, float granuY) {
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);
        ArrayList<String> arrayList = getXAxisArr(datebegin,dateend);
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(granuX);
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(arrayList));

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setGranularity(granuY);
        axisLeft.setAxisMinimum(0);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setGranularity(granuY);
        axisRight.setAxisMinimum(0);
    }
    private void DisplayTurnoverByDay(String date) {
        configureChartAppearance(chartBill,date,1f,1f);
        configureChartAppearance(charTurnover,date,1f,10000f);
        BarDataSet lineDataSetBill = new BarDataSet(datavalueBill(date),getResources().getString(R.string.totalbillperday));
        ArrayList<IBarDataSet> dataSetsBill = new ArrayList<>();
        dataSetsBill.add(lineDataSetBill);
        BarData dataBill = new BarData(dataSetsBill);
        dataBill.setBarWidth(0.3f);
        CombinedData combinedDataBill = new CombinedData();
        combinedDataBill.setData(dataBill);
        chartBill.setData(combinedDataBill);
        chartBill.invalidate();

        BarDataSet lineDataSetTurnover = new BarDataSet(datavalueTurnover(date),getResources().getString(R.string.totalturnoverperday));
        ArrayList<IBarDataSet> dataSetsTurnover = new ArrayList<>();
        dataSetsTurnover.add(lineDataSetTurnover);
        BarData dataTurnover = new BarData(dataSetsTurnover);
        dataTurnover.setBarWidth(0.3f);
        CombinedData combinedDataTurnover = new CombinedData();
        combinedDataTurnover.setData(dataTurnover);
        charTurnover.setData(combinedDataTurnover);
        charTurnover.invalidate();
    }
    private void DisplayTurnoverByDay(String datebegin, String dateend) {
        configureChartAppearance(chartBill,datebegin,dateend,1f,1f);
        configureChartAppearance(charTurnover,datebegin,dateend,1f,10000f);
        BarDataSet lineDataSetBill = new BarDataSet(datavalueBill(datebegin,dateend),getResources().getString(R.string.totalbillperday));
        ArrayList<IBarDataSet> dataSetsBill = new ArrayList<>();
        dataSetsBill.add(lineDataSetBill);
        BarData dataBill = new BarData(dataSetsBill);
        dataBill.setBarWidth(0.3f);
        CombinedData combinedDataBill = new CombinedData();
        combinedDataBill.setData(dataBill);
        chartBill.setData(combinedDataBill);
        chartBill.invalidate();

        BarDataSet lineDataSetTurnover = new BarDataSet(datavalueTurnover(datebegin,dateend),getResources().getString(R.string.totalturnoverperday));
        ArrayList<IBarDataSet> dataSetsTurnover = new ArrayList<>();
        dataSetsTurnover.add(lineDataSetTurnover);
        BarData dataTurnover = new BarData(dataSetsTurnover);
        dataTurnover.setBarWidth(0.3f);
        CombinedData combinedDataTurnover = new CombinedData();
        combinedDataTurnover.setData(dataTurnover);
        charTurnover.setData(combinedDataTurnover);
        charTurnover.invalidate();
    }

    private ArrayList<BarEntry> datavalueBill(String date){
        int y = billDAO.getTotalBillByDay(date);
        txt_turnover_totalbillcount.setText(getResources().getString(R.string.totalbill)+y);
        ArrayList<BarEntry> arrayList = new ArrayList<>();
        arrayList.add(new BarEntry(0,y));
        return arrayList;
    }
    private ArrayList<BarEntry> datavalueTurnover(String date){
        int y = checkDAO.getTurnoverByDay(date);
        txt_turnover_totalturnover.setText(getResources().getString(R.string.totalturnover)+y+" "+getResources().getString(R.string.unitMoney));
        ArrayList<BarEntry> arrayList = new ArrayList<>();
        arrayList.add(new BarEntry(0,y));
        return arrayList;
    }
    private int totalArr(ArrayList<Integer> arrayList){
        int total = 0;
        for (int item : arrayList){
            total+=item;
        }
        return total;
    }
    private ArrayList<BarEntry> datavalueBill(String datebegin, String dateend){
        ArrayList<Integer> listY = billDAO.getListCountBill(datebegin,dateend);
        txt_turnover_totalbillcount.setText(getResources().getString(R.string.totalbill)+totalArr(listY));
        ArrayList<BarEntry> arrayList = new ArrayList<>();
        int i = 0;
        for (int item : listY){
            arrayList.add(new BarEntry(i,item));
            i++;
        }
        return arrayList;
    }

    private ArrayList<BarEntry> datavalueTurnover(String datebegin, String dateend){
        ArrayList<Integer> listY = checkDAO.getListTurnover(datebegin,dateend);
        txt_turnover_totalturnover.setText(getResources().getString(R.string.totalturnover)+totalArr(listY)+" "+getResources().getString(R.string.unitMoney));
        ArrayList<BarEntry> arrayList = new ArrayList<>();
        int i = 0;
        for (int item : listY){
            arrayList.add(new BarEntry(i,item));
            i++;
        }
        return arrayList;
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
                                DisplayTurnoverByDay(date);
                                if(date.equals(today)){
                                    datechoosed = "hôm nay";
                                } else {
                                    datechoosed = date;
                                }
                                txt_turnover_date.setText("Ngày: "+datechoosed);
                                break;
                            case MyDatePickerDialog.CHOOSE_DATE_RANGE:
                                datechoosed = "Từ ngày "+datebegin+" đến ngày "+dateend;
                                DisplayTurnoverByDay(datebegin,dateend);
                                txt_turnover_date.setText(datechoosed);
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