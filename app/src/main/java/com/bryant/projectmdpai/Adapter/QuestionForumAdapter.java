package com.bryant.projectmdpai.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bryant.projectmdpai.Class.Question;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionForumAdapter  extends RecyclerView.Adapter<QuestionForumAdapter.QuestionForumHolder>{
    ArrayList<Question> listQuestion;
    ArrayList<User> listUser;
    Context context;

    public QuestionForumAdapter(ArrayList<Question> listQuestion, ArrayList<User> listUser) {
        this.listQuestion = listQuestion;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public QuestionForumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_question, parent, false);
        QuestionForumHolder questionForumHolder = new QuestionForumHolder(v);
        this.context=parent.getContext();
        return questionForumHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionForumHolder holder, int position) {
        Question q = listQuestion.get(position);
        holder.txtTitle.setText(q.getTitle());
        holder.txtDate.setText("-"+q.getTime());
        for(User u : listUser){
            if(u.getId().equals(q.getAuthor())){
                holder.txtUserFullName.setText("Questioner: "+u.getFull_name());
            }
        }


    }

    @Override
    public int getItemCount() {
        return listQuestion.size();
    }

    public class QuestionForumHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView txtTitle, txtUserFullName, txtDate;
        public QuestionForumHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfileQuestion);
            txtTitle = itemView.findViewById(R.id.txtCardQuestionTitle);
            txtDate = itemView.findViewById(R.id.txtCardQuestionDateTime);
            txtUserFullName = itemView.findViewById(R.id.txtCardQuestionAuthor);
        }
    }
}
