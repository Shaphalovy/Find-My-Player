package com.example.findmyplayer.Navigation.Events;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findmyplayer.MainActivity;
import com.example.findmyplayer.PoJo.EventPoJo;
import com.example.findmyplayer.PoJo.InterestedPoJo;
import com.example.findmyplayer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventViewFragment extends Fragment {


    private Context context;
    private EventPoJo eventPoJo;
    private TextView match_tv, location_tv, date_tv, time_tv, description_tv, invite_remaining_tv;
    private ImageView bannerIv;
    private Button interested_btn;
    private DatabaseReference databaseReference, eventDatabaseReference;
    private String evenId;
    private boolean isInterested;


  /*  public static EventViewFragment getInstance(String evenId) {

        Bundle bundle = new Bundle();
        bundle.putString("evenId", evenId);
        EventViewFragment eventViewFragment = new EventViewFragment();
        eventViewFragment.setArguments(bundle);
        return eventViewFragment;
    }*/

    public static EventViewFragment getInstance(EventPoJo eventPoJo) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("eventPoJo", eventPoJo);
        EventViewFragment eventViewFragment = new EventViewFragment();
        eventViewFragment.setArguments(bundle);
        return eventViewFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public EventViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_view, container, false);

        try {
            //evenId = getArguments().getString("evenId");
            eventPoJo = (EventPoJo) getArguments().getSerializable("eventPoJo");
            //setLayoutAsEvent(eventPoJo);
        } catch (Exception e) {

            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }

        match_tv = view.findViewById(R.id.match_tv);
        location_tv = view.findViewById(R.id.location_tv);
        date_tv = view.findViewById(R.id.date_tv);
        time_tv = view.findViewById(R.id.time_tv);
        description_tv = view.findViewById(R.id.description_tv);
        interested_btn = view.findViewById(R.id.interested_btn);
        bannerIv = view.findViewById(R.id.bannerIv);
        invite_remaining_tv = view.findViewById(R.id.invite_remaining_tv);

        databaseReference = FirebaseDatabase.getInstance().getReference("Interested");
        eventDatabaseReference = FirebaseDatabase.getInstance().getReference("Event");

        setLayoutAsEvent();

        checkPreviousInterest();

        if (MainActivity.userType.equals("Client")) {
            interested_btn.setVisibility(Button.INVISIBLE);
        }

        interested_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInterested){

                    Toast.makeText(context, "You are already interested In this event", Toast.LENGTH_SHORT).show();

                }
                else {
                    if (Integer.valueOf(eventPoJo.getLimit())>0){
                        interestedInEvent();
                    }
                    else {
                        Toast.makeText(context, "Remaining Invitation Limit Finished", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        return view;
    }

    private void checkPreviousInterest() {

        databaseReference.orderByChild("user_event").equalTo(MainActivity.userId+"_"+eventPoJo.getId()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            isInterested = true;
                            interested_btn.setBackgroundColor(getResources().getColor(R.color.green_light));
                            interested_btn.setClickable(false);
                        }
                        else {
                            isInterested = false;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

  /*  private void getEventData() {

        eventDatabaseReference.child(evenId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    eventPoJo = (EventPoJo) dataSnapshot.getValue();
                    setLayoutAsEvent(eventPoJo);

                } else {
                    Toast.makeText(context, "Something Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    private void interestedInEvent() {

        String id = databaseReference.push().getKey();
        String user_event = MainActivity.userId+"_"+eventPoJo.getId();
                InterestedPoJo interestedPoJo = new InterestedPoJo(id, eventPoJo.getId(),
                eventPoJo.getEventCreatorId(), MainActivity.userId, MainActivity.userName,
                        eventPoJo.getSports(), eventPoJo.getLocation(),user_event);

        databaseReference.child(id).setValue(interestedPoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(context, "Your interest will be shown to event creator", Toast.LENGTH_SHORT).show();

                    updateEventInterest();
                } else {
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateEventInterest() {

        int limit = Integer.valueOf(eventPoJo.getLimit());
        final int updatedLimit = limit - 1;
        interested_btn.setBackgroundColor(getResources().getColor(R.color.green_light));
        interested_btn.setClickable(false);
       // eventPoJo.setLimit(String.valueOf(updatedLimit));
        eventDatabaseReference.child(eventPoJo.getId()).child("limit").setValue(String.valueOf(updatedLimit)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    invite_remaining_tv.setText("Invitation Remaining : " + String.valueOf(updatedLimit));

                } else {
                    Toast.makeText(context, "Event limit not updated", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void setLayoutAsEvent() {

        try {
            final Uri uri = Uri.parse(eventPoJo.getDownloadUrl());
            Picasso.get().load(uri).into(bannerIv);


        } catch (Exception e) {
            Toast.makeText(context, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        match_tv.setText(eventPoJo.getSports() + " Match");
        location_tv.setText("Location : " + eventPoJo.getLocation());
        date_tv.setText("Date : " + eventPoJo.getDate());
        time_tv.setText("Time : " + eventPoJo.getTime());
        description_tv.setText("Description : " + eventPoJo.getDescription());
        invite_remaining_tv.setText("Invitation Remaining : " + eventPoJo.getLimit());

    }

}
