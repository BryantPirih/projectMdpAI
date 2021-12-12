package com.bryant.projectmdpai.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.Answer;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerHolder> {
    ArrayList<Answer> listAnswer = new ArrayList<>();
    ArrayList<User> listUser = new ArrayList<>();
    Context context;

    public AnswerAdapter(ArrayList<Answer> listAnswer, ArrayList<User> listUser) {
        this.listAnswer = listAnswer;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_answers, parent, false);
        AnswerHolder viewHolder = new AnswerHolder(v);
        this.context=parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerHolder holder, int position) {
        Answer a  = listAnswer.get(position);
        holder.txtAnswer.setText(a.getAnswer());
        holder.txtTime.setText(a.getTime());

        for(User u : listUser){
            if(u.getId().equals(a.getAuthor())){
                try {
                    holder.txtAuthor.setText(u.getFull_name());
                    StorageReference storageRef = FirebaseStorage
                            .getInstance("gs://mdp-project-9db6f.appspot.com/")
                            .getReference().child("images");
                    storageRef.child("profile_pictures").child(u.getId())
                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            Glide.with(context)
                                    .load(imageURL)
                                    .apply(new RequestOptions().override(40, 40))
                                    .placeholder(R.drawable.ic_baseline_person_24)
                                    .into(holder.imgProfile);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listAnswer.size();
    }


    public class AnswerHolder extends RecyclerView.ViewHolder {
        TextView txtAuthor, txtTime, txtAnswer;
        ImageView imgProfile;
        public AnswerHolder(@NonNull View itemView) {
            super(itemView);
            txtAuthor = itemView.findViewById(R.id.txt_answer_author);
            txtTime = itemView.findViewById(R.id.txt_answer_time);
            txtAnswer = itemView.findViewById(R.id.txt_answer_answer);
            imgProfile = itemView.findViewById(R.id.img_answer_profile);
        }
    }
}
