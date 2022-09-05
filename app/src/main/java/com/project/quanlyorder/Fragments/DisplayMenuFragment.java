package com.project.quanlyorder.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableRow;

import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.R;

public class DisplayMenuFragment extends Fragment {

    FragmentManager fragmentManager;
    TableRow row_TableAndZone, row_Menu, row_Sale;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_menu, container, false);
        setHasOptionsMenu(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Quản lý danh mục</font>"));
        fragmentManager = getActivity().getSupportFragmentManager();

        row_TableAndZone = (TableRow)view.findViewById(R.id.row_menu_TableAndZone);
        row_Menu = (TableRow)view.findViewById(R.id.row_menu_Menu);
        row_Sale = (TableRow)view.findViewById(R.id.row_menu_Sale);

        row_TableAndZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayZoneFragment displayZoneFragment = new DisplayZoneFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.contentView,displayZoneFragment).addToBackStack("displayZone");
                transaction.commit();
            }
        });

        row_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayCategoryFoodFragment displayCategoryFoodFragment = new DisplayCategoryFoodFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.contentView,displayCategoryFoodFragment).addToBackStack("displayCategoryFood");
                transaction.commit();
            }
        });
        row_Sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplaySaleListFragment displaySaleListFragment = new DisplaySaleListFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.contentView,displaySaleListFragment).addToBackStack("displaySaleList");
                transaction.commit();
            }
        });

        return view;
    }

}