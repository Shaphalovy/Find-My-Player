package com.example.findmyplayer.Navigation.Profile;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findmyplayer.MainActivity;
import com.example.findmyplayer.PoJo.ClientPoJo;
import com.example.findmyplayer.PoJo.UserPoJo;
import com.example.findmyplayer.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileHireFragment extends Fragment {

    private Context context;
    private ImageView profile_iv;
    private ImageView dialogProfile_iv;
    private TextView name_tv, email_tv,
            phone_tv, address_tv, description_tv, profession_tv, organization_tv;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private String userId;
    private DatabaseReference databaseReference;
    private FirebaseStorage storageReference;


    private Button edit_btn;
    private TextView edit_basic_tv, edit_contact_tv;

    AlertDialog.Builder builder;
    private android.app.AlertDialog dialog;

    ClientPoJo clientPoJo;
    String downloadUrl = "";

    private static final int GALLERY_IMAGE_CODE = 1;
    LinearLayout phone_layout,email_layout;


    public ProfileHireFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static ProfileHireFragment getInstance(String userId) {

        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        ProfileHireFragment profileHireFragment = new ProfileHireFragment();
        profileHireFragment.setArguments(bundle);
        return profileHireFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_hire, container, false);

        profile_iv = view.findViewById(R.id.profile_iv);
        name_tv = view.findViewById(R.id.name_tv);
        email_tv = view.findViewById(R.id.email_tv);
        phone_tv = view.findViewById(R.id.phone_tv);
        address_tv = view.findViewById(R.id.address_tv);
        profession_tv = view.findViewById(R.id.profession_tv);
        description_tv = view.findViewById(R.id.description_tv);
        organization_tv = view.findViewById(R.id.organization_tv);

        edit_btn = view.findViewById(R.id.edit_btn);
        edit_basic_tv = view.findViewById(R.id.edit_basic_tv);
        edit_contact_tv = view.findViewById(R.id.edit_contact_tv);

        email_layout = view.findViewById(R.id.email_layout);
        phone_layout = view.findViewById(R.id.phone_layout);

        try {
            userId = getArguments().getString("userId");
        } catch (Exception e) {
        }


        if (!userId.equals(MainActivity.userId)){
            edit_btn.setVisibility(Button.INVISIBLE);
            edit_basic_tv.setVisibility(TextView.INVISIBLE);
            edit_contact_tv.setVisibility(TextView.INVISIBLE);

        }
        databaseReference = FirebaseDatabase.getInstance().getReference("PlayerInfo");
        storageReference = FirebaseStorage.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.CustomDialog).build();

        email_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",clientPoJo.getEmail(),"null"));

                startActivity(Intent.createChooser(emailIntent,"send email"));

            }
        });
        phone_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+clientPoJo.getPhone()));
                startActivity(intent);
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImageLayout();
            }
        });

        edit_basic_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editBasicInformation();
            }
        });

        edit_contact_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editContactInformation();
            }
        });

        getDataFromFireBase();

        return view;
    }

    private void editBasicInformation() {

        builder = new AlertDialog.Builder(context);
        View builder_view = LayoutInflater.from(context).inflate(R.layout.basic_change_hire_layout, null, false);
        builder.setView(builder_view);

        final AlertDialog dialog = builder.show();
        final EditText profession_et = builder_view.findViewById(R.id.profession_et);
        profession_et.setText(clientPoJo.getProfession());
        final EditText organization_et = builder_view.findViewById(R.id.organization_et);
        organization_et.setText(clientPoJo.getOrganization());

        final Button done_btn = builder_view.findViewById(R.id.done_btn);

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ClientPoJo clientPoJoContact = clientPoJo;
                clientPoJoContact.setProfession(profession_et.getText().toString());
                clientPoJoContact.setOrganization(organization_et.getText().toString());

                editContactInformationContinue(clientPoJoContact);
            }
        });

    }

    private void editContactInformation() {

        builder = new AlertDialog.Builder(context);
        View builder_view = LayoutInflater.from(context).inflate(R.layout.contact_change_layout, null, false);
        builder.setView(builder_view);

        final AlertDialog dialog = builder.show();


        final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.location_array, android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Spinner
        final TextInputEditText email_et = builder_view.findViewById(R.id.email_et);
        email_et.setText(clientPoJo.getEmail());

        final TextInputEditText phone_et = builder_view.findViewById(R.id.phone_et);

        phone_et.setText(clientPoJo.getPhone());

        final TextInputEditText address_et = builder_view.findViewById(R.id.address_et);
        final Spinner address_sp = builder_view.findViewById(R.id.address_sp);
        address_sp.setAdapter(arrayAdapter);

        address_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String location = adapterView.getSelectedItem().toString();
                address_et.setText(location);
                Toast.makeText(context, "" + location, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                address_et.setText(clientPoJo.getAddress());
            }
        });

        final Button done_btn = builder_view.findViewById(R.id.done_btn);

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ClientPoJo clientPoJoContact = clientPoJo;
                clientPoJoContact.setEmail(email_et.getText().toString());
                clientPoJoContact.setPhone(phone_et.getText().toString());
                clientPoJoContact.setAddress(address_et.getText().toString());

                editContactInformationContinue(clientPoJoContact);
            }
        });


    }
    private void editContactInformationContinue(ClientPoJo clientPoJo) {

        dialog.show();

        databaseReference.child(userId).setValue(clientPoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(context, "Basic Information Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

    }

    private void editImageLayout() {

        builder = new AlertDialog.Builder(context);

        View builder_view = LayoutInflater.from(context).inflate(R.layout.user_image_change_layout, null, false);
        builder.setView(builder_view);
        final AlertDialog dialog = builder.show();
        dialogProfile_iv = builder_view.findViewById(R.id.profile_iv);
        final Button upload_btn = builder_view.findViewById(R.id.upload_btn);
        final EditText name_et = builder_view.findViewById(R.id.name_et);
        final EditText history_et = builder_view.findViewById(R.id.history_et);
        final Button done_btn = builder_view.findViewById(R.id.done_btn);
        final TextView description_tv = builder_view.findViewById(R.id.description_tv);
        description_tv.setText("Description");

        name_et.setText(clientPoJo.getName());
        history_et.setText(clientPoJo.getDescription());

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                String name = name_et.getText().toString();
                String history = history_et.getText().toString();
                ClientPoJo clientPoJoUpload = clientPoJo;
                clientPoJoUpload.setName(name);
                clientPoJoUpload.setDescription(history);
                if (!downloadUrl.equals("")) {
                    clientPoJoUpload.setProfile_img_url(downloadUrl);
                } else {
                }
                updateFireBaseUserProfile(clientPoJoUpload);


            }
        });

        dialogProfile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileImage();
            }
        });
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileImage();
            }
        });
    }

    private void updateFireBaseUserProfile(final ClientPoJo clientPoJo) {

        dialog.show();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(clientPoJo.getName())
                .setPhotoUri(Uri.parse(downloadUrl))
                .build();
        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    updateProfile(clientPoJo);
                } else {
                    dialog.dismiss();
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateProfile(ClientPoJo clientPoJo) {

        databaseReference.child(userId).setValue(clientPoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();

                }
            }
        });

    }

    private void uploadProfileImage() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_CODE && resultCode == RESULT_OK) {

            Uri uri = data.getData();

            dialogProfile_iv.setImageURI(uri);

            uploadImage(uri);
        }
    }

    private void uploadImage(Uri uri) {

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

    private void getDataFromFireBase() {


        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    clientPoJo = dataSnapshot.getValue(ClientPoJo.class);
                    setUpLayout(clientPoJo);
                } else {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpLayout(ClientPoJo clientPoJo) {

        Uri uri = Uri.parse(clientPoJo.getProfile_img_url());
        Picasso.get().load(uri).into(profile_iv);
        name_tv.setText(clientPoJo.getName());
        email_tv.setText(clientPoJo.getEmail());
        phone_tv.setText(clientPoJo.getPhone());
        address_tv.setText(clientPoJo.getAddress());
        profession_tv.setText(clientPoJo.getProfession());
        organization_tv.setText(clientPoJo.getOrganization());
        description_tv.setText(clientPoJo.getDescription());


    }
}
