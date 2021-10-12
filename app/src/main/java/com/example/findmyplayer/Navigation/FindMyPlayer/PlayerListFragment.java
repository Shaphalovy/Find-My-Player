package com.example.findmyplayer.Navigation.FindMyPlayer;


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

import com.example.findmyplayer.Adapter.PlayerAdapter;
import com.example.findmyplayer.Navigation.Profile.ProfileFragment;
import com.example.findmyplayer.PoJo.UserPoJo;
import com.example.findmyplayer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerListFragment extends Fragment implements PlayerAdapter.OnClickPlayerListener {

    RecyclerView player_rv;
    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private ArrayList<UserPoJo> userPoJos;
    private PlayerAdapter playerAdapter;
    String filter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public PlayerListFragment() {
        // Required empty public constructor
    }

    public static PlayerListFragment getInstance(String filter){

        Bundle bundle = new Bundle();
        bundle.putString("filter",filter);
        PlayerListFragment playerListFragment = new PlayerListFragment();
        playerListFragment.setArguments(bundle);
        return playerListFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_player_list, container, false);

        player_rv = view.findViewById(R.id.player_rv);

        try {
            filter = getArguments().getString("filter");
        }catch (Exception e){}

        databaseReference = FirebaseDatabase.getInstance().getReference("PlayerInfo");
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        userPoJos = new ArrayList<>();

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        player_rv.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.set(0, 0, 0, 20);
            }
        };
        player_rv.addItemDecoration(itemDecoration);
        playerAdapter = new PlayerAdapter(context,userPoJos,this);
        player_rv.setAdapter(playerAdapter);

        getDataFromFireBase();
        return view;
    }

    private void getDataFromFireBase() {
        Toast.makeText(context, ""+filter, Toast.LENGTH_SHORT).show();

        databaseReference.orderByChild("game_role_address").equalTo(filter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    userPoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()){


                        UserPoJo userPoJo = data.getValue(UserPoJo.class);
                        userPoJos.add(userPoJo);

                    }
                    playerAdapter.updatePlayer(userPoJos);
                }
                else {
                    userPoJos.clear();
                    playerAdapter.updatePlayer(userPoJos);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClickPlayer(UserPoJo userPoJo) {

        changeFragment(ProfileFragment.getInstance(userPoJo.getId()));

    }
    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_main, fragment);
        fragmentTransaction.commit();

    }
}
