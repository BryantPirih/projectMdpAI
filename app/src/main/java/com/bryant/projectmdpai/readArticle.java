package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bryant.projectmdpai.Adapter.articleAdapter;
import com.bryant.projectmdpai.Adapter.commentAdapter;
import com.bryant.projectmdpai.Adapter.readAdapter;
import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.Class.comment;
import com.bryant.projectmdpai.Class.like;
import com.bryant.projectmdpai.databinding.ActivityMainBinding;
import com.bryant.projectmdpai.databinding.ActivityReadArticleBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class readArticle extends AppCompatActivity {

    ArrayList<Article> articles = new ArrayList<>();
    ArrayList<like> likes = new ArrayList<>();
    ArrayList<comment> comments = new ArrayList<>();
    readAdapter ra;
    commentAdapter ca;

    private String uid;
    private String keyArticle;
    private String tempUsername;
    boolean sudah=false;
    int liked;
    int commented;

    private ActivityReadArticleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent.hasExtra("pass")){
            articles.add(intent.getParcelableExtra("pass"));
            keyArticle = articles.get(0).getId();
        }
        if (intent.hasExtra("uid")){
            uid = intent.getStringExtra("uid");
        }

        getData(new FirebaseCallback() {
            @Override
            public void onCallbackUsername(String username) {
                tempUsername =username;
            }

            @Override
            public void onCallbackLike(ArrayList<like> l) {
                likes = l;
            }

            @Override
            public void onCallbackComment(ArrayList<comment> c) {
                comments = c;
            }
        });

        binding.rvArticleRead.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ra = new readAdapter(articles);
        binding.rvArticleRead.setAdapter(ra);

        binding.rvCommentRead.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ca = new commentAdapter(comments);
        binding.rvCommentRead.setAdapter(ca);

        ca.notifyDataSetChanged();

        checkLike();
    }
    private void checkLike(){
        for (int i = 0; i < likes.size(); i++) {
            if (likes.get(i).getId_article() == articles.get(0).getId() && likes.get(i).getId_user() == uid){
                sudah=true;
                break;
            }
            else sudah=false;
        }
    }
    public void btnLike_Clicked(View view) {
        if (!sudah){
            likeArticle();
        }
        else{
            unLikeArticle();
        }
        getData(new FirebaseCallback() {
            @Override
            public void onCallbackUsername(String username) {
                tempUsername = username;
            }

            @Override
            public void onCallbackLike(ArrayList<like> l) {
                likes = l;
            }

            @Override
            public void onCallbackComment(ArrayList<comment> c) {
                comments = c;
            }
        });
        checkLike();
    }
    public void btnComment_Clicked(View view) {
        if(TextUtils.isEmpty(binding.edtComment.getText().toString())){
            binding.edtComment.setError("silahkan meinggalkan comment terlebih dahulu");
            binding.edtComment.requestFocus();
            return;
        }
        commentArticle();
        binding.edtComment.setText("");
        ca.notifyDataSetChanged();

    }
    private void commentArticle(){
        DatabaseReference reference = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference("comment");

        DatabaseReference pushedRef = reference.push();
        String key = pushedRef.getKey();

        comment c = new comment(key,keyArticle,tempUsername,binding.edtComment.getText().toString());
        reference.child(key).setValue(c).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "comment article berhasil", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "comment article gagal", Toast.LENGTH_SHORT).show();

            }
        });

        DatabaseReference dbRef = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference().child("comment");

        //get user password
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.hasChild(keyArticle)){
                        DataSnapshot userSnapshot = snapshot.child(keyArticle);
                        commented = Integer.parseInt(userSnapshot.child("jumlahComment").getValue().toString());
                    }
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //makeToast("Failed to get data");
            }
        });
        commented+=1;
        FirebaseDatabase root = FirebaseDatabase.getInstance(getResources().getString(R.string.url_db));
        root.getReference("articles/"+keyArticle+"/jumlahComment").setValue(commented);


    }
    private void likeArticle(){

        DatabaseReference reference = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference("like");


        DatabaseReference pushedRef = reference.child(keyArticle).push();
        like l = new like(keyArticle,uid);
        reference.child(keyArticle).setValue(l).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "like article berhasil", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "like article gagal", Toast.LENGTH_SHORT).show();

            }
        });


        DatabaseReference dbRef = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference().child("articles");

        //get user password
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.hasChild(keyArticle)){
                        DataSnapshot userSnapshot = snapshot.child(keyArticle);
                        liked = Integer.parseInt(userSnapshot.child("jumlahLike").getValue().toString());
                    }
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //makeToast("Failed to get data");
            }
        });
        liked+=1;
        FirebaseDatabase root = FirebaseDatabase.getInstance(getResources().getString(R.string.url_db));
        root.getReference("articles/"+keyArticle+"/jumlahLike").setValue(liked);

    }
    private void unLikeArticle(){

    }
    private interface FirebaseCallback{
        void onCallbackUsername(String username);
        void onCallbackLike(ArrayList<like> l);
        void onCallbackComment(ArrayList<comment> c);
    }
    private void getData(FirebaseCallback f){
        likes = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db));
        DatabaseReference reference = database.getReference("like");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren() ) {
                    try {
                        String id_article = ds.child("id_article").getValue().toString();
                        String id_user = ds.child("id_user").getValue().toString();
                        likes.add( new like(id_article,id_user));
                        System.out.println("Size of likes : "+likes.size());
                        f.onCallbackLike(likes);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("failed to read article");
            }
        });

        reference = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference().child("users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.hasChild(uid)){
                        DataSnapshot userSnapshot = snapshot.child(uid);
                        tempUsername = userSnapshot.child("username").getValue().toString();
                        f.onCallbackUsername(tempUsername);
                    }
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });

        database = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db));
        reference = database.getReference("comment");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren() ) {
                    try {
                        if (snapshot.hasChild(articles.get(0).getId()) ){
                            String id_article = ds.child("id_article").getValue().toString();
                            String id_comment = ds.child("id_comment").getValue().toString();
                            String username_user = ds.child("username_user").getValue().toString();
                            String comment = ds.child("comment").getValue().toString();
                            comments.add( new comment(id_comment,id_article,username_user,comment));
                            System.out.println("Size of comment : "+comments.size());
                            f.onCallbackComment(comments);
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("failed to read article");
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.option_menu_exit_article,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btnBackToHome){
            Intent i = new Intent(readArticle.this,UserHome.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}