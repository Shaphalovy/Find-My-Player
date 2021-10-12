package com.example.findmyplayer.Auth;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.findmyplayer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserTypeSelectFragment extends Fragment {


    Button player_btn,hire_btn;

    public UserTypeSelectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_type_select, container, false);

        player_btn = view.findViewById(R.id.player_btn);
        hire_btn = view.findViewById(R.id.hire_btn);

        player_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new SignUpFragment());
            }
        });

        hire_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new SignUpHireFragment());
            }
        });





        return view;
    }
    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container_auth, fragment);
        fragmentTransaction.commit();

    }

}
