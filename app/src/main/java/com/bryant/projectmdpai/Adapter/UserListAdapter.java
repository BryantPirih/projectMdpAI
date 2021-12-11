package com.bryant.projectmdpai.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    ArrayList<User> listUser = new ArrayList<>();
    Context context;

    public UserListAdapter(ArrayList<User> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_users_admin, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(v);
        this.context=parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.txtFullName.setText(user.getFull_name());
        holder.txtEmail.setText(user.getEmail());
        holder.txtAddress.setText(user.getAddress());
        if(user.getRole().equals("Dokter")){
            if(user.getStatus()==0){
                holder.txtRole.setTextColor(Color.RED);
                holder.txtRole.setText(user.getRole()+" - Unverified");
            }
            else {
                holder.txtRole.setTextColor(Color.BLUE);
                holder.txtRole.setText(user.getRole()+" - Verified");
            }
        }
        else{
            holder.txtRole.setText(user.getRole());
            holder.txtRole.setTextColor(Color.BLACK);
        }
        StorageReference storageRef = FirebaseStorage
                .getInstance("gs://mdp-project-9db6f.appspot.com/")
                .getReference().child("images");
        storageRef.child("profile_pictures").child(user.getId())
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(context)
                        .load(imageURL)
                        .placeholder(R.drawable.ic_user_icon)
                        .into(holder.imgView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView txtFullName, txtEmail, txtAddress, txtRole;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFullName = itemView.findViewById(R.id.txt_admin_users);
            txtEmail = itemView.findViewById(R.id.txt_admin_email);
            txtAddress = itemView.findViewById(R.id.txt_admin_address);
            txtRole = itemView.findViewById(R.id.txt_admin_role);
            imgView = itemView.findViewById(R.id.imageView);
        }
    }


}
