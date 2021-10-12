package com.example.findmyplayer.Navigation.Profile;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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
public class ProfileFragment extends Fragment {


    private Context context;

    private ImageView profile_iv;
    private ImageView dialogProfile_iv;
    private TextView name_tv, email_tv,price_tv,
            phone_tv, address_tv, gender_tv, sports_tv, category_tv, history_tv;

    private Button edit_btn;
    private TextView edit_basic_tv, edit_contact_tv;

    private String userId;

    private DatabaseReference databaseReference;
    private static final int GALLERY_IMAGE_CODE = 1;

    UserPoJo userPoJo;
    AlertDialog.Builder builder;
    private android.app.AlertDialog dialog;
    String downloadUrl = "";
    private FirebaseStorage storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private ArrayAdapter<CharSequence> sportsArrayAdapter,
            cricketPlayerArrayAdapter, footballPlayerArrayAdapter, genderArrayAdapter;

    String sports, playerType, gender;

    LinearLayout phone_layout, email_layout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment getInstance(String userId) {

        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        return profileFragment;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_iv = view.findViewById(R.id.profile_iv);
        name_tv = view.findViewById(R.id.name_tv);
        email_tv = view.findViewById(R.id.email_tv);
        phone_tv = view.findViewById(R.id.phone_tv);
        address_tv = view.findViewById(R.id.address_tv);
        gender_tv = view.findViewById(R.id.gender_tv);
        sports_tv = view.findViewById(R.id.sports_tv);
        category_tv = view.findViewById(R.id.category_tv);
        history_tv = view.findViewById(R.id.history_tv);
        edit_btn = view.findViewById(R.id.edit_btn);
        edit_basic_tv = view.findViewById(R.id.edit_basic_tv);
        edit_contact_tv = view.findViewById(R.id.edit_contact_tv);
        price_tv = view.findViewById(R.id.price_tv);

        email_layout = view.findViewById(R.id.email_layout);
        phone_layout = view.findViewById(R.id.phone_layout);

        try {
            userId = getArguments().getString("userId");
        } catch (Exception e) {
        }

        if (!userId.equals(MainActivity.userId)) {
            edit_btn.setVisibility(Button.INVISIBLE);
            edit_basic_tv.setVisibility(TextView.INVISIBLE);
            edit_contact_tv.setVisibility(TextView.INVISIBLE);

        }

        sportsArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.sports, android.R.layout.simple_list_item_1);
        sportsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cricketPlayerArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.cricket_player_type, android.R.layout.simple_list_item_1);
        cricketPlayerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        footballPlayerArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.football_player_type, android.R.layout.simple_list_item_1);
        footballPlayerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.gender_type, android.R.layout.simple_list_item_1);
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.CustomDialog).build();

        storageReference = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        email_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", userPoJo.getEmail(), "null"));

                startActivity(Intent.createChooser(emailIntent, "send email"));

            }
        });
        phone_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + userPoJo.getPhone()));
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


        databaseReference = FirebaseDatabase.getInstance().getReference("PlayerInfo");

        getDataFromFireBase();


        return view;
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
        email_et.setText(userPoJo.getEmail());

        final TextInputEditText phone_et = builder_view.findViewById(R.id.phone_et);

        phone_et.setText(userPoJo.getPhone());

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

                address_et.setText(userPoJo.getAddress());
            }
        });

        final Button done_btn = builder_view.findViewById(R.id.done_btn);

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                String email = email_et.getText().toString();
                String phone = phone_et.getText().toString();
                String address = address_et.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(address)){

                    UserPoJo userPoJoContact = userPoJo;
                    userPoJoContact.setEmail(email);
                    userPoJoContact.setPhone(phone);
                    userPoJoContact.setAddress(address);

                    if (userPoJoContact.getSports().equals("Kabaddi") || userPoJoContact.getSports().equals("Gollachhut"))

                    {
                        userPoJoContact.setGame_role_address(userPoJoContact.getSports()
                                + "_" + userPoJoContact.getAddress());

                        editContactInformationContinue(userPoJoContact);
                    }
                    else {


                        userPoJoContact.setGame_role_address(userPoJoContact.getSports()
                                + "_" + userPoJoContact.getPlayerType() + "_" + userPoJoContact.getAddress());

                        editContactInformationContinue(userPoJoContact);

                    }

                }


            }
        });


    }

    private void editContactInformationContinue(UserPoJo userPoJoContact) {

        dialog.show();

        databaseReference.child(userId).setValue(userPoJoContact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(context, "Contact Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

    }

    private void editBasicInformation() {

        builder = new AlertDialog.Builder(context);
        View builder_view = LayoutInflater.from(context).inflate(R.layout.user_basic_change_layout, null, false);
        builder.setView(builder_view);

        final AlertDialog dialog = builder.show();

        final Spinner gender_sp = builder_view.findViewById(R.id.gender_sp);
        gender_sp.setAdapter(genderArrayAdapter);
        final Spinner sports_sp = builder_view.findViewById(R.id.sports_sp);
        sports_sp.setAdapter(sportsArrayAdapter);
        final Spinner player_type_sp = builder_view.findViewById(R.id.player_type_sp);

        final Button done_btn = builder_view.findViewById(R.id.done_btn);
        final TextView player_type_tv = builder_view.findViewById(R.id.player_type_tv);

        sports_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sports = adapterView.getSelectedItem().toString();

                if (sports.equals("Cricket")) {
                    player_type_sp.setAdapter(cricketPlayerArrayAdapter);
                } else if (sports.equals("Football")) {
                    player_type_sp.setAdapter(footballPlayerArrayAdapter);
                } else {
                    player_type_sp.setVisibility(Spinner.GONE);
                    player_type_tv.setVisibility(TextView.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sports = userPoJo.getSports();
            }
        });
       /* player_type_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                playerType = adapterView.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        gender_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = gender_sp.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                gender = userPoJo.getGender();
            }
        });


        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                UserPoJo userPoJoBasic = userPoJo;
                userPoJoBasic.setSports(sports);
                userPoJoBasic.setGender(gender);

                if (userPoJoBasic.getSports().equals("Kabaddi") || userPoJoBasic.getSports().equals("Gollachhut")) {

                    userPoJoBasic.setPlayerType(null);
                    userPoJoBasic.setGame_role_address(userPoJoBasic.getSports()
                           + "_" + userPoJo.getAddress());
                    editBasicInformationContinue(userPoJoBasic);

                } else {
                    String playerType = player_type_sp.getSelectedItem().toString();
                    userPoJoBasic.setPlayerType(playerType);
                    userPoJoBasic.setGame_role_address(userPoJoBasic.getSports()
                            + "_" + playerType+ "_" + userPoJo.getAddress());
                    editBasicInformationContinue(userPoJoBasic);

                }

            }
        });

    }

    private void editBasicInformationContinue(UserPoJo userPoJo) {

        dialog.show();
        databaseReference.child(userId).setValue(userPoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(context, "Basic Info Updated", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT)
                            .show();
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
        final EditText price_et = builder_view.findViewById(R.id.price_et);
        final EditText history_et = builder_view.findViewById(R.id.history_et);
        final Button done_btn = builder_view.findViewById(R.id.done_btn);

        name_et.setText(userPoJo.getName());
        history_et.setText(userPoJo.getHistory());
        price_et.setText(userPoJo.getPrice());

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                String name = name_et.getText().toString();
                String history = history_et.getText().toString();
                String price = price_et.getText().toString();

                UserPoJo userPoJoUpload = userPoJo;
                userPoJoUpload.setName(name);
                userPoJoUpload.setHistory(history);
                userPoJoUpload.setPrice(price);

                if (!downloadUrl.equals("")) {
                    userPoJoUpload.setProfile_img_url(downloadUrl);
                } else {
                }
                updateFireBaseUserProfile(userPoJoUpload);


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

    private void updateFireBaseUserProfile(final UserPoJo userPoJo) {

        dialog.show();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userPoJo.getName())
                .setPhotoUri(Uri.parse(downloadUrl))
                .build();
        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    updateProfile(userPoJo);
                } else {
                    dialog.dismiss();
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateProfile(UserPoJo userPoJoUpload) {

        databaseReference.child(userId).setValue(userPoJoUpload).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                .child("ProfilePhoto").child("Football").child(uri.getLastPathSegment());
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

    //Get Main User Data
    private void getDataFromFireBase() {


        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    userPoJo = dataSnapshot.getValue(UserPoJo.class);
                    setUpLayout(userPoJo);
                } else {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpLayout(UserPoJo userPoJo) {

        Uri uri = Uri.parse(userPoJo.getProfile_img_url());
        Picasso.get().load(uri).into(profile_iv);
        name_tv.setText(userPoJo.getName());
        email_tv.setText(userPoJo.getEmail());
        phone_tv.setText(userPoJo.getPhone());
        address_tv.setText(userPoJo.getAddress());
        gender_tv.setText(userPoJo.getGender());
        sports_tv.setText(userPoJo.getSports());
        category_tv.setText(userPoJo.getPlayerType());
        history_tv.setText(userPoJo.getHistory());
        price_tv.setText(userPoJo.getPrice());


    }

}
