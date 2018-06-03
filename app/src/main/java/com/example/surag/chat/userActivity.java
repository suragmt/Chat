package com.example.surag.chat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class userActivity extends AppCompatActivity {
    private Toolbar usertoolbar;
    private RecyclerView userlist;
    private DatabaseReference userdatabase;
    private FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        usertoolbar=(Toolbar) findViewById(R.id.usersappbar);
        setSupportActionBar(usertoolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userlist=(RecyclerView) findViewById(R.id.userlist);
        userlist.setHasFixedSize(true);
        userlist.setLayoutManager(new LinearLayoutManager(this));
        userdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
            DatabaseReference personref= FirebaseDatabase.getInstance().getReference().child("Users");
        Query personquery=personref.orderByKey();
        FirebaseRecyclerOptions useroptions=new FirebaseRecyclerOptions.Builder<Users>().setQuery(personquery,Users.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UsersViewHolder>(useroptions) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
                holder.setName(model.getName());
                holder.setStatus(model.getStatus());
                holder.setImage(model.getThumb(),getApplicationContext());
                final String userid=getRef(position).getKey();
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileintent=new Intent(userActivity.this,profileActivity.class);
                        profileintent.putExtra("userid",userid);
                        startActivity(profileintent);
                    }
                });

            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singleuserlayout,parent,false);
            return  new userActivity.UsersViewHolder(view);

                        }


        };
        userlist.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

       firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.startListening();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mview;


        public UsersViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }

        public void setName(String name){
            TextView  usernameview=(TextView) mview.findViewById(R.id.username);
            usernameview.setText(name);
        }
        public void setStatus(String status)
        {
            TextView statusview=(TextView) mview.findViewById(R.id.userstatus);
            statusview.setText(status);
        }
        public void setImage(String thumb,Context ctx)
        {
            CircleImageView circle=(CircleImageView) mview.findViewById(R.id.userimage);
            Picasso.get().load(thumb).placeholder(R.drawable.profile).into(circle);
        }
    }
}
