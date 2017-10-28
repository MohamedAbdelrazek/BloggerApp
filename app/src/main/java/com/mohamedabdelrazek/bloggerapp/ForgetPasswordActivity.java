package com.mohamedabdelrazek.bloggerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends AppCompatActivity {
    @BindView(R.id.submit_btn)
    Button mSubmitBtn;

    @BindView(R.id.input_email)
    EditText mEmailAddress;

    @BindView(R.id.rest_password_indicate_txtview)
    TextView mMessage;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailResetPassword();
            }


        });
    }

    private void sendEmailResetPassword() {
        if (validate()) {
            final ProgressDialog progressBar = new ProgressDialog(this);
            progressBar.setMessage("Requesting password reset");
            progressBar.setCancelable(false);
            progressBar.show();
            mAuth.sendPasswordResetEmail(mEmailAddress.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgetPasswordActivity.this,
                                "check your email address for instructions on how to change your password!",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        progressBar.dismiss();
                        finish();

                    } else {

                        Toast.makeText(ForgetPasswordActivity.this,
                                "Failed to Authenticate you!",
                                Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }
            });


        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = mEmailAddress.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailAddress.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailAddress.setError(null);
        }
        return valid;
    }
}