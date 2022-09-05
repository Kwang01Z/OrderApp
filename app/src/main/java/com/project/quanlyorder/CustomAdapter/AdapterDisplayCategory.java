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

import androidx.fragment.app.FragmentActivity;

import com.project.quanlyorder.DTO.FoodCategoryDTO;
import com.project.quanlyorder.R;

import java.util.List;

public class AdapterDisplayCategory extends BaseAdapter {

    Context context;
    int layout;
    List<FoodCategoryDTO> foodCategoryDTOList ;
    ViewHolder viewHolder;


    public AdapterDisplayCategory(Context context, int layout, List<FoodCategoryDTO> foodCategoryDTOList) {
        this.context = context;
        this.layout = layout;
        this.foodCategoryDTOList = foodCategoryDTOList;
    }

    @Override
    public int getCount() {
        return foodCategoryDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodCategoryDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return foodCategoryDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //nếu lần đầu gọi view
        if(view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);

            //truyền component vào viewholder để ko gọi findview ở những lần hiển thị khác
            viewHolder.txt_customcategory_NameCategory = (TextView) view.findViewById(R.id.txt_customcategory_NameCategory);
            viewHolder.img_customcategory_ImgCategory = (ImageView) view.findViewById(R.id.img_customcategory_ImgCategory);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //lấy dữ liệu từ database truyền vào viewholder
        FoodCategoryDTO foodCategoryDTO = foodCategoryDTOList.get(position);
        viewHolder.txt_customcategory_NameCategory.setText(foodCategoryDTO.getName());
        byte[] categoryimage = foodCategoryDTO.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(categoryimage,0,categoryimage.length);
        viewHolder.img_customcategory_ImgCategory.setImageBitmap(bitmap);

        return view;
    }

    //tạo viewholer lưu trữ component
    public class ViewHolder{
        TextView txt_customcategory_NameCategory;
        ImageView img_customcategory_ImgCategory;
    }
}
