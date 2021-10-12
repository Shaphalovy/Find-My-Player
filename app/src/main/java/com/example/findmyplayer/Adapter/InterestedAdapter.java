package com.example.findmyplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmyplayer.PoJo.InterestedPoJo;
import com.example.findmyplayer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InterestedAdapter extends RecyclerView.Adapter<InterestedAdapter.InterestedViewHolder> {

    private Context context;
    private ArrayList<InterestedPoJo>interestedPoJos;
    private InterestedItemClickListener interestedItemClickListener;
    private DatabaseReference databaseReference;


    public InterestedAdapter(Context context, ArrayList<InterestedPoJo> interestedPoJos, InterestedItemClickListener interestedItemClickListener) {
        this.context = context;
        this.interestedPoJos = interestedPoJos;
        this.interestedItemClickListener = interestedItemClickListener;
        databaseReference = FirebaseDatabase.getInstance().getReference("Interested");
    }

    @NonNull
    @Override
    public InterestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.interested_single_layout,parent,false);

        return new InterestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestedViewHolder holder, int position) {

        final InterestedPoJo interestedPoJo = interestedPoJos.get(position);
        holder.interest_tv.setText(interestedPoJo.getInterestedUserName()
                +" interested about event of "+interestedPoJo.getSports()+" match at "+interestedPoJo.getLocation());
        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child(interestedPoJo.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        }
                        else {

                        }
                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return interestedPoJos.size();
    }

    public class InterestedViewHolder extends RecyclerView.ViewHolder {

        TextView interest_tv;
        ImageView delete_iv;

        public InterestedViewHolder(@NonNull View itemView) {
            super(itemView);

            interest_tv = itemView.findViewById(R.id.interest_tv);
            delete_iv = itemView.findViewById(R.id.delete_iv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interestedItemClickListener.onClickInterestedItem(interestedPoJos.get(getAdapterPosition()));
                }
            });

        }
    }
    public interface InterestedItemClickListener{

        void onClickInterestedItem(InterestedPoJo interestedPoJo);
    }
    public void updateInterestList(ArrayList<InterestedPoJo>interestedPoJos){

        this.interestedPoJos = interestedPoJos;
        notifyDataSetChanged();

    }
}
