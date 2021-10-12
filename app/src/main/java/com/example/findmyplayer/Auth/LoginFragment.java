package com.example.findmyplayer.Auth;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.findmyplayer.MainActivity;
import com.example.findmyplayer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    TextInputEditText email_et, password_et;
    TextView forget_password_tv, create_account_tv;
    Button login_btn;
    FirebaseAuth firebaseAuth;
    private Context context;
    AlertDialog dialog;


    public LoginFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        email_et = view.findViewById(R.id.email_et);
        password_et = view.findViewById(R.id.password_et);
        login_btn = view.findViewById(R.id.login_btn);
        forget_password_tv = view.findViewById(R.id.forget_password_tv);
        create_account_tv = view.findViewById(R.id.create_account_tv);
        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.CustomDialog).build();

        firebaseAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


        create_account_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new UserTypeSelectFragment());
            }
        });
        forget_password_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new ForgetPasswordFragment());
            }
        });

        return view;
    }
    private void loginUser() {

        dialog.show();

        String email = email_et.getText().toString();
        String password = password_et.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            //Email Password is not null
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Login Successful Successful

                        if (firebaseAuth.getCurrentUser().isEmailVerified()){

                            //Email Verified
                            dialog.dismiss();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            getActivity().finish();



                        }
                        else {
                            //Email not Verified
                            dialog.dismiss();
                            Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        // Login Error
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "" + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else {

            //Email Password is null
            dialog.dismiss();
            Toast.makeText(context, "All filed required", Toast.LENGTH_SHORT).show();
        }




    }

    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container_auth, fragment);
        fragmentTransaction.commit();

    }

}
