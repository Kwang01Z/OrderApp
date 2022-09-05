package com.project.quanlyorder.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.quanlyorder.DAO.TableFoodDAO;
import com.project.quanlyorder.DAO.UserInfoDAO;
import com.project.quanlyorder.DAO.ZoneDAO;
import com.project.quanlyorder.DTO.BillDTO;
import com.project.quanlyorder.DTO.UserInfoDTO;
import com.project.quanlyorder.R;

import java.util.List;

public class AdapterDisplayStatistic extends BaseAdapter {
    Context context;
    int layout;
    List<BillDTO> billDTOList;
    ViewHolder viewHolder;
    UserInfoDAO userinfoDAO;
    TableFoodDAO tablefoodDAO;
    ZoneDAO zoneDAO;

    public AdapterDisplayStatistic(Context context, int layout, List<BillDTO> billDTOList){
        this.context = context;
        this.layout = layout;
        this.billDTOList = billDTOList;
        userinfoDAO = new UserInfoDAO(context);
        tablefoodDAO = new TableFoodDAO(context);
        zoneDAO = new ZoneDAO(context);
    }


    @Override
    public int getCount() {
        return billDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return billDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return billDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);

            viewHolder.txt_customstatistic_idBill = (TextView)view.findViewById(R.id.txt_customrespone_idBill);
            viewHolder.txt_customstatistic_dateCheckin = (TextView)view.findViewById(R.id.txt_customrespone_dateCheckin);
            viewHolder.txt_customstatistic_nameZone = (TextView)view.findViewById(R.id.txt_customstatistic_nameZone);
            viewHolder.txt_customstatistic_sumMoney = (TextView)view.findViewById(R.id.txt_customstatistic_sumMoney);
            viewHolder.txt_customstatistic_status = (TextView)view.findViewById(R.id.txt_customstatistic_status);
            viewHolder.txt_customstatistic_nameTable = (TextView)view.findViewById(R.id.txt_customstatistic_nameTable);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        BillDTO billDTO = billDTOList.get(position);
        viewHolder.txt_customstatistic_idBill.setText(view.getResources().getString(R.string.idBill)+billDTO.getId());
        viewHolder.txt_customstatistic_dateCheckin.setText(billDTO.getDateCheckIn());
        viewHolder.txt_customstatistic_sumMoney.setText(billDTO.getSummoney()+" VNĐ");
        if (billDTO.getStatus() == 1)
        {
            viewHolder.txt_customstatistic_status.setText("Đã thanh toán");
        }else {
            viewHolder.txt_customstatistic_status.setText("Chưa thanh toán");
        }
        UserInfoDTO userinfoDTO = userinfoDAO.getUserInfoById(billDTO.getId());
        viewHolder.txt_customstatistic_nameZone.setText(userinfoDTO.getFullName());
        viewHolder.txt_customstatistic_nameTable.setText(tablefoodDAO.getTableById(billDTO.getIdTable()).getName()+" / "+zoneDAO.getZoneById(tablefoodDAO.getTableById(billDTO.getIdTable()).getIdZone()).getName());

        return view;
    }

    public class ViewHolder{
        TextView txt_customstatistic_idBill, txt_customstatistic_dateCheckin, txt_customstatistic_nameZone
                ,txt_customstatistic_sumMoney,txt_customstatistic_status, txt_customstatistic_nameTable;

    }
}
