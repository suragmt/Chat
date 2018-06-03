package com.example.surag.chat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class profileActivity extends AppCompatActivity {
    private DatabaseReference profileref;
    private ImageView profileimage;
    private TextView displayname,status,friends;
    private Button friendrequest;
    private ProgressDialog profileprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String userid=getIntent().getStringExtra("userid");
        profileref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

        profileprogress=new ProgressDialog(profileActivity.this);

        profileprogress.setTitle("User Profile");
        profileprogress.setMessage("wait while profile getting loaded");
        profileprogress.setCanceledOnTouchOutside(false);
        profileprogress.show();
        displayname=(TextView) findViewById(R.id.profilename);
        status=(TextView) findViewById(R.id.profilestatus);
        friends=(TextView) findViewById(R.id.profilefriends);
        friendrequest=(Button) findViewById(R.id.profilerequest);
        profileimage=(ImageView) findViewById(R.id.profileimage);


        profileref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displaynames=dataSnapshot.child("name").getValue().toString();
                String statuss=dataSnapshot.child("status").getValue().toString();
                String images=dataSnapshot.child("image").getValue().toString();
                displayname.setText(displaynames);
                status.setText(statuss);
                Picasso.get().load(images).placeholder(R.drawable.profile2).into(profileimage);
                profileprogress.dismiss();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
