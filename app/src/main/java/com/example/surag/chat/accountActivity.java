package com.example.surag.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class accountActivity extends AppCompatActivity {
    private DatabaseReference accountdb;
    private FirebaseUser currentuser;
    private TextView displayname;
    private TextView mstatus;
    private StorageReference mStorageRef;
    private static final int GALLERY_PICK=1;
    private ProgressDialog imageprogress;
    private CircleImageView accountpic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        displayname=(TextView) findViewById(R.id.accountusername);
        mstatus=(TextView) findViewById(R.id.accountstatus);
        Button statusbutton = (Button) findViewById(R.id.accountstatusbutton);
        mStorageRef = FirebaseStorage.getInstance().getReference();


        statusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statuspass=mstatus.getText().toString();

                Intent statusintent=new Intent(accountActivity.this,statusActivity.class);
                statusintent.putExtra("statuspass",statuspass);
                startActivity(statusintent);
            }
        });
        Button picbutton = (Button) findViewById(R.id.accountpicbutton);
        picbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryintent=new Intent();
                galleryintent.setType("image/*");
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryintent,"Choose image"),GALLERY_PICK);
                /*CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(accountActivity.this);*/
            }
        });
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
        String uid= Objects.requireNonNull(currentuser).getUid();
        accountdb= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        accountdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name= Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String status= Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString();
                String image= Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                Toast.makeText(accountActivity.this,image,Toast.LENGTH_LONG).show();

                displayname.setText(name);
                mstatus.setText(status);
                accountpic=(CircleImageView) findViewById(R.id.accountprofilepic);
                if(!image.equals("default")) {
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(accountpic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK) {
            Uri imageuri = data.getData();

            CropImage.activity(imageuri).setAspectRatio(1, 1).setMinCropWindowSize(500, 500).start(accountActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Toast.makeText(accountActivity.this,"resultok",Toast.LENGTH_LONG).show();

                imageprogress=new ProgressDialog(accountActivity.this);

                imageprogress.setTitle("image");
                imageprogress.setMessage("updating your image");
                imageprogress.setCanceledOnTouchOutside(false);
                imageprogress.show();
                Uri resultUri = result.getUri();
                File thumb_filepath=new File(resultUri.getPath());
                String current_id=currentuser.getUid();

                try {
                    final Bitmap thumb_bitmap= new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filepath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] thumb_byte = baos.toByteArray();



                StorageReference filepath=mStorageRef.child("profile_images").child(current_id+ ".jpg");
                final StorageReference thumbfilepath=mStorageRef.child("profile_images").child("thumbs").child(current_id+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            final String downloadurl= Objects.requireNonNull(task.getResult().getDownloadUrl()).toString();

                            UploadTask uploadTask = thumbfilepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot>thumbtask) {

                                    String thumb_downloadurl= Objects.requireNonNull(thumbtask.getResult().getDownloadUrl()).toString();
                                    if (thumbtask.isSuccessful()) {

                                        Map updatehashmap=new HashMap();
                                        updatehashmap.put("image",downloadurl);
                                        updatehashmap.put("thumb",thumb_downloadurl);
                                        accountdb.updateChildren(updatehashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    imageprogress.dismiss();
                                                    Toast.makeText(accountActivity.this, "done uploadig", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                                    } else{
                                        Toast.makeText(accountActivity.this, "error", Toast.LENGTH_LONG).show();
                                    imageprogress.dismiss();
                                }

                                }
                            });

                        }
                        else
                            Toast.makeText(accountActivity.this,"error",Toast.LENGTH_LONG).show();
                        imageprogress.dismiss();

                    }
                });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(accountActivity.this,"error resultcode",Toast.LENGTH_LONG).show();

            }

        }


    }
}
