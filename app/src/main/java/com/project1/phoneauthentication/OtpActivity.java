package com.project1.phoneauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpActivity extends AppCompatActivity {
    private AppCompatButton verifyButton;
    private AppCompatEditText editText;
    private String OTP;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        verifyButton = findViewById(R.id.verifyOtpButton);
        editText = findViewById(R.id.OtpET);
        firebaseAuth = FirebaseAuth.getInstance();
        OTP = getIntent().getStringExtra("auth");
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verification_editText = editText.getText().toString();
                if (!verification_editText.isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, verification_editText);
                    signIn(credential);
                } else {
                    Toast.makeText(OtpActivity.this, "Please Enter otp", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void signIn(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendToHome();
                } else {
                    Toast.makeText(OtpActivity.this, "Verfication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            sendToHome();
        }
    }
    private void sendToHome(){
        Intent home =new Intent(OtpActivity.this,LogOutActivity.class);
        startActivity(home);
        finish();
    }
}
