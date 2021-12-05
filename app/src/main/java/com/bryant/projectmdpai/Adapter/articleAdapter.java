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
    ArrayList<likeComments> LikeComments = new ArrayList<>();



    public articleAdapter(ArrayList<Article> articles, ArrayList<likeComments> likeComments) {
        this.articles = articles;
        LikeComments = likeComments;
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
        likeComments l = LikeComments.get(position);
        holder.bind(a,l);
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

        void bind(Article article,likeComments like){
            binding.txtCardArticleTitle.setText(article.getTitle());
            binding.txtCardArticleDatetime.setText(article.getTimeString());
            binding.txtCardArticleAuthor.setText(article.getAuthor());
            int temp=0,temp1=0;
            if (LikeComments.size()!=0 || !LikeComments.isEmpty()){
                for (int i = 0; i < LikeComments.size(); i++) {
                    if (LikeComments.get(i).getLike()==1){
                        temp+=1;
                    }
                    if(LikeComments.get(i).getComment()!=null || LikeComments.get(i).getComment()!=""){
                        temp1+=1;
                    }
                }
            }
            binding.txtJumlahLike.setText(temp+"");
            binding.txtJumlahComment.setText(temp1);
        }
    }

//    public interface OnItemClickCallback{
//        void onItemClicked(film film);
//    }
}

