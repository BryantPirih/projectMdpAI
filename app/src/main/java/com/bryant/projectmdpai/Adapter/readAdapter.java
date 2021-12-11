package com.bryant.projectmdpai.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.databinding.ItemArticleBinding;
import com.bryant.projectmdpai.databinding.ItemCardArticleBinding;

import java.util.ArrayList;

public class readAdapter extends RecyclerView.Adapter<readAdapter.holder> {

    ArrayList<Article> articles = new ArrayList<Article>();

    public readAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArticleBinding binding = ItemArticleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
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
        }
    }
}
