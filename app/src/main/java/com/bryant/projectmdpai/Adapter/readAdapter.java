package com.bryant.projectmdpai.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.ItemArticleBinding;
import com.bryant.projectmdpai.databinding.ItemCardArticleBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class readAdapter extends RecyclerView.Adapter<readAdapter.holder> {

    ArrayList<Article> articles = new ArrayList<Article>();
    Context context;

    public readAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArticleBinding binding = ItemArticleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        this.context=parent.getContext();
        return new holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        Article a = articles.get(position);
        holder.bind(a);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        private final ItemArticleBinding binding;

        public holder(@NonNull ItemArticleBinding itemArticleBinding) {
            super(itemArticleBinding.getRoot());
            this.binding = itemArticleBinding;
        }

        void bind(Article a){
            //binding.imgArticleA.setImageBitmap();
            binding.edtContentA.setText(a.getContent());
            StorageReference storageRef = FirebaseStorage
                    .getInstance("gs://mdp-project-9db6f.appspot.com/")
                    .getReference().child("images");
            storageRef.child("article_pictures").child(articles.get(0).getId())
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageURL = uri.toString();
                    Glide.with(context)
                            .load(imageURL)
                            .apply(new RequestOptions().override(500, 500))
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(binding.imgArticleA);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.getMessage());
                }
            });

        }
    }
}
