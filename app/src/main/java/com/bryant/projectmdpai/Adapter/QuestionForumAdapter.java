package com.bryant.projectmdpai.Adapter;

import android.content.Context;
import android.net.Uri;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class QuestionForumAdapter  extends RecyclerView.Adapter<QuestionForumAdapter.QuestionForumHolder>{
    ArrayList<Question> listQuestion;
    ArrayList<Question> listQuestionSorted = new ArrayList<>();
    ArrayList<User> listUser;
    Context context;
    private OnItemClickCallback onItemClickCallback;

    public QuestionForumAdapter(ArrayList<Question> listQuestion, ArrayList<User> listUser) {
        this.listQuestion = listQuestion;
        this.listQuestionSorted.addAll(listQuestion);
        this.listUser = listUser;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
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
        Question q = listQuestionSorted.get(position);
        holder.txtTitle.setText(q.getTitle());
        holder.txtDate.setText("-"+q.getTime());

        for(User u : listUser){
            if(u.getId().equals(q.getAuthor())){
                try {
                    holder.txtUserFullName.setText("Questioner: "+u.getFull_name());
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
                                    .into(holder.imgProfile);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                break;
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(listQuestionSorted.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listQuestionSorted.size();
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
    public interface OnItemClickCallback{
        void onItemClicked(Question question);
    }

    public void showAll() {
        listQuestionSorted.clear();
        listQuestionSorted.addAll(listQuestion);
        this.notifyDataSetChanged();
    }

    public void search(String keyword){
        listQuestionSorted.clear();
        for (Question question: listQuestion) {
            if (question.getTitle().toLowerCase().contains(keyword.toLowerCase())){
                listQuestionSorted.add(question);
            }
        }
        this.notifyDataSetChanged();
    }
}
