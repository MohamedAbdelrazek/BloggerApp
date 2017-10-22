package com.mohamedabdelrazek.bloggerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

public class BlogActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 11;
    private ImageButton mImageButton;
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

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

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
            CropImage.activity()

                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();

                mImageButton.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blog_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ation_save:
                startPosting();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
