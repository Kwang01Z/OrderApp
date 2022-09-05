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

import com.project.quanlyorder.Activities.AddTableActivity;
import com.project.quanlyorder.Activities.AddZoneActivity;
import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.Activities.RegisterActivity;
import com.project.quanlyorder.CustomAdapter.AdapterDisplayTable;
import com.project.quanlyorder.DAO.BillDAO;
import com.project.quanlyorder.DAO.TableFoodDAO;
import com.project.quanlyorder.DAO.ZoneDAO;
import com.project.quanlyorder.DTO.TableFoodDTO;
import com.project.quanlyorder.DTO.ZoneDTO;
import com.project.quanlyorder.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayTableFragment extends Fragment {

    AdapterDisplayTable adapterDisplayTable;
    FragmentManager fragmentManager;
    GridView gvTable;
    TableFoodDAO tablefoodDAO;
    List<TableFoodDTO> tablefoodDTOList;
    List<String> zoneNameList;
    ZoneDAO zoneDAO;
    Spinner spinnerNameZone;
    int idZone,idAcc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_table, container, false);
        setHasOptionsMenu(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Quản lý bàn ăn</font>"));
        fragmentManager = getActivity().getSupportFragmentManager();
        spinnerNameZone = (Spinner)view.findViewById(R.id.spin_displaytable_idzone);
        zoneNameList = new ArrayList<>();
        zoneDAO = new ZoneDAO(getActivity());
        zoneNameList = zoneDAO.getZoneNameList();

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,zoneNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        Bundle bundle = getArguments();

        spinnerNameZone.setAdapter(adapter);
        if (bundle != null){
            idAcc = bundle.getInt("idAccount");
            idZone = bundle.getInt("idZone");
            String nameZone = zoneDAO.getZoneById(idZone).getName();
            int posSpin = zoneNameList.indexOf(nameZone);
            spinnerNameZone.setSelection(posSpin);
            if(idZone == 0){
                setHasOptionsMenu(false);
                nameZone = zoneDAO.getZoneById(1).getName();
                posSpin = zoneNameList.indexOf(nameZone);
                spinnerNameZone.setSelection(posSpin);
            }

        } else {
            setHasOptionsMenu(false);
        }

        spinnerNameZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameZone =  spinnerNameZone.getSelectedItem().toString();
                idZone = zoneDAO.getIdZoneByName(nameZone);
                DisplayTableList(idZone);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        gvTable = view.findViewById(R.id.gvDisplayTable);
        tablefoodDAO = new TableFoodDAO(getActivity());
        DisplayTableList(idZone);
        registerForContextMenu(gvTable);
        return view;
    }
    public void CancelBillifTableFree(){
        BillDAO billDAO = new BillDAO(getActivity());
        billDAO.cancelBillifTableFree();
    }
    @Override
    public void onResume() {
        super.onResume();
        DisplayTableList(idZone);
    }
    //hiển thị dữ liệu trên gridview
    private void DisplayTableList(int idZone){
        CancelBillifTableFree();
        tablefoodDTOList = tablefoodDAO.getTableList(idZone);
        adapterDisplayTable = new AdapterDisplayTable(getActivity(),R.layout.custom_layout_displaytable,tablefoodDTOList,idAcc);
        gvTable.setAdapter(adapterDisplayTable);
        adapterDisplayTable.notifyDataSetChanged();
    }

    //khởi tạo nút thêm bàn
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddTable = menu.add(1,R.id.itAddTable,1,R.string.addTable);
        itAddTable.setIcon(R.drawable.ic_baseline_add_24);
        itAddTable.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }

    ActivityResultLauncher<Intent> resultLauncherAddTable= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        boolean resultCategory = intent.getBooleanExtra("result",false);
                        String function = intent.getStringExtra("function");
                        if(function.equals("addTable"))
                        {
                            if(resultCategory){
                                DisplayTableList(idZone);
                                Toast.makeText(getActivity(),"Thêm thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Thêm thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }else if(function.equals("updateTable")){
                            if(resultCategory){
                                DisplayTableList(idZone);
                                Toast.makeText(getActivity(),"Sửa thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Sửa thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });

    //xử lý nút thêm bàn ăn
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itAddTable:
                Intent intent = new Intent(getActivity(), AddTableActivity.class);
                intent.putExtra("idZone",idZone);
                resultLauncherAddTable.launch(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //hiển thị contextmenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu, menu);
    }

    //xử lí context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = menuInfo.position;
        int idTable = tablefoodDTOList.get(pos).getId();

        switch (id){
            case R.id.itEdit:
                Intent iEdit = new Intent(getActivity(), AddTableActivity.class);
                iEdit.putExtra("idTable",idTable);
                iEdit.putExtra("idZone",idZone);
                resultLauncherAddTable.launch(iEdit);
                break;

            case R.id.itDelete:
                boolean chk = tablefoodDAO.deleteTableFood(idTable);
                if(chk){
                    DisplayTableList(idZone);
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