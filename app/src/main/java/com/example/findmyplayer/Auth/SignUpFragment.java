package com.example.findmyplayer.Auth;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.findmyplayer.PoJo.UserPoJo;
import com.example.findmyplayer.R;
import com.google.android.material.textfield.TextInputEditText;


public class SignUpFragment extends Fragment {


    ImageView sign_up_btn_iv;
    RadioButton football_rb,cricket_rb,female_rb,male_rb;
    RadioGroup sports_rg,gender_rg;
    TextInputEditText address_et,phone_et,password_et,email_et,user_name_et;
    private Context context;
    ArrayAdapter<CharSequence> arrayAdapter;
    Spinner address_sp;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        sign_up_btn_iv = view.findViewById(R.id.sign_up_btn_iv);
        football_rb = view.findViewById(R.id.football_rb);
        cricket_rb = view.findViewById(R.id.cricket_rb);
        sports_rg = view.findViewById(R.id.sports_rg);
        female_rb = view.findViewById(R.id.female_rb);
        male_rb = view.findViewById(R.id.male_rb);
        gender_rg = view.findViewById(R.id.gender_rg);
        address_et = view.findViewById(R.id.address_et);
        phone_et = view.findViewById(R.id.phone_et);
        password_et = view.findViewById(R.id.password_et);
        email_et = view.findViewById(R.id.email_et);
        user_name_et = view.findViewById(R.id.user_name_et);

        arrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.location_array, android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Spinner
        address_sp = view.findViewById(R.id.address_sp);
        address_sp.setAdapter(arrayAdapter);

        address_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String location = adapterView.getSelectedItem().toString();
                address_et.setText(location);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sign_up_btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToFormAsUser();

            }
        });


        return view;
    }

    private void goToFormAsUser() {

        String name = user_name_et.getText().toString();
        String email = email_et.getText().toString();
        String password = password_et.getText().toString();
        String phone = phone_et.getText().toString();
        String address = address_et.getText().toString();
        String gender = null,sports = null;
        try {
            int genderSelectedId = gender_rg.getCheckedRadioButtonId();
            RadioButton gender_rv = getView().findViewById(genderSelectedId);
            gender = gender_rv.getText().toString();
            int sports_selectedId = sports_rg.getCheckedRadioButtonId();
            RadioButton sports_rv = getView().findViewById(sports_selectedId);
             sports = sports_rv.getText().toString();
        } catch (Exception e){

        }


        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(address)&& !TextUtils.isEmpty(gender)
                && !TextUtils.isEmpty(sports)){

            //No Empty Field Here
            UserPoJo userPoJo = new UserPoJo(name,email,password,phone,address,gender,sports);
            switch (sports){
                case "Cricket":

                    changeFragment(CricketSignUpFragment.getInstance(userPoJo));

                    break;
                case "Football":

                    changeFragment(FootballSignUpFragment.getInstance(userPoJo));

                    break;
                case "Kabaddi":

                    changeFragment(OtherPlayerSignUpFragment.getInstance(userPoJo));

                    break;
                case "Gollachhut":

                    changeFragment(OtherPlayerSignUpFragment.getInstance(userPoJo));

                    break;
            }



        }
        else {
            //Empty Field Here
            Toast.makeText(context, "All fields required.", Toast.LENGTH_SHORT).show();
        }

    }


    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_auth, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }



}
