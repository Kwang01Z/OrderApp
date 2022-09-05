package com.project.quanlyorder.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.quanlyorder.DTO.CheckDTO;
import com.project.quanlyorder.R;

import java.util.List;

public class AdapterDisplayRespone extends BaseAdapter {
    Context context;
    int layout;
    List<CheckDTO> checkDTOList;
    ViewHolder viewHolder;

    public AdapterDisplayRespone(Context context, int layout, List<CheckDTO> checkDTOList) {
        this.context = context;
        this.layout = layout;
        this.checkDTOList = checkDTOList;
    }

    @Override
    public int getCount() {
        return checkDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return checkDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return checkDTOList.get(position).getIdBill();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            viewHolder = new AdapterDisplayRespone.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, parent, false);

            //truyền component vào viewholder để ko gọi findview ở những lần hiển thị khác
            viewHolder.txtIdBill = view.findViewById(R.id.txt_customrespone_idBill);
            viewHolder.txtDate = view.findViewById(R.id.txt_customrespone_dateCheckin);
            viewHolder.txtRespone = view.findViewById(R.id.tv_customrespone_respone);

            CheckDTO checkDTO = checkDTOList.get(position);
            viewHolder.txtIdBill.setText(view.getResources().getString(R.string.idBill)+checkDTO.getIdBill());
            viewHolder.txtDate.setText(checkDTO.getDateCheckOut());
            viewHolder.txtRespone.setText(checkDTO.getNote());
            view.setTag(viewHolder);
        } else {
            view.getTag();
        }
        return view;
    }
    public class ViewHolder{
        TextView txtIdBill, txtDate, txtRespone;
    }
}
