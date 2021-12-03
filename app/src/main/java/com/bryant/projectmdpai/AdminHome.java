package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bryant.projectmdpai.AdminFragment.AdminRequestFragment;
import com.bryant.projectmdpai.AdminFragment.AdminUsersFragment;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.UserFragment.UserHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity {
    BottomNavigationView botnav;
    FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        botnav = findViewById(R.id.bottNavAdmin);
        container = findViewById(R.id.frameLayoutAdmin);


        botnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String menu;
                Fragment fragment;
                if(item.getItemId()==R.id.item_admin_list){
                    menu="Users";
                    fragment = AdminUsersFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutAdmin, fragment).commit();
                    return true;
                }
                else{
                    menu="Requests";
                    fragment = AdminRequestFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutAdmin, fragment).commit();
                    return true;
                }
            }
        });

        if(savedInstanceState == null){
            botnav.setSelectedItemId(R.id.item_admin_list);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_nav, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.item_admin_logout){
            Intent toLogin = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(toLogin);

        }
        return super.onOptionsItemSelected(item);
    }
}