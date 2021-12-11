package com.bryant.projectmdpai.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.ItemCardArticleBinding;
import com.bryant.projectmdpai.databinding.ItemDoctorBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.holder> {

    ArrayList<User> listUser;
    Context context;

    public DoctorListAdapter(ArrayList<User> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDoctorBinding binding = ItemDoctorBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        this.context=parent.getContext();
        return new holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        User users = listUser.get(position);
        holder.bind(users);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(), "HALO", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        private ItemDoctorBinding binding;

        public holder(@NonNull ItemDoctorBinding itemDoctorBinding) {
            super(itemDoctorBinding.getRoot());
            this.binding = itemDoctorBinding;
        }

        void bind(User user){
            if(user.getRole().equals("Dokter")){
                binding.txtCardAskAuthor.setText("dr. " + user.getFull_name());
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
                                .into(binding.imgProfileAskDoctor);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        }
    }
}
