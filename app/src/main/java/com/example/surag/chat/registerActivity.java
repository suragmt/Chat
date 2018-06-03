package com.example.surag.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class registerActivity extends AppCompatActivity {
    private Button regbut;
    private TextInputLayout regname;
    private TextInputLayout regemail;
    private TextInputLayout regpassword;
    private FirebaseAuth mAuth;
    private Toolbar rtoolbar;
    private ProgressDialog registerprogress;
    private FirebaseDatabase maindatabase;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        regname=(TextInputLayout) findViewById(R.id.regdisplayname);
        regemail=(TextInputLayout) findViewById(R.id.regemailid);
        regpassword=(TextInputLayout) findViewById(R.id.regpassword);
        rtoolbar=(Toolbar) findViewById(R.id.registerpagetoolbar);
        setSupportActionBar(rtoolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerprogress=new ProgressDialog(this);
        regbut=(Button) findViewById(R.id.regcreatebutton);
        regbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String dispname=regname.getEditText().getText().toString();
                String email=regemail.getEditText().getText().toString();
                String password=regpassword.getEditText().getText().toString();
                if(TextUtils.isEmpty(dispname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(registerActivity.this, "Please complete your forms",
                            Toast.LENGTH_SHORT).show();}
                            else{
                    registerprogress.setTitle("create");
                    registerprogress.setMessage("Creating Account");
                    registerprogress.setCanceledOnTouchOutside(false);
                    registerprogress.show();
                    register_user(dispname, email, password);
                }

            }
        });
    }
    private void register_user(final String displayname, String email, String password){
    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
                String uid=currentuser.getUid();
                FirebaseDatabase maindatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = maindatabase.getReference().child("Users").child(uid);

                HashMap<String,String> usermap=new HashMap<>();
                usermap.put("name",displayname);
                usermap.put("status","Hi im here");
                usermap.put("image","default");
                usermap.put("thumb","default");
                myRef.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                registerprogress.dismiss();
                Intent mainintent =new Intent(registerActivity.this,MainActivity.class);
                mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(mainintent);
                finish();
                        }
                    }
                });



            } else {
                registerprogress.hide();
                // If sign in fails, display a message to the user.
                Toast.makeText(registerActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }
            // ...

    });
}}
