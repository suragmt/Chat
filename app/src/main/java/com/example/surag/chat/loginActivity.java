package com.example.surag.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {
    private Toolbar ltoolbar;
    private Button loginbutton;
    private TextInputLayout email;
    private TextInputLayout password;
    private FirebaseAuth mAuth;
    private ProgressDialog loginprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        ltoolbar=(Toolbar) findViewById(R.id.loginpagetoolbar);
        loginbutton=(Button) findViewById(R.id.loginbutton);
        email=(TextInputLayout) findViewById(R.id.loginpageemail);
        password=(TextInputLayout) findViewById(R.id.loginpagepassword);
        loginprogress=new ProgressDialog(this);




        setSupportActionBar(ltoolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String semail=email.getEditText().getText().toString();
                String spassword=password.getEditText().getText().toString();
                if(TextUtils.isEmpty(semail)||TextUtils.isEmpty(spassword))
                {
                    Toast.makeText(loginActivity.this, "Please complete your forms",
                            Toast.LENGTH_SHORT).show();}
                else{
                    loginprogress.setTitle("Login");
                    loginprogress.setMessage("Logging in");
                    loginprogress.setCanceledOnTouchOutside(false);
                    loginprogress.show();
                    login_user(semail,spassword);

                }




            }
        });

    }
    private void login_user(String semail,String spassword)
    {

    mAuth.signInWithEmailAndPassword(semail, spassword)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                loginprogress.dismiss();

                Intent mainintent =new Intent(loginActivity.this,MainActivity.class);
                mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainintent);
                finish();
            } else {
                loginprogress.hide();
                // If sign in fails, display a message to the user.
                Toast.makeText(loginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }

            // ...
        }
    });}
}
