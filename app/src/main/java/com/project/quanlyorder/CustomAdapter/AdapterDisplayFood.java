package com.project.quanlyorder.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.quanlyorder.DAO.BillDAO;
import com.project.quanlyorder.DAO.BillInfoDAO;
import com.project.quanlyorder.DAO.FoodDAO;
import com.project.quanlyorder.DAO.SaleDAO;
import com.project.quanlyorder.DTO.BillDTO;
import com.project.quanlyorder.DTO.BillInfoDTO;
import com.project.quanlyorder.DTO.FoodDTO;
import com.project.quanlyorder.R;

import java.util.List;

public class AdapterDisplayFood extends BaseAdapter {

    Context context;
    int layout;
    List<FoodDTO> foodDTOList;
    ViewHolder viewHolder;
    SaleDAO saleDAO;
    int idTable;
    BillDAO billDAO;

    public AdapterDisplayFood(Context context, int layout, List<FoodDTO> foodDTOList, int idTable) {
        this.context = context;
        this.layout = layout;
        this.foodDTOList = foodDTOList;
        this.idTable = idTable;
    }

    @Override
    public int getCount() {
        return foodDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return foodDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //lần đầu gọi view
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, parent, false);

            //truyền component vào viewholder để ko gọi findview ở những lần hiển thị khác
            viewHolder.imgFood = (ImageView) view.findViewById(R.id.img_customfood_ImgFood);
            viewHolder.txtNameFood = (TextView) view.findViewById(R.id.txt_customfood_NameFood);
            viewHolder.txtPriceFood = (TextView) view.findViewById(R.id.txt_customfood_Price);
            viewHolder.txtSaleFood = (TextView) view.findViewById(R.id.txt_customfood_Sale);
            viewHolder.txtStatusFood = (TextView) view.findViewById(R.id.txt_customfood_Status);
            viewHolder.txtCount = (TextView)view.findViewById(R.id.txt_customfood_count);
            view.setTag(viewHolder);
        } else {
            view.getTag();
        }
        //lấy dữ liệu từ database truyền vào viewholder
        FoodDTO foodDTO = foodDTOList.get(position);
        byte[] imgFood2 = foodDTO.getImg();
        Bitmap bitmap= BitmapFactory.decodeByteArray(imgFood2, 0, imgFood2.length);
        viewHolder.imgFood.setImageBitmap(bitmap);
        viewHolder.txtNameFood.setText(foodDTO.getName());

        String statusFood;
        if (foodDTO.getStatus() == 0) {
            statusFood = view.getResources().getString(R.string.food_not_exsit);
        } else {
            statusFood = view.getResources().getString(R.string.food_exist);
        }
        viewHolder.txtStatusFood.setText(statusFood);
        saleDAO = new SaleDAO(view.getContext());
        billDAO = new BillDAO(view.getContext());
        int sale = saleDAO.getSaleValueById(foodDTO.getIdSale());

        String unitMoney = view.getResources().getString(R.string.unitMoney);
        if (sale == 0) {
            viewHolder.txtPriceFood.setText(foodDTO.getPrice()+unitMoney);
            viewHolder.txtSaleFood.setVisibility(View.GONE);
        } else {
            viewHolder.txtSaleFood.setVisibility(View.VISIBLE);
            String saleText = " - " + sale + "% ";
            viewHolder.txtSaleFood.setText(saleText);
            int curentPrice = foodDTO.getPrice() * (100 - sale) / 100;
            String htmlcontent = "<s>" + foodDTO.getPrice()+ unitMoney + "</s>"
                    + "<br>" + curentPrice + unitMoney;
            viewHolder.txtPriceFood.setText(android.text.Html.fromHtml(htmlcontent));
        }
        if (idTable > 0){
            int count;
            int idBill = billDAO.getIDBillByIdTable(idTable);
            BillInfoDAO billInfoDAO = new BillInfoDAO(view.getContext());
            count = billInfoDAO.getBillInfoById(idBill,foodDTOList.get(position).getId()).getCount();
            if(count > 0){
                viewHolder.txtCount.setVisibility(View.VISIBLE);
                viewHolder.txtCount.setText(String.valueOf(count));
            }
            else {
                viewHolder.txtCount.setVisibility(View.GONE);
            }

        } else {
            viewHolder.txtCount.setVisibility(View.GONE);
        }
        return view;
    }
    //tạo viewholer lưu trữ component
    public class ViewHolder{
        ImageView imgFood;
        TextView txtNameFood, txtPriceFood, txtStatusFood, txtSaleFood, txtCount;
    }
}
