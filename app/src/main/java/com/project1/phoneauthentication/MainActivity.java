package com.project1.phoneauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton aSendOtp;
    private EditText aCountryCode, aPhoneNumber;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aPhoneNumber = findViewById(R.id.phoneNumber);
        aCountryCode = findViewById(R.id.countryCode);
        aSendOtp = findViewById(R.id.sendCodeButton);
        firebaseAuth = FirebaseAuth.getInstance();

        aSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country_code = aCountryCode.getText().toString();
                String phone = aPhoneNumber.getText().toString();
                String phoneNumber = "+" + country_code + "" + phone;
                if (!country_code.isEmpty() || !phone.isEmpty()) {
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
               } else{
                    Toast.makeText(MainActivity.this, " Please Enter Valid stuff", Toast.LENGTH_SHORT).show();
//                if(country_code.isEmpty()) {
//                        Toast.makeText(MainActivity.this, "Please Enter Country code", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(MainActivity.this, "Please Enter Mobile number", Toast.LENGTH_SHORT).show();
                    }

            }
        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                Toast.makeText(MainActivity.this, "Otp has been sent", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent otpIntent = new Intent(MainActivity.this, OtpActivity.class);
                        otpIntent.putExtra("auth", s);
                        startActivity(otpIntent);

                    }
                }, 10000);


            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null ){
            sentToHome();
        }

    }

    private void sentToHome() {
        Intent home=new Intent(MainActivity.this,LogOutActivity.class);
        startActivity(home);
        finish();
    }




    private void signIn(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sentToHome();
                }else{
                    Toast.makeText(MainActivity.this, task .getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
