package com.project.quanlyorder.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.project.quanlyorder.DAO.FoodCategoryDAO;
import com.project.quanlyorder.DTO.FoodCategoryDTO;
import com.project.quanlyorder.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener{

    Button BTN_addcategory_Addcategory;
    ImageView IMG_addcategory_back, IMG_addcategory_AddImg;
    TextView TXT_addcategory_FunctionTitle;
    TextInputLayout TXTL_addcategory_NameCategory;
    FoodCategoryDAO foodCategoryDAO;
    int idCategory = 0;
    Bitmap bitmapold;   //Bitmap dạng ảnh theo ma trận các pixel

    //dùng result launcher do activityforresult ko dùng đc nữa
    ActivityResultLauncher<Intent> resultLauncherOpenIMG = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                        Uri uri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            IMG_addcategory_AddImg.setImageBitmap(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category_layout);

        foodCategoryDAO = new FoodCategoryDAO(this);
        //region Lấy đối tượng view
        BTN_addcategory_Addcategory = (Button)findViewById(R.id.btn_addcategory_Addcategory) ;
        IMG_addcategory_back = (ImageView) findViewById(R.id.img_addcategory_back) ;
        IMG_addcategory_AddImg = (ImageView)findViewById(R.id.img_addcategory_AddImg);
        TXT_addcategory_FunctionTitle = (TextView) findViewById(R.id.txt_addcategory_FunctionTilte) ;
        TXTL_addcategory_NameCategory = (TextInputLayout) findViewById(R.id.txtl_addcategory_NameCategory);
        //end region

        BitmapDrawable olddrawable = (BitmapDrawable)IMG_addcategory_AddImg.getDrawable();
        bitmapold = olddrawable.getBitmap();

        //region Hiển thị trang sửa nếu được chọn từ context menu sửa
        idCategory = getIntent().getIntExtra("idCategory",0);
        if(idCategory != 0){
            TXT_addcategory_FunctionTitle.setText(getResources().getString(R.string.editcategory));
            FoodCategoryDTO foodCategoryDTO = foodCategoryDAO.getFoodCategoryById(idCategory);

            //Hiển thị lại thông tin từ csdl
            TXTL_addcategory_NameCategory.getEditText().setText(foodCategoryDTO.getName());

            byte[] categoryimage = foodCategoryDTO.getImg();
            Bitmap bitmap = BitmapFactory.decodeByteArray(categoryimage,0,categoryimage.length);
            IMG_addcategory_AddImg.setImageBitmap(bitmap);

            BTN_addcategory_Addcategory.setText("Sửa loại");
        }
        //endregion

        IMG_addcategory_back.setOnClickListener(this);
        IMG_addcategory_AddImg.setOnClickListener(this);
        BTN_addcategory_Addcategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        boolean resultCategory;
        String function;
        switch (id){
            case R.id.img_addcategory_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //animation
                break;

            case R.id.img_addcategory_AddImg:
                Intent iGetIMG = new Intent();
                iGetIMG.setType("image/*"); //lấy những mục chứa hình ảnh
                iGetIMG.setAction(Intent.ACTION_GET_CONTENT);   //lấy mục hiện tại đang chứa hình
                resultLauncherOpenIMG.launch(Intent.createChooser(iGetIMG,getResources().getString(R.string.choseimg)));    //mở intent chọn hình ảnh
                break;

            case R.id.btn_addcategory_Addcategory:
                if(!validateImage() | !validateName()){
                    return;
                }

                String sTenLoai = TXTL_addcategory_NameCategory.getEditText().getText().toString();
                FoodCategoryDTO foodCategoryDTO = new FoodCategoryDTO();
                foodCategoryDTO.setName(sTenLoai);
                foodCategoryDTO.setImg(imageViewtoByte(IMG_addcategory_AddImg));
                foodCategoryDTO.setIdSale(0);
                if(idCategory != 0){
                    resultCategory = foodCategoryDAO.updateFoodCategory(idCategory,foodCategoryDTO);
                    function = "updateCategory";
                }else {
                    if(!validateName2()){
                        return;
                    }
                    resultCategory = foodCategoryDAO.addFoodCategory(foodCategoryDTO);
                    function = "addCategory";
                }

                //Thêm, sửa loại dựa theo obj loaimonDTO
                Intent intent = new Intent();
                intent.putExtra("result",resultCategory);
                intent.putExtra("function",function);
                setResult(RESULT_OK,intent);
                finish();
                break;

        }
    }

    //Chuyển ảnh bitmap về mảng byte lưu vào csdl
    private byte[] imageViewtoByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //region validate fields
    private boolean validateImage(){
        BitmapDrawable drawable = (BitmapDrawable)IMG_addcategory_AddImg.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        if(bitmap == bitmapold){
            Toast.makeText(getApplicationContext(),"Xin chọn hình ảnh",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateName(){
        String val = TXTL_addcategory_NameCategory.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addcategory_NameCategory.setError(getResources().getString(R.string.not_empty));
            return false;
        } else {
            TXTL_addcategory_NameCategory.setError(null);
            TXTL_addcategory_NameCategory.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateName2(){
        String val = TXTL_addcategory_NameCategory.getEditText().getText().toString().trim();
        if(foodCategoryDAO.CheckExist(val) != 0){
            TXTL_addcategory_NameCategory.setError(getResources().getString(R.string.foodcategory_exist));
            return false;
        } else {
            TXTL_addcategory_NameCategory.setError(null);
            TXTL_addcategory_NameCategory.setErrorEnabled(false);
            return true;
        }
    }
    //endregion

}