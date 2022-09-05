package com.project.quanlyorder.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.Activities.PaymentActivity;
import com.project.quanlyorder.DAO.BillDAO;
import com.project.quanlyorder.DAO.TableFoodDAO;
import com.project.quanlyorder.DTO.BillDTO;
import com.project.quanlyorder.DTO.SaleDTO;
import com.project.quanlyorder.DTO.TableFoodDTO;
import com.project.quanlyorder.Fragments.DisplayCategoryFoodFragment;
import com.project.quanlyorder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterDisplayTable extends BaseAdapter implements View.OnClickListener {
    Context context;
    int layout,idAcc;
    List<TableFoodDTO> tableFoodDTOList;
    ViewHolder viewHolder;
    FragmentManager fragmentManager;
    public AdapterDisplayTable(Context context, int layout, List<TableFoodDTO> tableFoodDTOList,int idAcc) {
        this.context = context;
        this.layout = layout;
        this.tableFoodDTOList = tableFoodDTOList;
        this.idAcc = idAcc;
        fragmentManager = ((HomeActivity)context).getSupportFragmentManager();
    }

    @Override
    public int getCount() {
        return tableFoodDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return tableFoodDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tableFoodDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //lần đầu gọi view
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);
            viewHolder.txtNameTable = (TextView) view.findViewById(R.id.txt_customtable_NameTable);
            viewHolder.imgTable = (ImageView) view.findViewById(R.id.img_customtable_Table);
            viewHolder.imgOrder= (ImageView)view.findViewById(R.id.img_customtable_Order);
            viewHolder.imgPaymeny = (ImageView)view.findViewById(R.id.img_customtable_Payment);
            viewHolder.imgHideButton = (ImageView)view.findViewById(R.id.img_customtable_HideButton);

            view.setTag(viewHolder);
        } else {
            view.getTag();
        }
        if(tableFoodDTOList.get(position).isPicked()){
            DisplayButton();
        }else {
            HideButton();
        }
        TableFoodDTO tableFoodDTO = tableFoodDTOList.get(position);
        viewHolder.txtNameTable.setText(tableFoodDTO.getName());
        if(tableFoodDTO.getStatus() == 0){
            viewHolder.imgTable.setImageResource(R.drawable.ic_baseline_event_seat_40);
        } else {
            viewHolder.imgTable.setImageResource(R.drawable.ic_baseline_airline_seat_legroom_normal_40);
        }
        viewHolder.imgTable.setTag(position);
        //sự kiện click
        viewHolder.imgTable.setOnClickListener(this);
        viewHolder.imgOrder.setOnClickListener(this);
        viewHolder.imgPaymeny.setOnClickListener(this);
        viewHolder.imgHideButton.setOnClickListener(this);
        return view;
    }
    private void DisplayButton(){
        viewHolder.imgOrder.setVisibility(View.VISIBLE);
        viewHolder.imgPaymeny.setVisibility(View.VISIBLE);
        viewHolder.imgHideButton.setVisibility(View.VISIBLE);
    }
    private void HideButton(){
        viewHolder.imgOrder.setVisibility(View.INVISIBLE);
        viewHolder.imgPaymeny.setVisibility(View.INVISIBLE);
        viewHolder.imgHideButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        viewHolder = (ViewHolder) ((View) v.getParent()).getTag();

        int pos1 = (int) viewHolder.imgTable.getTag();
        int idTable = tableFoodDTOList.get(pos1).getId();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateCheckIn= dateFormat.format(calendar.getTime());
        switch (id){
            case R.id.img_customtable_Table:
                int pos = (int)v.getTag();
                tableFoodDTOList.get(pos).setPicked(true);
                DisplayButton();
                break;

            case R.id.img_customtable_HideButton:
                HideButton();
                break;

            case R.id.img_customtable_Order:

                //chuyển qua trang category
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                DisplayCategoryFoodFragment displayCategoryFragment = new DisplayCategoryFoodFragment();

                Bundle bDataCategory = new Bundle();
                bDataCategory.putInt("idTable",idTable);
                displayCategoryFragment.setArguments(bDataCategory);

                transaction.replace(R.id.contentView,displayCategoryFragment).addToBackStack("displaytable");
                transaction.commit();
                break;

            case R.id.img_customtable_Payment:
                //chuyển dữ liệu qua trang thanh toán

                Intent iThanhToan = new Intent(context, PaymentActivity.class);
                iThanhToan.putExtra("idAcc",idAcc);
                iThanhToan.putExtra("idTable",idTable);
                iThanhToan.putExtra("dateCheckIn",dateCheckIn);
                context.startActivity(iThanhToan);
                break;
        }
    }

    public class ViewHolder{
        TextView txtNameTable;
        ImageView imgTable, imgOrder, imgPaymeny, imgHideButton;
    }
}
