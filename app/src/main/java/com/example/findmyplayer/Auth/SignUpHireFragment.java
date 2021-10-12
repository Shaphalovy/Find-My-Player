package com.example.findmyplayer.Auth;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.findmyplayer.PoJo.ClientPoJo;
import com.example.findmyplayer.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpHireFragment extends Fragment {

    TextInputEditText address_et,phone_et,password_et,email_et,user_name_et,profession_et,organization_et;
    ImageView sign_up_btn_iv;
    Spinner address_sp;
    ArrayAdapter<CharSequence> arrayAdapter;



    private Context context;


    public SignUpHireFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_sign_up_hire, container, false);


        address_et = view.findViewById(R.id.address_et);
        phone_et = view.findViewById(R.id.phone_et);
        password_et = view.findViewById(R.id.password_et);
        email_et = view.findViewById(R.id.email_et);
        user_name_et = view.findViewById(R.id.user_name_et);
        profession_et = view.findViewById(R.id.profession_et);
        organization_et = view.findViewById(R.id.organization_et);

        sign_up_btn_iv = view.findViewById(R.id.sign_up_btn_iv);

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
                continueSignUp();
            }
        });

        return view;
    }

    private void continueSignUp() {

        String name = user_name_et.getText().toString();
        String email = email_et.getText().toString();
        String password = password_et.getText().toString();
        String phone = phone_et.getText().toString();
        String address = address_et.getText().toString();
        String profession = profession_et.getText().toString();
        String organization = organization_et.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(address)&& !TextUtils.isEmpty(profession)
                && !TextUtils.isEmpty(organization)) {

            ClientPoJo clientPoJo = new ClientPoJo(name, email, password, phone, address, profession, organization);
            changeFragment(SignUpHireSecondFragment.getInstance(clientPoJo));
        }
        else {
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
