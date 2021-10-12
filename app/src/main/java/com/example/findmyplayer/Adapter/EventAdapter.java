package com.example.findmyplayer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmyplayer.MainActivity;
import com.example.findmyplayer.PoJo.EventPoJo;
import com.example.findmyplayer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<EventPoJo>eventPoJos;
    private Context context;
    private EventClickListener eventClickListener;
    private DatabaseReference databaseReference;
    private String userId;

    public EventAdapter(ArrayList<EventPoJo> eventPoJos, Context context, EventClickListener eventClickListener) {
        this.eventPoJos = eventPoJos;
        this.context = context;
        this.eventClickListener = eventClickListener;
        databaseReference = FirebaseDatabase.getInstance().getReference("Event");
        userId = MainActivity.userId;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.event_single_layout,parent,false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        final EventPoJo eventPoJo = eventPoJos.get(position);

        holder.sports_tv.setText(eventPoJo.getSports());
        holder.location_tv.setText(eventPoJo.getLocation());
        holder.date_tv.setText(eventPoJo.getDate());
        if (userId.equals(eventPoJo.getEventCreatorId())){
            holder.delete_iv.setVisibility(ImageView.VISIBLE);
            holder.delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Nothing Change
                    databaseReference.child(eventPoJo.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
        else {
            holder.delete_iv.setVisibility(ImageView.INVISIBLE);
        }

        try {
            final Uri uri = Uri.parse(eventPoJo.getDownloadUrl());
            Picasso.get().load(uri).into(holder.bannerIv);


        }
        catch (Exception e){}

    }

    @Override
    public int getItemCount() {
        return eventPoJos.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView sports_tv,location_tv,date_tv;
        ImageView bannerIv,delete_iv;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            sports_tv = itemView.findViewById(R.id.sports_tv);
            location_tv = itemView.findViewById(R.id.location_tv);
            bannerIv = itemView.findViewById(R.id.bannerIv);
            date_tv = itemView.findViewById(R.id.date_tv);
            delete_iv = itemView.findViewById(R.id.delete_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventClickListener.onClickEvent(eventPoJos.get(getAdapterPosition()));
                }
            });

        }
    }
    public interface EventClickListener{
        void onClickEvent(EventPoJo eventPoJo);
    }

    public void updateData(ArrayList<EventPoJo>eventPoJos){

        this.eventPoJos = eventPoJos;
        notifyDataSetChanged();

    };
}
