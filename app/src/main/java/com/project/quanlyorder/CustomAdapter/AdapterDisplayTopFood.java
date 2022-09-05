package com.project.quanlyorder.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.quanlyorder.DAO.BillDAO;
import com.project.quanlyorder.DAO.FoodDAO;
import com.project.quanlyorder.DAO.SaleDAO;
import com.project.quanlyorder.DTO.BillInfoDTO;
import com.project.quanlyorder.DTO.FoodDTO;
import com.project.quanlyorder.DTO.TopFoodDTO;
import com.project.quanlyorder.R;

import java.util.List;

public class AdapterDisplayTopFood extends BaseAdapter {
    Context context;
    int layout;
    List<TopFoodDTO> topFoodDTOList;
    ViewHolder viewHolder;
    FoodDAO foodDAO;
    SaleDAO saleDAO;
    BillDAO billDAO;

    public AdapterDisplayTopFood(Context context, int layout, List<TopFoodDTO> topFoodDTOList) {
        this.context = context;
        this.layout = layout;
        this.topFoodDTOList = topFoodDTOList;
    }

    @Override
    public int getCount() {
        return topFoodDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return topFoodDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return topFoodDTOList.get(position).getIdFood();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //lần đầu gọi view
        if (view == null) {
            viewHolder = new AdapterDisplayTopFood.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, parent, false);

            //truyền component vào viewholder để ko gọi findview ở những lần hiển thị khác
            viewHolder.imgFood = (ImageView) view.findViewById(R.id.img_topfood_ImgFood);
            viewHolder.txtNameFood = (TextView) view.findViewById(R.id.txt_topfood_NameFood);
            viewHolder.txtPriceFood = (TextView) view.findViewById(R.id.txt_topfood_Price);
            viewHolder.txtSaleFood = (TextView) view.findViewById(R.id.txt_topfood_Sale);
            viewHolder.txtStatusFood = (TextView) view.findViewById(R.id.txt_topfood_Status);
            viewHolder.txtCount = (TextView)view.findViewById(R.id.txt_topfood_count);
            view.setTag(viewHolder);
        } else {
            view.getTag();
        }
        //lấy dữ liệu từ database truyền vào viewholder
        foodDAO = new FoodDAO(view.getContext());
        saleDAO = new SaleDAO(view.getContext());
        billDAO = new BillDAO(view.getContext());
        FoodDTO foodDTO = foodDAO.getFoodById(topFoodDTOList.get(position).getIdFood());
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

        viewHolder.txtCount.setText(view.getResources().getString(R.string.orded)+topFoodDTOList.get(position).getCount());
        return view;
    }
    public class ViewHolder{
        ImageView imgFood;
        TextView txtNameFood, txtPriceFood, txtStatusFood, txtSaleFood, txtCount;
    }
}
