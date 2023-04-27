package com.example.rentalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {

    FirebaseAuth mAuth;

    String backendotp;

    TextInputEditText enterOtp;
    TextView phonenumber;
    Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        btnVerify = findViewById(R.id.otpBtn);

        enterOtp = findViewById(R.id.otp);
        phonenumber = findViewById(R.id.phone);

        mAuth = FirebaseAuth.getInstance();
        String phone = getIntent().getStringExtra("Phonenumber");
        backendotp = getIntent().getStringExtra("otpbackend");
        phonenumber.setText(phone);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterOtp.getText().toString().trim().isEmpty()) {
                    Toast.makeText(otp.this, "Please enter the otp", Toast.LENGTH_LONG).show();
                } else if (!enterOtp.getText().toString().trim().isEmpty()) {
                    String enteredOtp = enterOtp.getText().toString().trim();

                    if (backendotp != null) {
                        PhoneAuthCredential phoneCredential = PhoneAuthProvider.getCredential(
                                backendotp, enteredOtp
                        );
                        //if the otp is received we will pass it to the set profile page
                        //Edit this section Later onn
                        FirebaseAuth.getInstance().signInWithCredential(phoneCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(otp.this, UserProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(otp.this, "Please check your internet connections", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(otp.this, "Please enter the internet connections", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(otp.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.resendOtp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+977" + phone,
                        60,
                        TimeUnit.SECONDS,
                        otp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(otp.this, "verification completed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(otp.this, "Error while verifying your phone number", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String otpbackend, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(otpbackend, forceResendingToken);
                                backendotp = otpbackend;
                                Toast.makeText(otp.this, "verification code send successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}