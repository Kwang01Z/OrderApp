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

import com.project.quanlyorder.Activities.AddAccountActivity;
import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.CustomAdapter.AdapterDisplayAccount;
import com.project.quanlyorder.DAO.AccountDAO;
import com.project.quanlyorder.DTO.AccountDTO;
import com.project.quanlyorder.R;

import java.util.List;

public class DisplayAccountFragment extends Fragment {
    AdapterDisplayAccount adapterDisplayAccount;
    FragmentManager fragmentManager;
    GridView gvAccount;
    AccountDAO accountDAO;
    List<AccountDTO> accountDTOList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_account, container, false);
        setHasOptionsMenu(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Quản lý tài khoản</font>"));
        fragmentManager = getActivity().getSupportFragmentManager();

        gvAccount = view.findViewById(R.id.gvAccount);
        accountDAO = new AccountDAO(getActivity());
        DisplayAccountList();
        gvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                int idAccount = accountDTOList.get(position).getId();
                int idAcc_current = getArguments().getInt("current_idAccount",0);
                bundle.putInt("idAccount",idAccount);
                bundle.putInt("current_idAccount",idAcc_current);
                DisplayInfomationFragment displayInfomationFragment = new DisplayInfomationFragment();
                displayInfomationFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentView,displayInfomationFragment).addToBackStack("displayInfo");
                fragmentTransaction.commit();
            }
        });
        registerForContextMenu(gvAccount);
        return view;
    }

    //hiển thị dữ liệu trên gridview
    private void DisplayAccountList(){
        accountDTOList = accountDAO.getAccountList();
        adapterDisplayAccount = new AdapterDisplayAccount(getActivity(),R.layout.custom_layout_displayaccount,accountDTOList);
        gvAccount.setAdapter(adapterDisplayAccount);
        adapterDisplayAccount.notifyDataSetChanged();
    }

    //khởi tạo nút thêm acc
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddAccount = menu.add(1,R.id.itAddAccount,1,R.string.addAccount);
        itAddAccount.setIcon(R.drawable.ic_baseline_add_24);
        itAddAccount.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }

    ActivityResultLauncher<Intent> resultLauncherAccount = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        boolean resultAccount = intent.getBooleanExtra("result",false);
                        String function = intent.getStringExtra("function");
                        if(function.equals("addAccount"))
                        {
                            if(resultAccount){
                                DisplayAccountList();
                                Toast.makeText(getActivity(),"Thêm thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Thêm thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }else if(function.equals("updateAccount")){
                            if(resultAccount){
                                DisplayAccountList();
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
            case R.id.itAddAccount:
                Intent intent = new Intent(getActivity(), AddAccountActivity.class);
                resultLauncherAccount.launch(intent);
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
        int idAccount = accountDTOList.get(pos).getId();

        switch (id){
            case R.id.itEdit:
                Intent iEdit = new Intent(getActivity(), AddAccountActivity.class);
                iEdit.putExtra("idAccount",idAccount);
                resultLauncherAccount.launch(iEdit);
                break;

            case R.id.itDelete:
                boolean chk = accountDAO.deleteAccount(idAccount);
                if(chk){
                    DisplayAccountList();
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