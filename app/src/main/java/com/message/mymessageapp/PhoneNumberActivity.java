package com.message.mymessageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.message.mymessageapp.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumberActivity extends AppCompatActivity {

    //view binding
    ActivityPhoneNumberBinding binding;

    EditText phoneBox;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        phoneBox = findViewById(R.id.phoneBox);
        binding.phoneBox.requestFocus();
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null) {
            Intent intent = new Intent(PhoneNumberActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        final ProgressBar progressBar = findViewById(R.id.progressbar_sending_otp);

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(PhoneNumberActivity.this,OtpActivity.class);
//                intent.putExtra("phoneNumber", binding.phoneBox.getText().toString());
//                startActivity(intent);
                if (!phoneBox.getText().toString().trim().isEmpty()){
                    if ((phoneBox.getText().toString().trim()).length()==10){

                        progressBar.setVisibility(View.VISIBLE);
                        binding.continueBtn.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + phoneBox.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                PhoneNumberActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.GONE);
                                        binding.continueBtn.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        binding.continueBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(PhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(View.GONE);
                                        binding.continueBtn.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                                        intent.putExtra("phoneNumber",phoneBox.getText().toString());
                                        intent.putExtra("backendOtp",backendOtp);
                                        startActivity(intent);
                                    }
                                }
                        );
                    }else {
                        Toast.makeText(PhoneNumberActivity.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PhoneNumberActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}