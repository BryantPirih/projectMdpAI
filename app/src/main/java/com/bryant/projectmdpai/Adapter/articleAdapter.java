package com.bryant.projectmdpai.Adapter;

import android.content.Context;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.Class.like;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.ItemCardArticleBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class articleAdapter extends RecyclerView.Adapter<articleAdapter.holder> {

    ArrayList<Article> articles = new ArrayList<>();
    ArrayList<User> listUser;
    Context context;

    public articleAdapter(ArrayList<Article> articles,ArrayList<User> listUser) {
        this.articles = articles;
        this.listUser = listUser;
    }
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public articleAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardArticleBinding binding = ItemCardArticleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        this.context=parent.getContext();
        return new holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull articleAdapter.holder holder, int position) {
        Article a = articles.get(position);
        holder.bind(a);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        private final ItemCardArticleBinding binding;

        public holder(@NonNull ItemCardArticleBinding itemCardArticleBinding) {
            super(itemCardArticleBinding.getRoot());
            this.binding = itemCardArticleBinding;
        }
        void bind(Article article){
            binding.txtCardArticleTitle.setText(article.getTitle());
            binding.txtCardArticleDatetime.setText(article.getTimeString());
            binding.txtCardArticleAuthor.setText(article.getAuthor());
            for(User u : listUser){
                if(u.getUsername().equals(article.getAuthor())){
                    try{
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
                                    .into(binding.imgArticleProfileAuthor);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                    break;
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                }
            }
            binding.txtJumlahLike.setText(article.getJumlahLike()+"");
            binding.txtJumlahComment.setText(article.getJumlahComment()+"");
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            Bitmap b = BitmapFactory.decodeByteArray(article.getImage(),0,article.getImage().length,o);
//            binding.imgArticleProfileAuthor.setImageBitmap(b);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(Article article);
    }
}

