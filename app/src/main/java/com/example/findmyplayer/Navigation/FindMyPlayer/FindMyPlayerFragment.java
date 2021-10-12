package com.example.findmyplayer.Navigation.FindMyPlayer;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findmyplayer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindMyPlayerFragment extends Fragment {

    private Context context;
    private Spinner address_sp, sports_sp, player_type_sp;
    private Button find_player_btn;
    private ArrayAdapter<CharSequence> addressArrayAdapter,
            sportsArrayAdapter, cricketPlayerArrayAdapter, footballPlayerArrayAdapter;
    String sports, playerType, address;

    private TextView player_type_tv;

    public FindMyPlayerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_find_my_player, container, false);

        address_sp = view.findViewById(R.id.address_sp);
        sports_sp = view.findViewById(R.id.sports_sp);
        player_type_sp = view.findViewById(R.id.player_type_sp);
        find_player_btn = view.findViewById(R.id.find_player_btn);
        player_type_tv = view.findViewById(R.id.player_type_tv);
        //Initialize Array Adapter*****************

        addressArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.location_array, android.R.layout.simple_list_item_1);
        addressArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sportsArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.sports, android.R.layout.simple_list_item_1);
        sportsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cricketPlayerArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.cricket_player_type, android.R.layout.simple_list_item_1);
        cricketPlayerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        footballPlayerArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.football_player_type, android.R.layout.simple_list_item_1);
        footballPlayerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //**********************************Initialize Array Adapter

        address_sp.setAdapter(addressArrayAdapter);
        sports_sp.setAdapter(sportsArrayAdapter);

        address_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                address = adapterView.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                address = "Adabar";
            }
        });

        sports_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sports = adapterView.getSelectedItem().toString();

                if (sports.equals("Cricket")) {
                    player_type_sp.setAdapter(cricketPlayerArrayAdapter);
                } else if (sports.equals("Football")){
                    player_type_sp.setAdapter(footballPlayerArrayAdapter);
                }
                else {
                    player_type_sp.setVisibility(Spinner.INVISIBLE);
                    player_type_tv.setVisibility(TextView.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                sports = "Cricket";
            }
        });
        player_type_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                playerType = adapterView.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                if (sports.equals("Cricket")) {
                    playerType = "Batsman";
                } else {
                   playerType = "Striker";
                }
            }
        });

        find_player_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sports.equals("Cricket") || sports.equals("Football")){
                    changeFragment(PlayerListFragment.getInstance(sports+"_"+playerType+"_"+address));
                }
                else {
                    changeFragment(PlayerListFragment.getInstance(sports+"_"+address));
                }


            }
        });

        return view;
    }
    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


}
