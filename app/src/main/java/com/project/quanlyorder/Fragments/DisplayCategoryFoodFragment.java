package com.project.quanlyorder.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.GridView;
import android.widget.Toast;

import com.project.quanlyorder.Activities.AddCategoryActivity;
import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.CustomAdapter.AdapterDisplayCategory;
import com.project.quanlyorder.DAO.FoodCategoryDAO;
import com.project.quanlyorder.DTO.FoodCategoryDTO;
import com.project.quanlyorder.R;

import java.lang.reflect.Field;
import java.util.List;


public class DisplayCategoryFoodFragment extends Fragment {

    AdapterDisplayCategory adapterDisplayCategory;
    FragmentManager fragmentManager;
    GridView gvCategory;
    FoodCategoryDAO foodCategoryDAO;
    List<FoodCategoryDTO> foodCategoryDTOList;
    int idTable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_category_food, container, false);
        setHasOptionsMenu(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Quản lý thực đơn</font>"));
        fragmentManager = getActivity().getSupportFragmentManager();

        gvCategory = view.findViewById(R.id.gvCategory);
        foodCategoryDAO = new FoodCategoryDAO(getActivity());
        DisplayCategoryFoodList();
        Bundle bundle = getArguments();
        if (bundle!=null) {
            idTable = bundle.getInt("idTable", 0);
            setHasOptionsMenu(false);
        }
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idCategory = foodCategoryDTOList.get(position).getId();

                DisplayFoodListFragment displayFoodListFragment = new DisplayFoodListFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("idCategory",idCategory);
                if(idTable != 0){
                    bundle.putInt("idTable",idTable);
                }
                displayFoodListFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentView,displayFoodListFragment).addToBackStack("displayCategory");
                fragmentTransaction.commit();
            }
        });
        registerForContextMenu(gvCategory);
        return view;
    }

    //hiển thị dữ liệu trên gridview
    private void DisplayCategoryFoodList(){
        foodCategoryDTOList = foodCategoryDAO.getCategoryFoodList();
        adapterDisplayCategory = new AdapterDisplayCategory(getActivity(),R.layout.custom_layout_displaycategory,foodCategoryDTOList);
        gvCategory.setAdapter(adapterDisplayCategory);
        adapterDisplayCategory.notifyDataSetChanged();
    }

    //khởi tạo nút thêm loại
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddCategory = menu.add(1,R.id.itAddCategory,1,R.string.addCategory);
        itAddCategory.setIcon(R.drawable.ic_baseline_add_24);
        itAddCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }

    ActivityResultLauncher<Intent> resultLauncherCategory = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        boolean resultCategory = intent.getBooleanExtra("result",false);
                        String function = intent.getStringExtra("function");
                        if(function.equals("addCategory"))
                        {
                            if(resultCategory){
                                DisplayCategoryFoodList();
                                Toast.makeText(getActivity(),"Thêm thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Thêm thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }else if(function.equals("updateCategory")){
                            if(resultCategory){
                                DisplayCategoryFoodList();
                                Toast.makeText(getActivity(),"Sửa thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Sửa thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });

    //xử lý nút thêm loại
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itAddCategory:
                Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
                resultLauncherCategory.launch(intent);
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
        int idCategory = foodCategoryDTOList.get(pos).getId();

        switch (id){
            case R.id.itEdit:
                Intent iEdit = new Intent(getActivity(), AddCategoryActivity.class);
                iEdit.putExtra("idCategory",idCategory);
                resultLauncherCategory.launch(iEdit);
                break;

            case R.id.itDelete:
                boolean chk = foodCategoryDAO.deleteFoodCtegory(idCategory);
                if(chk){
                    DisplayCategoryFoodList();
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