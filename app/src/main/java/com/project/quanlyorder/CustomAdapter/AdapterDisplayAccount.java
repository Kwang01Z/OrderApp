package com.project.quanlyorder.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.quanlyorder.DAO.UserInfoDAO;
import com.project.quanlyorder.DTO.AccountDTO;
import com.project.quanlyorder.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterDisplayAccount extends BaseAdapter {

    Context context;
    int layout;
    List<AccountDTO> accountDTOList;
    ViewHolder viewHolder;
    UserInfoDAO userInfoDAO;

    public AdapterDisplayAccount(Context context, int layout, List<AccountDTO> accountDTOList) {
        this.context = context;
        this.layout = layout;
        this.accountDTOList = accountDTOList;
    }

    @Override
    public int getCount() {
        return accountDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return accountDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return accountDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);

            viewHolder.cimg_customacc_CImgAcc = (CircleImageView) view.findViewById(R.id.img_customacc_ImgAccount);
            viewHolder.txt_customacc_Fullname = (TextView) view.findViewById(R.id.txt_customacc_FullName);
            viewHolder.txt_customstaff_PosUser = (TextView) view.findViewById(R.id.txt_customacc_PosUser);
            viewHolder.txt_customstaff_PhoneNum = (TextView) view.findViewById(R.id.txt_customacc_PhoneNum);
            viewHolder.txt_customstaff_Email = (TextView) view.findViewById(R.id.txt_customacc_Email);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //lấy dữ liệu từ database truyền vào viewholder
        AccountDTO accountDTO = accountDTOList.get(position);
        userInfoDAO = new UserInfoDAO(view.getContext());
        int id = accountDTO.getId();
        viewHolder.txt_customacc_Fullname.setText(userInfoDAO.getUserInfoById(id).getFullName());
        if(accountDTO.getTypeAcc() == 1){
            viewHolder.cimg_customacc_CImgAcc.setBorderColor(view.getResources().getColor(R.color.teal_200));
        } else {
            viewHolder.cimg_customacc_CImgAcc.setBorderColor(view.getResources().getColor(R.color.black));
        }
        viewHolder.txt_customstaff_PosUser.setText(userInfoDAO.getUserInfoById(id).getUserPosition());
        viewHolder.txt_customstaff_PhoneNum.setText(userInfoDAO.getUserInfoById(id).getPhoneNumber());
        viewHolder.txt_customstaff_Email.setText(userInfoDAO.getUserInfoById(id).getEmail());
        return view;
    }
    public class ViewHolder{
        CircleImageView cimg_customacc_CImgAcc;
        TextView txt_customacc_Fullname, txt_customstaff_PosUser,txt_customstaff_PhoneNum, txt_customstaff_Email;
    }
}
