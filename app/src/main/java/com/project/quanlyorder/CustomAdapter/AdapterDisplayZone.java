package com.project.quanlyorder.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.quanlyorder.DTO.SaleDTO;
import com.project.quanlyorder.DTO.ZoneDTO;
import com.project.quanlyorder.R;

import java.util.List;

public class AdapterDisplayZone extends BaseAdapter {
    Context context;
    int layout;
    List<ZoneDTO> zoneDTOList;
    ViewHolder viewHolder;

    public AdapterDisplayZone(Context context, int layout, List<ZoneDTO> zoneDTOList) {
        this.context = context;
        this.layout = layout;
        this.zoneDTOList = zoneDTOList;
    }

    @Override
    public int getCount() {
        return zoneDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return zoneDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return zoneDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);
            viewHolder.txtNameZone = (TextView) view.findViewById(R.id.txt_customzone_NameZone);
            view.setTag(viewHolder);
        } else {
            view.getTag();
        }
        ZoneDTO zoneDTO = zoneDTOList.get(position);
        viewHolder.txtNameZone.setText(zoneDTO.getName());
        return view;
    }
    public class ViewHolder{
        TextView txtNameZone;
    }
}
