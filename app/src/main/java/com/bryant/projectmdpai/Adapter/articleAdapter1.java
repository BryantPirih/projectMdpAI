package com.bryant.projectmdpai.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.databinding.ItemCardArticleBinding;

import java.util.ArrayList;

public class articleAdapter1  extends RecyclerView.Adapter<articleAdapter1.holder> {

    ArrayList<Article> articles = new ArrayList<Article>();

    private OnItemClickCallback onItemClickCallback;

    public articleAdapter1(ArrayList<Article> articles) {
        this.articles = articles;
    }
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardArticleBinding binding = ItemCardArticleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        //this.context=parent.getContext();
        return new holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
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
            binding.txtJumlahLike.setText(article.getJumlahLike()+"");
            binding.txtJumlahComment.setText(article.getJumlahComment()+"");
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(Article article);
    }
}
