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

import com.project.quanlyorder.Activities.AddCategoryActivity;
import com.project.quanlyorder.Activities.AddZoneActivity;
import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.CustomAdapter.AdapterDisplayZone;
import com.project.quanlyorder.DAO.ZoneDAO;
import com.project.quanlyorder.DTO.ZoneDTO;
import com.project.quanlyorder.R;

import java.util.List;


public class DisplayZoneFragment extends Fragment {

    AdapterDisplayZone adapterDisplayZone;
    FragmentManager fragmentManager;
    GridView gvZone;
    ZoneDAO zoneDAO;
    List<ZoneDTO> zoneDTOList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_zone_layout, container, false);
        setHasOptionsMenu(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Quản lý thực đơn</font>"));
        fragmentManager = getActivity().getSupportFragmentManager();

        gvZone = view.findViewById(R.id.gvZone);
        zoneDAO = new ZoneDAO(getActivity());
        DisplayZoneList();
        gvZone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idZone = zoneDTOList.get(position).getId();

                DisplayTableFragment displayTableFragment = new DisplayTableFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("idZone",idZone);
                displayTableFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentView,displayTableFragment).addToBackStack("displayZone");
                fragmentTransaction.commit();
            }
        });
        registerForContextMenu(gvZone);
        return view;
    }

    //hiển thị dữ liệu trên gridview
    private void DisplayZoneList(){
        zoneDTOList = zoneDAO.getZoneList();
        adapterDisplayZone = new AdapterDisplayZone(getActivity(),R.layout.custom_layout_displayzone,zoneDTOList);
        gvZone.setAdapter(adapterDisplayZone);
        adapterDisplayZone.notifyDataSetChanged();
    }

    //khởi tạo nút thêm loại
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddCategory = menu.add(1,R.id.itAddZone,1,R.string.addZone);
        itAddCategory.setIcon(R.drawable.ic_baseline_add_24);
        itAddCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }

    ActivityResultLauncher<Intent> resultLauncherAddZone= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        boolean resultCategory = intent.getBooleanExtra("result",false);
                        String function = intent.getStringExtra("function");
                        if(function.equals("addZone"))
                        {
                            if(resultCategory){
                                DisplayZoneList();
                                Toast.makeText(getActivity(),"Thêm thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Thêm thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }else if(function.equals("updateZone")){
                            if(resultCategory){
                                DisplayZoneList();
                                Toast.makeText(getActivity(),"Sửa thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Sửa thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });

    //xử lý nút thêm khu vuc
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itAddZone:
                Intent intent = new Intent(getActivity(), AddZoneActivity.class);
                resultLauncherAddZone.launch(intent);
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
        int idZone = zoneDTOList.get(pos).getId();

        switch (id){
            case R.id.itEdit:
                Intent iEdit = new Intent(getActivity(), AddZoneActivity.class);
                iEdit.putExtra("idZone",idZone);
                resultLauncherAddZone.launch(iEdit);
                break;

            case R.id.itDelete:
                boolean chk = zoneDAO.deleteZone(idZone);
                if(chk){
                    DisplayZoneList();
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