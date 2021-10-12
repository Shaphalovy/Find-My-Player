package com.example.findmyplayer.Navigation.Events;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findmyplayer.Adapter.EventAdapter;
import com.example.findmyplayer.MainActivity;
import com.example.findmyplayer.PoJo.EventPoJo;
import com.example.findmyplayer.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment implements EventAdapter.EventClickListener {

    private static final int GALLERY_IMAGE_CODE = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;
    private Context context;
    private FloatingActionButton add_event_fab;
    private RecyclerView event_rv;
    private DatabaseReference databaseReference;

    private static int year, month, day;
    private Calendar calendar;
    private ArrayList<EventPoJo> eventPoJos;
    private EventAdapter eventAdapter;
    ImageView bannerIv;
    TextView nearby_event_tv;

    private android.app.AlertDialog loading_dialog, dialog;

    FirebaseStorage firebaseStorage;

    //Event PoJo
    String downloadUrl;
    String userArea;

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment getInstance(String userArea){

        Bundle bundle = new Bundle();
        bundle.putString("userArea",userArea);
        EventFragment eventFragment = new EventFragment();
        eventFragment.setArguments(bundle);
        return eventFragment;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        try {

            userArea = getArguments().getString("userArea");
        }
        catch (Exception e){}

        Toast.makeText(context, ""+userArea, Toast.LENGTH_SHORT).show();
        nearby_event_tv = view.findViewById(R.id.nearby_event_tv);

        add_event_fab = view.findViewById(R.id.add_event_fab);
        event_rv = view.findViewById(R.id.event_rv);
        databaseReference = FirebaseDatabase.getInstance().getReference("Event");
        firebaseStorage = FirebaseStorage.getInstance();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if (MainActivity.userType.equals("Player")) {
            add_event_fab.setVisibility(FloatingActionButton.INVISIBLE);
        }

        loading_dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.CustomDialog).build();

        eventPoJos = new ArrayList<>();
        eventAdapter = new EventAdapter(eventPoJos, context, this);

        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.set(0, 0, 0, 20);
            }
        };
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        event_rv.addItemDecoration(itemDecoration);
        event_rv.setLayoutManager(layoutManager);
        event_rv.setAdapter(eventAdapter);


        add_event_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createEvent();
            }
        });

        nearby_event_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, ""+userArea, Toast.LENGTH_SHORT).show();
                getNearbyEvent();
            }
        });

        getDataFromDataBase();
        return view;
    }

    private void getNearbyEvent() {

        databaseReference.orderByChild("location").equalTo(userArea).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    eventPoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        EventPoJo eventPoJo = data.getValue(EventPoJo.class);
                        eventPoJos.add(eventPoJo);

                    }
                    eventAdapter.updateData(eventPoJos);
                } else {
                    eventPoJos.clear();
                    eventAdapter.updateData(eventPoJos);
                    Toast.makeText(context, "No Event Exists", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDataFromDataBase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    eventPoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        EventPoJo eventPoJo = data.getValue(EventPoJo.class);
                        eventPoJos.add(eventPoJo);

                    }
                    eventAdapter.updateData(eventPoJos);
                } else {
                    eventAdapter.updateData(eventPoJos);
                    Toast.makeText(context, "No Event Exists", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createEvent() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.create_event_layout, null, false);
        builder.setView(view);
        final AlertDialog dialog = builder.show();


        final EditText date_et = view.findViewById(R.id.date_et);

      /*  final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE dd, MMM, yyyy");
                calendar.set(year,month,dayOfMonth);
                String finalDate = sdf.format(calendar.getTime());
                date_et.setText(finalDate);
            }
        };

        date_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                DatePickerDialog datePickerDialog =new DatePickerDialog(context,dateListener,year,month,day);
                datePickerDialog.show();
                return false;
            }
        });*/


        Button create_event_btn = view.findViewById(R.id.create_event_btn);


        final Spinner sports_sp = view.findViewById(R.id.sports_sp);
        final Spinner address_sp = view.findViewById(R.id.address_sp);
        final EditText time_et = view.findViewById(R.id.time_et);
        final EditText description_et = view.findViewById(R.id.description_et);
        bannerIv = view.findViewById(R.id.bannerIv);
        final EditText limit_et = view.findViewById(R.id.limit_et);

        //Spinner.............................
        ArrayAdapter<CharSequence> addressArrayAdapter, sportsArrayAdapter;
        addressArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.location_array, android.R.layout.simple_list_item_1);
        addressArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sportsArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.sports, android.R.layout.simple_list_item_1);
        sportsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        address_sp.setAdapter(addressArrayAdapter);
        sports_sp.setAdapter(sportsArrayAdapter);
        //......................................Spinner

        bannerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    uploadProfileImage();
                } else {

                    ActivityCompat.requestPermissions(
                            getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    return;
                }


            }
        });

        create_event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = databaseReference.push().getKey();
                String eventCreatorId = MainActivity.userId;
                String date = date_et.getText().toString();
                String sports = sports_sp.getSelectedItem().toString();
                String address = address_sp.getSelectedItem().toString();
                String time = time_et.getText().toString();
                String description = description_et.getText().toString();
                String limit = limit_et.getText().toString();



                if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(eventCreatorId) && !TextUtils.isEmpty(date) &&
                        !TextUtils.isEmpty(sports) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(time) &&
                        !TextUtils.isEmpty(description) && !TextUtils.isEmpty(limit)
                        && !TextUtils.isEmpty(downloadUrl)) {

                    //Not Null
                    EventPoJo eventPoJo = new EventPoJo(id, eventCreatorId, sports, address,
                            date, time, description, downloadUrl, limit);
                    uploadEventData(eventPoJo);
                    dialog.dismiss();
                    loading_dialog.show();

                } else {
                    Toast.makeText(context, "All field are require", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void uploadEventData(EventPoJo eventPoJo) {

        databaseReference.child(eventPoJo.getId()).setValue(eventPoJo).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            loading_dialog.dismiss();

                            Toast.makeText(context, "Event Added", Toast.LENGTH_SHORT).show();
                        } else {
                            loading_dialog.dismiss();
                            Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

            bannerIv.setImageURI(uri);

            uploadImage(uri);
        }
    }

    private void uploadImage(Uri uri) {

        loading_dialog.show();
        final StorageReference filePath = firebaseStorage.getReference()
                .child("EventPhoto").child(uri.getLastPathSegment());
        filePath.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    loading_dialog.dismiss();
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                // Continue with the task to get the download URL
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    loading_dialog.dismiss();
                    Uri downloadUri = task.getResult();
                    downloadUrl = String.valueOf(downloadUri);
                    //  String photoId = databaseReference.push().getKey();
                    Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show();

                } else {
                    // Handle failures
                    // ...
                    loading_dialog.dismiss();
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void onClickEvent(EventPoJo eventPoJo) {

        changeFragment(EventViewFragment.getInstance(eventPoJo));

    }

    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
