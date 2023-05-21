package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rentalapplication.Adapter.ChatsAdapter;
import com.google.android.material.tabs.TabLayout;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ChatLayoutActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
//   TabItem chatItem, callItem;
    ViewPager viewPager;
    //    TextView chatLayoutTitle;
    ChatsAdapter chatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);
        tabLayout = findViewById(R.id.tabLayoutChat);
//        chatItem = findViewById(R.id.tabChats);
        viewPager = findViewById(R.id.chatViewPager);

        toolbar = findViewById(R.id.toolbar);

        //FOR MENU ICON AND white 3 dot
        Drawable toolbarDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_more_vert_24);
        toolbar.setOverflowIcon(toolbarDrawable);

        //set action bar
        setSupportActionBar(toolbar);

//        chatsAdapter = new ChatsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(chatsAdapter);

        chatsAdapter = new ChatsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(chatsAdapter);

        tabLayout.setupWithViewPager(viewPager);

       // tab layout ko kateko wala insert here if error occur


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;

    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);

    }
}