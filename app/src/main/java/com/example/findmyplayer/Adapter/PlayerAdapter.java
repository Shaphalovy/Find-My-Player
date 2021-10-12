package com.example.findmyplayer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmyplayer.MainActivity;
import com.example.findmyplayer.PoJo.HirePoJo;
import com.example.findmyplayer.PoJo.UserPoJo;
import com.example.findmyplayer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context context;
    private ArrayList<UserPoJo>userPoJos;
    private OnClickPlayerListener onClickPlayerListener;
    private String currentUserId;
    private String currentUserName;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public PlayerAdapter(Context context, ArrayList<UserPoJo> userPoJos, OnClickPlayerListener onClickPlayerListener) {
        this.context = context;
        this.userPoJos = userPoJos;
        this.onClickPlayerListener = onClickPlayerListener;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Hire");
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        currentUserName = firebaseAuth.getCurrentUser().getDisplayName();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.player_single_layout,parent,false);

        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayerViewHolder holder, int position) {

        final UserPoJo userPoJo = userPoJos.get(position);
        holder.name_tv.setText(userPoJo.getName());
        holder.price_tv.setText(userPoJo.getPrice()+ " Taka");
        try {
            final Uri uri = Uri.parse(userPoJo.getProfile_img_url());
            Picasso.get().load(uri).into(holder.profile_iv);
        }
        catch (Exception e){}



        if (MainActivity.userType.equals("Client"))
        {
            holder.hire_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String hireId = databaseReference.push().getKey();

                    HirePoJo hirePoJo = new HirePoJo(hireId,currentUserId, MainActivity.userType,userPoJo.getId(),currentUserName);
                    databaseReference.child(hireId).setValue(hirePoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                holder.hire_btn.setText("Request send");
                                Toast.makeText(context, "Request Send", Toast.LENGTH_SHORT).show();
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
            holder.hire_btn.setVisibility(Button.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return userPoJos.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        TextView name_tv, price_tv;
        Button hire_btn;
        ImageView profile_iv;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);

            name_tv = itemView.findViewById(R.id.name_tv);
            price_tv = itemView.findViewById(R.id.price_tv);
            hire_btn = itemView.findViewById(R.id.hire_btn);
            profile_iv = itemView.findViewById(R.id.profile_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onClickPlayerListener.onClickPlayer(userPoJos.get(getAdapterPosition()));
                }
            });


        }
    }

    public void updatePlayer(ArrayList<UserPoJo>userPoJos){

        this.userPoJos = userPoJos;
        notifyDataSetChanged();

    }

    public interface OnClickPlayerListener{

        void onClickPlayer(UserPoJo userPoJo);
    }
}
