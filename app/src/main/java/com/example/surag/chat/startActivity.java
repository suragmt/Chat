package com.example.surag.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startActivity extends AppCompatActivity {
    private Button loginbtn;
    private Button startregbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loginbtn =(Button) findViewById(R.id.startloginbutton);

        startregbtn =(Button) findViewById(R.id.startregbutton);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                Intent startlogin=new Intent(startActivity.this,loginActivity.class);
                startActivity(startlogin);

            }
        });
        startregbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent regintent=new Intent(startActivity.this,registerActivity.class);
                startActivity(regintent);
            }
        });



    }
}
