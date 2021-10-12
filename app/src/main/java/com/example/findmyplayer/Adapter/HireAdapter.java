package com.example.findmyplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmyplayer.PoJo.HirePoJo;
import com.example.findmyplayer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HireAdapter extends RecyclerView.Adapter<HireAdapter.HireViewHolder> {

    private Context context;
    private HireItemClickListener hireItemClickListener;
    private ArrayList<HirePoJo> hirePoJos;
    private DatabaseReference databaseReference;

    public HireAdapter(Context context, HireItemClickListener hireItemClickListener, ArrayList<HirePoJo> hirePoJos) {
        this.context = context;
        this.hireItemClickListener = hireItemClickListener;
        this.hirePoJos = hirePoJos;
        databaseReference = FirebaseDatabase.getInstance().getReference("Hire");
    }

    @NonNull
    @Override
    public HireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.news_feed_single_layout, parent, false);
        return new HireViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HireViewHolder holder, int position) {

        HirePoJo hirePoJo = hirePoJos.get(position);
        holder.hire_tv.setText(hirePoJo.getRecruiterName() + " wants to hire you");

    }

    @Override
    public int getItemCount() {
        return hirePoJos.size();
    }

    public class HireViewHolder extends RecyclerView.ViewHolder {

        TextView hire_tv;
        ImageView delete_iv;

        public HireViewHolder(@NonNull View itemView) {
            super(itemView);

            hire_tv = itemView.findViewById(R.id.hire_tv);
            delete_iv = itemView.findViewById(R.id.delete_iv);

            delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = hirePoJos.get(getAdapterPosition()).getId();
                    databaseReference.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hireItemClickListener.onClickHireItem(hirePoJos.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface HireItemClickListener {

        void onClickHireItem(HirePoJo hirePoJo);
    }

    public void updateHireList(ArrayList<HirePoJo> hirePoJos) {

        this.hirePoJos = hirePoJos;
        notifyDataSetChanged();

    }
}
