package com.example.findmyplayer.Navigation;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findmyplayer.Adapter.HireAdapter;
import com.example.findmyplayer.Adapter.InterestedAdapter;
import com.example.findmyplayer.MainActivity;
import com.example.findmyplayer.Navigation.Profile.ProfileFragment;
import com.example.findmyplayer.Navigation.Profile.ProfileHireFragment;
import com.example.findmyplayer.PoJo.HirePoJo;
import com.example.findmyplayer.PoJo.InterestedPoJo;
import com.example.findmyplayer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment
        implements HireAdapter.HireItemClickListener,InterestedAdapter.InterestedItemClickListener {

    RecyclerView hire_rv;
    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private ArrayList<HirePoJo> hirePoJos;
    private ArrayList<InterestedPoJo>interestedPoJos;
    private HireAdapter hireAdapter;
    private InterestedAdapter interestedAdapter;
    android.app.AlertDialog dialog;
    String userType;

    public NewsFeedFragment() {
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
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        hire_rv = view.findViewById(R.id.hire_rv);

        userType = MainActivity.userType;

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        //Loading Dialog
        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.CustomDialog).build();

        //RecyclerView.............................
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        hire_rv.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.set(0, 0, 0, 20);
            }
        };
        hire_rv.addItemDecoration(itemDecoration);

        if (userType.equals("Player")){
            databaseReference = FirebaseDatabase.getInstance().getReference("Hire");
            hirePoJos = new ArrayList<>();
            hireAdapter = new HireAdapter(context, this, hirePoJos);
            hire_rv.setAdapter(hireAdapter);
            getHireDataFromFireBase();
        }

        else {
            databaseReference = FirebaseDatabase.getInstance().getReference("Interested");
            interestedPoJos = new ArrayList<>();
            interestedAdapter = new InterestedAdapter(context,interestedPoJos,this);
            hire_rv.setAdapter(interestedAdapter);
            getInterestedDataFromFireBase();

        }






        //..............................RecyclerView


        return view;
    }

    private void getInterestedDataFromFireBase() {

        dialog.show();
        databaseReference.orderByChild("eventCreatorId").equalTo(MainActivity.userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    interestedPoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()){


                        InterestedPoJo interestedPoJo = data.getValue(InterestedPoJo.class);
                        interestedPoJos.add(interestedPoJo);

                    }
                    interestedAdapter.updateInterestList(interestedPoJos);
                    dialog.dismiss();
                }
                else {
                    interestedPoJos.clear();
                    interestedAdapter.updateInterestList(interestedPoJos);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getHireDataFromFireBase() {

        dialog.show();
        databaseReference.orderByChild("playerId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    hirePoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()){


                        HirePoJo hirePoJo = data.getValue(HirePoJo.class);
                        hirePoJos.add(hirePoJo);

                    }
                    hireAdapter.updateHireList(hirePoJos);
                    dialog.dismiss();
                }
                else {
                    hirePoJos.clear();
                    hireAdapter.updateHireList(hirePoJos);
                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                dialog.dismiss();
                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClickHireItem(HirePoJo hirePoJo) {

        changeFragment(ProfileHireFragment.getInstance(hirePoJo.getRecruiterId()));

    }
    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_main, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onClickInterestedItem(InterestedPoJo interestedPoJo) {

        changeFragment(ProfileFragment.getInstance(interestedPoJo.getInterestedUserId()));

    }
}
