package com.project.quanlyorder.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.quanlyorder.OtherLibrary.DisplayBillInfoNoteActivity;
import com.project.quanlyorder.DAO.FoodDAO;
import com.project.quanlyorder.DAO.SaleDAO;
import com.project.quanlyorder.DTO.BillInfoDTO;
import com.project.quanlyorder.DTO.FoodDTO;
import com.project.quanlyorder.DTO.SaleDTO;
import com.project.quanlyorder.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterDisplayPayment extends BaseAdapter {
    Context context;
    int layout;
    List<BillInfoDTO> billinfoDTOList;
    ViewHolder viewHolder;

    public AdapterDisplayPayment(Context context, int layout, List<BillInfoDTO> billinfoDTOList) {
        this.context = context;
        this.layout = layout;
        this.billinfoDTOList = billinfoDTOList;
    }

    @Override
    public int getCount() {
        return billinfoDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return billinfoDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return billinfoDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);

            viewHolder.img_custompayment_IMGFood = (CircleImageView)view.findViewById(R.id.img_custompayment_ImgFood);
            viewHolder.txt_custompayment_NameFood = (TextView)view.findViewById(R.id.txt_custompayment_NameFood);
            viewHolder.txt_custompayment_Count = (TextView)view.findViewById(R.id.txt_custompayment_Count);
            viewHolder.txt_custompayment_Price = (TextView)view.findViewById(R.id.txt_custompayment_Price);
            viewHolder.imgbtn_note = (ImageButton) view.findViewById(R.id.btn_payment_note);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }


        BillInfoDTO billInfoDTO = billinfoDTOList.get(position);
        FoodDAO foodDAO = new FoodDAO(view.getContext());
        FoodDTO foodDTO = foodDAO.getFoodById(billInfoDTO.getIdFood());
        if(billInfoDTO.getNote().trim().equals("")||billInfoDTO.getNote() == null){
            viewHolder.imgbtn_note.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.imgbtn_note.setVisibility(View.VISIBLE);
        }
        viewHolder.imgbtn_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayBillInfoNoteActivity dialog_note = new DisplayBillInfoNoteActivity(context,billInfoDTO.getNote().trim());
                dialog_note.show();
            }
        });
        viewHolder.txt_custompayment_NameFood.setText(foodDTO.getName());
        viewHolder.txt_custompayment_Count.setText(String.valueOf(billInfoDTO.getCount()));
        String unitMoney = view.getResources().getString(R.string.unitMoney);
        SaleDAO saleDAO = new SaleDAO(view.getContext());
        SaleDTO saleDTO = saleDAO.getSaleById(foodDTO.getIdSale());
        if(saleDTO.getSalevalue()==0){
            viewHolder.txt_custompayment_Price.setText(foodDTO.getPrice() * billInfoDTO.getCount() + unitMoney);
        } else {
            String htmlcontent = "<strike>" + foodDTO.getPrice() * billInfoDTO.getCount() + unitMoney + "</strike>"
                    + "<br>" + foodDTO.getPrice() * billInfoDTO.getCount() * (100 - saleDTO.getSalevalue()) / 100 + unitMoney;
            viewHolder.txt_custompayment_Price.setText(android.text.Html.fromHtml(htmlcontent));
        }
        byte[] paymentimg = foodDTO.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(paymentimg,0,paymentimg.length);
        viewHolder.img_custompayment_IMGFood.setImageBitmap(bitmap);

        return view;
    }
    public class ViewHolder{
        CircleImageView img_custompayment_IMGFood;
        ImageButton imgbtn_note;
        TextView txt_custompayment_NameFood, txt_custompayment_Count, txt_custompayment_Price;
    }
}
