package com.example.rentalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Signup extends AppCompatActivity {

    TextInputEditText entersignUpPhone;
    TextView signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        entersignUpPhone = findViewById(R.id.signUpPhone);
        signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(entersignUpPhone.getText().toString())) {
                    Toast.makeText(Signup.this, "Please Enter the phone number", Toast.LENGTH_LONG).show();
                } else if (entersignUpPhone.getText().toString().trim().length() == 10) {

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+977" + entersignUpPhone.getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            Signup.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    Toast.makeText(Signup.this, "verification completed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("signup error",e.getMessage());
                                }

                                @Override
                                public void onCodeSent(@NonNull String otpbackend, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(otpbackend, forceResendingToken);
                                    String Phonenumber = entersignUpPhone.getText().toString();
                                    Intent intent = new Intent(Signup.this, otp.class);
                                    intent.putExtra("Phonenumber", Phonenumber);
                                    intent.putExtra("otpbackend", otpbackend);
                                    startActivity(intent);
                                }
                            });
                } else {
                    Toast.makeText(Signup.this, "Please enter the valid Phone Number", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
//    it checks and if the user is already logged in it directly passes the user to the main actitity
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(Signup.this,HomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}