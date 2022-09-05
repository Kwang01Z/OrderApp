package com.project.quanlyorder.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.quanlyorder.Activities.HomeActivity;
import com.project.quanlyorder.Activities.WelcomeActivity;
import com.project.quanlyorder.DAO.AccountDAO;
import com.project.quanlyorder.DAO.UserInfoDAO;
import com.project.quanlyorder.DTO.UserInfoDTO;
import com.project.quanlyorder.R;

public class DisplayInfomationFragment extends Fragment {
    TextView i4_FullName, i4_Birthday, i4_Gender, i4_Email, i4_Phonenum, i4_Logout, i4_PosUser;

    int idAcc_current,idAcc;
    AccountDAO accountDAO;
    UserInfoDAO userInfoDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_infomation, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Thông tin</font>"));
        setHasOptionsMenu(false);

        i4_FullName = view.findViewById(R.id.i4_FullName);
        i4_Birthday = view.findViewById(R.id.i4_birthday);
        i4_Gender = view.findViewById(R.id.i4_gender);
        i4_Email = view.findViewById(R.id.i4_Email);
        i4_Phonenum = view.findViewById(R.id.i4_PhoneNum);
        i4_Logout = view.findViewById(R.id.i4_logout);
        i4_PosUser = view.findViewById(R.id.i4_posUser);

        accountDAO = new AccountDAO(getActivity());
        userInfoDAO = new UserInfoDAO(getActivity());
        Bundle bundle = getArguments();
        idAcc_current = bundle.getInt("current_idAccount",0);
        idAcc = bundle.getInt("idAccount",0);
        if (idAcc == 0) {
            UserInfoDTO userInfoDTO = userInfoDAO.getUserInfoById(idAcc_current);
            i4_FullName.setText(userInfoDTO.getFullName());
            i4_Birthday.setText(userInfoDTO.getBirthday());
            i4_Gender.setText(userInfoDTO.getGender());
            i4_PosUser.setText(userInfoDTO.getUserPosition());
            i4_Email.setText(userInfoDTO.getEmail());
            i4_Phonenum.setText(userInfoDTO.getPhoneNumber());
            i4_Logout.setVisibility(View.VISIBLE);
            i4_Logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), WelcomeActivity.class);
                    startActivity(intent);
                }
            });
        }
        else {
            UserInfoDTO userInfoDTO = userInfoDAO.getUserInfoById(idAcc);
            i4_FullName.setText(userInfoDTO.getFullName());
            i4_Birthday.setText(userInfoDTO.getBirthday());
            i4_Gender.setText(userInfoDTO.getGender());
            i4_PosUser.setText(userInfoDTO.getUserPosition());
            i4_Email.setText(userInfoDTO.getEmail());
            i4_Phonenum.setText(userInfoDTO.getPhoneNumber());
            i4_Logout.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}