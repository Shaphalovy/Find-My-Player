package com.example.findmyplayer.Auth;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.findmyplayer.PoJo.ClientPoJo;
import com.example.findmyplayer.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpHireSecondFragment extends Fragment {


    private Context context;
    private ClientPoJo clientPoJo;
    private static final int GALLERY_IMAGE_CODE = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;
    private Button sign_up_btn, upload_btn;
    private ImageView profile_iv;
    private EditText description_et;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseStorage storageReference;

    String downloadUrl;
    android.app.AlertDialog dialog;



    public static SignUpHireSecondFragment getInstance(ClientPoJo clientPoJo){

        Bundle bundle = new Bundle();
        bundle.putSerializable("clientPoJo",clientPoJo);
        SignUpHireSecondFragment signUpHireSecondFragment = new SignUpHireSecondFragment();
        signUpHireSecondFragment.setArguments(bundle);

        return signUpHireSecondFragment;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public SignUpHireSecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up_hire_second, container, false);

        try {

             clientPoJo = (ClientPoJo) getArguments().getSerializable("clientPoJo");

        }catch (Exception e){

        }
        sign_up_btn = view.findViewById(R.id.sign_up_btn);
        upload_btn = view.findViewById(R.id.upload_btn);
        profile_iv = view.findViewById(R.id.profile_iv);
        description_et = view.findViewById(R.id.description_et);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("PlayerInfo");

        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.CustomDialog).build();

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery(GALLERY_IMAGE_CODE);
                } else {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    return;
                }
            }
        });
        profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery(GALLERY_IMAGE_CODE);
                } else {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    return;
                }


            }
        });
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String description = description_et.getText().toString();

                if (!TextUtils.isEmpty(description) && !TextUtils.isEmpty(downloadUrl)){

                    clientPoJo.setProfile_img_url(downloadUrl);

                    clientPoJo.setDescription(description);
                    clientPoJo.setUserType("Client");
                    signUpUser();
                    dialog.show();
                }
                else {
                    Toast.makeText(context, "All fields are require", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
    private void getImageFromGallery(final int GALLERY_PICK_CODE) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_PICK_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_CODE && resultCode == RESULT_OK) {

            //After Getting Image

            Uri uri = data.getData();
            profile_iv.setImageURI(uri);

            uploadImage(uri);
        }
    }
    private void uploadImage(Uri uri) {

        //Upload Profile Image
        dialog.show();
        final StorageReference filePath = storageReference.getReference()
                .child("ProfilePhoto").child("Hire").child(uri.getLastPathSegment());
        filePath.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                // Continue with the task to get the download URL
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Uri downloadUri = task.getResult();
                    downloadUrl = String.valueOf(downloadUri);
                    //  String photoId = databaseReference.push().getKey();
                    Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show();

                } else {
                    // Handle failures
                    // ...
                    dialog.dismiss();
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    private void signUpUser() {

        firebaseAuth.createUserWithEmailAndPassword(clientPoJo.getEmail(), clientPoJo.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser firebaseUser = task.getResult().getUser();
                    String userId = firebaseUser.getUid();
                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Registered Successfully"
                                        + " please verify your email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    updateFireBaseUserProfile(firebaseUser);
                    clientPoJo.setId(userId);


                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateFireBaseUserProfile(FirebaseUser firebaseUser) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(clientPoJo.getName())
                .setPhotoUri(Uri.parse(downloadUrl))
                .build();
        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    saveUserInfoInDatabase();
                } else {
                    dialog.dismiss();
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void saveUserInfoInDatabase() {


        databaseReference.child(clientPoJo.getId()).setValue(clientPoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    firebaseAuth.signOut();
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                    //changeFragment(new LoginFragment());
                } else {
                    dialog.dismiss();
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
