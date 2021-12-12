package com.bryant.projectmdpai.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.comment;
import com.bryant.projectmdpai.databinding.ItemCommentBinding;

import java.util.ArrayList;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.holder> {

    ArrayList<comment> comments = new ArrayList<comment>();
    public commentAdapter(ArrayList<comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding binding = ItemCommentBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        comment c = comments.get(position);
        holder.bind(c);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        private final ItemCommentBinding binding;

        public holder(@NonNull ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            this.binding = itemCommentBinding;
        }

        void bind(comment comment){
            binding.edtUsernameC.setText(comment.getUsername_user());
            binding.edtCommentC.setText(comment.getComment());
        }

    }
}
