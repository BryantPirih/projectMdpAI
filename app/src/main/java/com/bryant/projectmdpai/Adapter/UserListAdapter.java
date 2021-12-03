package com.bryant.projectmdpai.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.R;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    ArrayList<User> listUser = new ArrayList<>();

    public UserListAdapter(ArrayList<User> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_users_admin, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.txtFullName.setText(user.getFull_name());
        holder.txtEmail.setText(user.getEmail());
        holder.txtAddress.setText(user.getAddress());
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView txtFullName, txtEmail, txtAddress;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFullName = itemView.findViewById(R.id.txt_admin_users);
            txtEmail = itemView.findViewById(R.id.txt_admin_email);
            txtAddress = itemView.findViewById(R.id.txt_admin_address);
        }
    }
}
