package com.project.quanlyorder.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.quanlyorder.Activities.AddCategoryActivity;
import com.project.quanlyorder.Activities.AddFoodActivity;
import com.project.quanlyorder.Activities.AmountMenuActivity;
import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.CustomAdapter.AdapterDisplayFood;
import com.project.quanlyorder.DAO.BillDAO;
import com.project.quanlyorder.DAO.FoodCategoryDAO;
import com.project.quanlyorder.DAO.FoodDAO;
import com.project.quanlyorder.DAO.TableFoodDAO;
import com.project.quanlyorder.DTO.FoodDTO;
import com.project.quanlyorder.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayFoodListFragment extends Fragment {

    AdapterDisplayFood adapterDisplayFood;
    FragmentManager fragmentManager;
    GridView gvFood;
    FoodDAO foodDAO;
    List<FoodDTO> foodDTOList;
    List<String> categoryNameList;
    FoodCategoryDAO foodCategoryDAO;
    Spinner spinnerNameCategory;
    int idCategory, idTable = 0,idBill;
//    BillDAO billDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_food_list, container, false);
        setHasOptionsMenu(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Quản lý thực đơn</font>"));
        fragmentManager = getActivity().getSupportFragmentManager();
        spinnerNameCategory = (Spinner)view.findViewById(R.id.spin_displayfood_idcategory);
        categoryNameList = new ArrayList<>();
        foodCategoryDAO = new FoodCategoryDAO(getActivity());
        categoryNameList = foodCategoryDAO.getCategoryNameList();
        gvFood = view.findViewById(R.id.gvFood);
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,categoryNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        Bundle bundle = getArguments();
        spinnerNameCategory.setAdapter(adapter);
//        billDAO = new BillDAO(view.getContext());
        if(bundle != null){
            idCategory = bundle.getInt("idCategory");
            spinnerNameCategory.setSelection(idCategory-1);
            idTable = bundle.getInt("idTable",0);
            if (idTable != 0){
                setHasOptionsMenu(false);
                gvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int statusFood = foodDTOList.get(position).getStatus();
                        if(statusFood == 1){
                                Intent iAmount = new Intent(getActivity(), AmountMenuActivity.class);
                                iAmount.putExtra("idTable",idTable);
                                iAmount.putExtra("idFood",foodDTOList.get(position).getId());
                                startActivity(iAmount);
                            }else {
                                Toast.makeText(getActivity(),getResources().getString(R.string.nofood), Toast.LENGTH_SHORT).show();
                            }
                        DisplayFoodList(idCategory);
                    }
                });
            }
        }
        spinnerNameCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nameCategory =  spinnerNameCategory.getSelectedItem().toString();

                    idCategory = foodCategoryDAO.getIdCategoryByName(nameCategory);

                DisplayFoodList(idCategory);
               // Log.e("-----spin",String.valueOf(idCategory-1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        foodDAO = new FoodDAO(getActivity());
        DisplayFoodList(idCategory);
        registerForContextMenu(gvFood);
        return view;
    }
    public void DisplayFoodList(int idCategory){
        foodDTOList = foodDAO.getListFoodByIdCategory(idCategory);
        adapterDisplayFood = new AdapterDisplayFood(getActivity(),R.layout.custom_layout_displayfood,foodDTOList,idTable);
        gvFood.setAdapter(adapterDisplayFood);
        adapterDisplayFood.notifyDataSetChanged();
    }

    ActivityResultLauncher<Intent> resultLauncherFood = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        boolean resultFood = intent.getBooleanExtra("result",false);
                        String function = intent.getStringExtra("function");
                        if(function.equals("addFood"))
                        {
                            if(resultFood){
                                DisplayFoodList(idCategory);
                                Toast.makeText(getActivity(),"Thêm thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Thêm thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }else if(function.equals("updateFood")){
                            if(resultFood){
                                DisplayFoodList(idCategory);
                                Toast.makeText(getActivity(),"Sửa thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Sửa thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });
    @Override
    public void onResume() {
        super.onResume();
        DisplayFoodList(idCategory);
    }
    //khởi tạo nút thêm loại
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddCategory = menu.add(1,R.id.itAddFood,1,R.string.addFood);
        itAddCategory.setIcon(R.drawable.ic_baseline_add_24);
        itAddCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }
    //xử lý nút thêm món
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itAddFood:
                Intent intent = new Intent(getActivity(), AddFoodActivity.class);
                intent.putExtra("idCategory",idCategory);
                resultLauncherFood.launch(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //hiển thị contextmenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    //xử lí context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = menuInfo.position;
        int idFood = foodDTOList.get(pos).getId();

        switch (id){
            case R.id.itEdit:
                Intent iEdit = new Intent(getActivity(), AddFoodActivity.class);
                iEdit.putExtra("idFood",idFood);
                iEdit.putExtra("idCategory",idCategory);
                resultLauncherFood.launch(iEdit);
                break;

            case R.id.itDelete:
                boolean chk = foodDAO.deleteFood(idFood);
                if(chk){
                    DisplayFoodList(idCategory);
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.delete_sucessful)
                            ,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.delete_failed)
                            ,Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }

}