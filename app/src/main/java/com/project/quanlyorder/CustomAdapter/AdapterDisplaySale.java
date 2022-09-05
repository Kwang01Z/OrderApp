package com.project.quanlyorder.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.quanlyorder.DTO.BillDTO;
import com.project.quanlyorder.DTO.SaleDTO;
import com.project.quanlyorder.R;

import java.util.List;

public class AdapterDisplaySale extends BaseAdapter {
    Context context;
    int layout;
    List<SaleDTO> saleDTOList;
    ViewHolder viewHolder;

    public AdapterDisplaySale(Context context, int layout, List<SaleDTO> saleDTOList) {
        this.context = context;
        this.layout = layout;
        this.saleDTOList = saleDTOList;
    }

    @Override
    public int getCount() {
        return saleDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return saleDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return saleDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //lần đầu gọi view
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);

            viewHolder.txtSaleName = (TextView) view.findViewById(R.id.txt_customsale_NameSale);
            viewHolder.txtSaleValue = (TextView) view.findViewById(R.id.txt_customsale_SaleValue);
            view.setTag(viewHolder);
        } else {
            view.getTag();
        }
        SaleDTO saleDTO = saleDTOList.get(position);
        viewHolder.txtSaleName.setText(saleDTO.getName());
        viewHolder.txtSaleValue.setText("-"+saleDTO.getSalevalue()+"%");

        return view;
    }

    //tạo viewholer lưu trữ component
    public class ViewHolder{
        TextView txtSaleName, txtSaleValue;
    }
}
