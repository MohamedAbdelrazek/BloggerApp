package com.mohamedabdelrazek.bloggerapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mohamed on 31/10/2017.
 */

public class ProfileFragment extends Fragment {
    private static final int GALLERY_REQUEST = 11;
    private Uri mImageUri;
    private StorageReference mStorageReference;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabaseReference;
    @BindView(R.id.select_image)
    ImageButton mImageButton;
    @BindView(R.id.post_title)
    EditText mTitleTextView;
    @BindView(R.id.post_desc)
    EditText mDescTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(getContext());
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Blog");
        return view;
    }

    private void startPosting() {

        mProgressDialog.setMessage("Posting ....");
        mProgressDialog.setCancelable(false);
        final String postTitle = mTitleTextView.getText().toString();
        final String postDesc = mDescTextView.getText().toString();
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


        } else {

            Toast.makeText(getContext(), "Fill in all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {

            mImageUri = data.getData();
            CropImage.activity()

                    .setAspectRatio(1, 1)
                    .start(getActivity());
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                mImageUri = result.getUri();

                mImageButton.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.getError();
            }
        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_blog_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
