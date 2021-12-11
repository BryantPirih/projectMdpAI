package com.bryant.projectmdpai.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.comment;

import java.util.ArrayList;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.holder> {
    ArrayList<comment> comments = new ArrayList<comment>();

    public commentAdapter(ArrayList<comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class holder extends RecyclerView.ViewHolder {
        public holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
