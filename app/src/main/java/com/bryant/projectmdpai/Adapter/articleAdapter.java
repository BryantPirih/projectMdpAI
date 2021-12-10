package com.bryant.projectmdpai.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.likeComments;
import com.bryant.projectmdpai.databinding.ItemCardArticleBinding;

import java.util.ArrayList;

public class articleAdapter extends RecyclerView.Adapter<articleAdapter.holder> {

    ArrayList<Article> articles = new ArrayList<>();

    public articleAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }


    @NonNull
    @Override
    public articleAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardArticleBinding binding = ItemCardArticleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull articleAdapter.holder holder, int position) {
        Article a = articles.get(position);
        holder.bind(a);
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
        }
    }

//    public interface OnItemClickCallback{
//        void onItemClicked(film film);
//    }
}

