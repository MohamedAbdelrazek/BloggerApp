package com.mohamedabdelrazek.bloggerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class BlogActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 11;
    private ImageButton mImageButton;
    private Button mSubmitButton;
    private Uri mImageUri;
    private StorageReference mStorageReference;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_blog);
        mImageButton = findViewById(R.id.select_image);
        mSubmitButton = findViewById(R.id.submit_button);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();


            }
        });
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Blog");
    }

    private void startPosting() {

        mProgressDialog.setMessage("Posting ....");

        final String postTitle = ((EditText) findViewById(R.id.post_title)).getText().toString();
        final String postDesc = ((EditText) findViewById(R.id.post_desc)).getText().toString();
        if (!TextUtils.isEmpty(postTitle) && !TextUtils.isEmpty(postDesc) && mImageUri != null) {
            mProgressDialog.show();
            StorageReference filePath = mStorageReference.child("Blog_Images").child(Utils.getRandomName());

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgressDialog.dismiss();
                    Uri imgUri = taskSnapshot.getDownloadUrl();
                    mDatabaseReference.push().setValue(new BlogModel(postTitle, postDesc, imgUri.toString()));

                }
            });


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            mImageUri = data.getData();
            mImageButton.setImageURI(mImageUri);

        }
    }
}
