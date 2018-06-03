package com.example.surag.chat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    private ViewPager mainviewpager;
    private SectionPagerAdapter mainadapter;
    private TabLayout maintablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mtoolbar = (Toolbar) findViewById(R.id.mainpagetoolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("ChatApp");
        mainviewpager = (ViewPager) findViewById(R.id.mainviewpager);
        mainadapter = new SectionPagerAdapter(getSupportFragmentManager());
        mainviewpager.setAdapter(mainadapter);
        maintablayout = (TabLayout) findViewById(R.id.maintablayout);
        maintablayout.setupWithViewPager(mainviewpager);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent startIntent = new Intent(MainActivity.this, startActivity.class);
            startActivity(startIntent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mainmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        /*if(item.getItemId()==R.id.mainmenulogout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent startIntent = new Intent(MainActivity.this, startActivity.class);
            startActivity(startIntent);
            finish();
            return true;}
        if (item.getItemId()==R.id.mainmenuaccount){
                Intent accountIntent = new Intent(MainActivity.this, accountActivity.class);
                startActivity(accountIntent);
                return true;}

        if (item.getItemId()==  R.id.mainusers)
        {Intent userIntent = new Intent(MainActivity.this, userActivity.class);
                    startActivity(userIntent);
                    return true;}

        else
            return false;*/
           switch (item.getItemId()) {
               case R.id.mainmenulogout: {
                   FirebaseAuth.getInstance().signOut();
                   Intent startIntent = new Intent(MainActivity.this, startActivity.class);
                   startActivity(startIntent);
                   finish();
                   return true;
               }
               case R.id.mainmenuaccount: {
                   Intent accountIntent = new Intent(MainActivity.this, accountActivity.class);
                   startActivity(accountIntent);
                   return true;
               }
               case R.id.mainusers: {
                   Intent userIntent = new Intent(MainActivity.this, userActivity.class);
                   startActivity(userIntent);
                   return true;
               }
           }
                return false;



    }}