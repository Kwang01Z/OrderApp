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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.project.quanlyorder.DAO.FoodCategoryDAO;
import com.project.quanlyorder.DAO.FoodDAO;
import com.project.quanlyorder.DAO.SaleDAO;
import com.project.quanlyorder.DTO.FoodDTO;
import com.project.quanlyorder.DTO.SaleDTO;
import com.project.quanlyorder.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener {

    Button BTN_addfood_AddFood;
    LinearLayout layoutStatus;
    RadioGroup RG_addfood_StatusFood;
    RadioButton RB_addfood_foodexist, RB_addfood_foodnotexist;
    ImageView IMG_addfood_back, IMG_addfood_AddImg;
    TextView TXT_addfood_FunctionTitle;
    TextInputLayout TXTL_addfood_NameFood , TXTL_addfood_Price, TXTL_addfood_Category;
    Spinner Spinner_addfood_Sale;
    FoodDAO foodDAO;
    int idFood, idCategory, idSale;
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
                            IMG_addfood_AddImg.setImageBitmap(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_layout);

        foodDAO = new FoodDAO(this);
        //region Lấy đối tượng view
        BTN_addfood_AddFood = (Button)findViewById(R.id.btn_addFood_AddFood) ;
        IMG_addfood_back = (ImageView) findViewById(R.id.img_addFood_back) ;
        IMG_addfood_AddImg = (ImageView)findViewById(R.id.img_addFood_AddImg);
        TXT_addfood_FunctionTitle = (TextView) findViewById(R.id.txt_addFood_FunctionTilte) ;
        TXTL_addfood_NameFood = (TextInputLayout) findViewById(R.id.txtl_addFood_NameFood);
        TXTL_addfood_Price = (TextInputLayout) findViewById(R.id.txtl_addFood_PriceFood);
        Spinner_addfood_Sale = (Spinner) findViewById(R.id.spinner_addfood_Sale);
        TXTL_addfood_Category = (TextInputLayout)findViewById(R.id.txtl_addFood_CategoryFood);
        layoutStatus = (LinearLayout)findViewById(R.id.layout_StatusFood);
        RG_addfood_StatusFood = (RadioGroup)findViewById(R.id.rg_addFood_StatusFood);
        RB_addfood_foodexist = (RadioButton)findViewById(R.id.rd_addFood_FoodExist);
        RB_addfood_foodnotexist = (RadioButton)findViewById(R.id.rd_addFood_FoodNotExist);
        //end region

        BitmapDrawable olddrawable = (BitmapDrawable)IMG_addfood_AddImg.getDrawable();
        bitmapold = olddrawable.getBitmap();

        //region Hiển thị trang sửa nếu được chọn từ context menu sửa
        idFood = getIntent().getIntExtra("idFood",0);
        if(idFood != 0){
            TXT_addfood_FunctionTitle.setText(getResources().getString(R.string.editFood));
            FoodDTO foodDTO = foodDAO.getFoodById(idFood);

            //Hiển thị lại thông tin từ csdl
            TXTL_addfood_NameFood.getEditText().setText(foodDTO.getName());
            TXTL_addfood_Price.getEditText().setText(String.valueOf(foodDTO.getPrice()));

            byte[] Foodimage = foodDTO.getImg();
            Bitmap bitmap = BitmapFactory.decodeByteArray(Foodimage,0,Foodimage.length);
            IMG_addfood_AddImg.setImageBitmap(bitmap);

            BTN_addfood_AddFood.setText(getResources().getString(R.string.editFood));
            TXTL_addfood_Category.getEditText().setClickable(true);
            layoutStatus.setVisibility(View.VISIBLE);

        }
        //endregion

        idCategory = getIntent().getIntExtra("idCategory",-1);
        if(idCategory != -1){
            FoodCategoryDAO foodCategoryDAO = new FoodCategoryDAO(this);
            String sCategory = foodCategoryDAO.getFoodCategoryById(idCategory).getName();
            TXTL_addfood_Category.getEditText().setText(sCategory);
        } else {
            TXTL_addfood_Category.getEditText().setText("Unknow");
        }
        List<String> saleNameList = new ArrayList<>();
        SaleDAO saleDAO =new SaleDAO(this);
        saleNameList = saleDAO.getSaleNameList();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,saleNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        Spinner_addfood_Sale.setAdapter(adapter);
        idSale = foodDAO.getFoodById(idFood).getIdSale();
        Spinner_addfood_Sale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameSale =  Spinner_addfood_Sale.getSelectedItem().toString();
                idSale = saleDAO.getSaleByName(nameSale).getId();
                //Log.e("-----","spinner click");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        IMG_addfood_back.setOnClickListener(this);
        IMG_addfood_AddImg.setOnClickListener(this);
        BTN_addfood_AddFood.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        boolean resultFood;
        String function;
        switch (id){
            case R.id.img_addFood_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //animation
                break;

            case R.id.img_addFood_AddImg:
                Intent iGetIMG = new Intent();
                iGetIMG.setType("image/*"); //lấy những mục chứa hình ảnh
                iGetIMG.setAction(Intent.ACTION_GET_CONTENT);   //lấy mục hiện tại đang chứa hình
                resultLauncherOpenIMG.launch(Intent.createChooser(iGetIMG,getResources().getString(R.string.choseimg)));    //mở intent chọn hình ảnh
                break;

            case R.id.btn_addFood_AddFood:
                if(!validateImage() | !validateName() | !validatePrice()){
                    return;
                }

                String sFoodName = TXTL_addfood_NameFood.getEditText().getText().toString().trim();
                String sFoodPrice = TXTL_addfood_Price.getEditText().getText().toString().trim();
                FoodDTO foodDTO = new FoodDTO();
                foodDTO.setName(sFoodName);
                foodDTO.setImg(imageViewtoByte(IMG_addfood_AddImg));
                //foodDTO.setStatus(1);
                foodDTO.setPrice(Integer.parseInt(sFoodPrice));
                foodDTO.setIdSale(idSale);

                idFood = getIntent().getIntExtra("idFood",0);
                if (idFood != 0){
                    foodDTO.setId(idFood);
                    FoodCategoryDAO foodCategoryDAO = new FoodCategoryDAO(this);
                    String sFoodCategory = TXTL_addfood_Category.getEditText().toString();
                    foodDTO.setIdCategory(idCategory);

                    switch (RG_addfood_StatusFood.getCheckedRadioButtonId()){
                        case R.id.rd_addFood_FoodNotExist:
                            foodDTO.setStatus(0);
                            break;
                        case R.id.rd_addFood_FoodExist:
                            foodDTO.setStatus(1);
                            break;
                    }
                    resultFood = foodDAO.updateFood(idFood,foodDTO);
                    function = "updateFood";
                }
                else{
                    if(!validateName2()){
                        return;
                    }
                    foodDTO.setIdCategory(idCategory);
                    foodDTO.setStatus(1);
                    resultFood = foodDAO.addFood(foodDTO);
                    function = "addFood";
                }


                //Thêm, sửa loại dựa theo obj loaimonDTO
                Intent intent = new Intent();
                intent.putExtra("result",resultFood);
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
        BitmapDrawable drawable = (BitmapDrawable)IMG_addfood_AddImg.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        if(bitmap == bitmapold){
            Toast.makeText(getApplicationContext(),"Xin chọn hình ảnh",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateName(){
        String val = TXTL_addfood_NameFood.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addfood_NameFood.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_addfood_NameFood.setError(null);
            TXTL_addfood_NameFood.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateName2(){
        String val = TXTL_addfood_NameFood.getEditText().getText().toString().trim();
        if(foodDAO.CheckExist(val) != 0 && !val.equals(foodDAO.getFoodById(idFood).getName())){
            TXTL_addfood_NameFood.setError(getResources().getString(R.string.foodname_exist));
            return false;
        } else {
            TXTL_addfood_NameFood.setError(null);
            TXTL_addfood_NameFood.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePrice(){
        String val = TXTL_addfood_Price.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addfood_Price.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(!val.matches(("\\d+(?:\\.\\d+)?"))){
            TXTL_addfood_Price.setError(getResources().getString(R.string.foodprice_error));
            return false;
        }else {
            TXTL_addfood_Price.setError(null);
            TXTL_addfood_Price.setErrorEnabled(false);
            return true;
        }
    }

    //endregion
}