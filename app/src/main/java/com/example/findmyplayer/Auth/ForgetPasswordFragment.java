package com.example.findmyplayer.Auth;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.findmyplayer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPasswordFragment extends Fragment {

    private Context context;
    private TextInputEditText email_et;
    private Button send_mail_btn;
    private FirebaseAuth firebaseAuth;
    AlertDialog dialog;


    public ForgetPasswordFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_forget_password, container, false);

        email_et = view.findViewById(R.id.email_et);
        send_mail_btn = view.findViewById(R.id.send_mail_btn);

        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.CustomDialog).build();

        firebaseAuth = FirebaseAuth.getInstance();

        send_mail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPasswordResetEmail();
            }
        });


        return view;
    }

    private void sendPasswordResetEmail() {

        String email = email_et.getText().toString();

        if (!TextUtils.isEmpty(email)){

            dialog.show();
            // email not empty
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        dialog.dismiss();
                        Toast.makeText(context, "Email send", Toast.LENGTH_SHORT).show();
                        changeFragment(new LoginFragment());

                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(context, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show();
        }


    }
    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_auth, fragment);
        fragmentTransaction.commit();

    }

}
