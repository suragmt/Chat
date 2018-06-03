package com.example.surag.chat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class statusActivity extends AppCompatActivity {

    private Toolbar statustoolbar;
    private TextInputLayout statusinput;
    private Button updatebutton;
    private DatabaseReference statusrefer;
    private FirebaseUser currentuser;
    private ProgressDialog statusprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        statustoolbar=(Toolbar) findViewById(R.id.statusappbar);
        setSupportActionBar(statustoolbar);
        getSupportActionBar().setTitle("User Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        statusinput=(TextInputLayout) findViewById(R.id.statustext);
        statusprogress=new ProgressDialog(this);
        statusrefer= FirebaseDatabase.getInstance().getReference().child("Users");
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
        updatebutton=(Button) findViewById(R.id.statusbutton);
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                statusprogress.setTitle("Status");
                statusprogress.setMessage("updating your status");
                statusprogress.setCanceledOnTouchOutside(false);
                statusprogress.show();
                String userc=currentuser.getUid();
                String status=statusinput.getEditText().getText().toString();
                 String statusrec=getIntent().getStringExtra("statuspass");
                statusinput.getEditText().setText(statusrec);
                statusrefer.child(userc).child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            statusprogress.dismiss();
                        else Toast.makeText(getApplicationContext(),"error while updating status",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}
