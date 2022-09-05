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
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
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

import com.project.quanlyorder.Activities.AddSaleActivity;
import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.CustomAdapter.AdapterDisplaySale;
import com.project.quanlyorder.DAO.SaleDAO;
import com.project.quanlyorder.DTO.SaleDTO;
import com.project.quanlyorder.R;

import java.util.List;


public class DisplaySaleListFragment extends Fragment {

    AdapterDisplaySale adapterDisplaySale;
    FragmentManager fragmentManager;
    GridView gvSale;
    SaleDAO saleDAO;
    List<SaleDTO> saleDTOList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_sale_list, container, false);
        setHasOptionsMenu(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Quản lý mã giảm giá</font>"));
        fragmentManager = getActivity().getSupportFragmentManager();

        gvSale = view.findViewById(R.id.gvSale);
        saleDAO = new SaleDAO(getActivity());
        DisplaySaleList();
        registerForContextMenu(gvSale);
        return view;
    }

    //hiển thị dữ liệu trên gridview
    private void DisplaySaleList(){
        saleDTOList = saleDAO.getSaleList();
        adapterDisplaySale = new AdapterDisplaySale(getActivity(),R.layout.custom_layout_displaysale,saleDTOList);
        gvSale.setAdapter(adapterDisplaySale);
        adapterDisplaySale.notifyDataSetChanged();
    }

    //khởi tạo nút thêm loại
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddCategory = menu.add(1,R.id.itAddSale,1,R.string.addSale);
        itAddCategory.setIcon(R.drawable.ic_baseline_add_24);
        itAddCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }

    ActivityResultLauncher<Intent> resultLauncherAddSale= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        boolean resultCategory = intent.getBooleanExtra("result",false);
                        String function = intent.getStringExtra("function");
                        if(function.equals("addSale"))
                        {
                            if(resultCategory){
                                DisplaySaleList();
                                Toast.makeText(getActivity(),"Thêm thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Thêm thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }else if(function.equals("updateSale")){
                            if(resultCategory){
                                DisplaySaleList();
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
            case R.id.itAddSale:
                Intent intent = new Intent(getActivity(), AddSaleActivity.class);
                resultLauncherAddSale.launch(intent);
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
        int idSale = saleDTOList.get(pos).getId();

        switch (id){
            case R.id.itEdit:
                Intent iEdit = new Intent(getActivity(), AddSaleActivity.class);
                iEdit.putExtra("idSale",idSale);
                resultLauncherAddSale.launch(iEdit);
                break;

            case R.id.itDelete:
                boolean chk = saleDAO.deleteSale(idSale);
                if(chk){
                    DisplaySaleList();
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