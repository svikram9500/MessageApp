package com.message.mymessageapp;

//vikram

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.message.mymessageapp.databinding.ActivityOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    ActivityOtpBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    String verificationId;
    String otp,getbackendOtp,phoneNumber;
   // Button verifyOtpbtn;
    EditText inputnumber1,inputnumber2,inputnumber3,inputnumber4,inputnumber5,inputnumber6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        final Button verifyOtpbtn = findViewById(R.id.continueBtnotp);
        inputnumber1 = findViewById(R.id.inputotp1);
        inputnumber2 = findViewById(R.id.inputotp2);
        inputnumber3 = findViewById(R.id.inputotp3);
        inputnumber4 = findViewById(R.id.inputotp4);
        inputnumber5 = findViewById(R.id.inputotp5);
        inputnumber6 = findViewById(R.id.inputotp6);



//        dialog = new ProgressDialog(this);
//        dialog.setMessage("Sending Otp...");
//        dialog.setCancelable(false);
//        dialog.show();

        auth = FirebaseAuth.getInstance();

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        getbackendOtp = getIntent().getStringExtra("backendOtp");

        final ProgressBar progressBarverifyotp = findViewById(R.id.progressbar_verify_otp);

        binding.phonelable.setText("Verify  " + phoneNumber);


        verifyOtpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputnumber1.getText().toString().trim().isEmpty() && !inputnumber2.getText().toString().trim().isEmpty() &&
                !inputnumber3.getText().toString().trim().isEmpty() && !inputnumber4.getText().toString().trim().isEmpty() &&
                        !inputnumber5.getText().toString().trim().isEmpty() && !inputnumber6.getText().toString().trim().isEmpty()){
                    String entercodeotp = inputnumber1.getText().toString() +
                            inputnumber2.getText().toString() +
                            inputnumber3.getText().toString() +
                            inputnumber4.getText().toString() +
                            inputnumber5.getText().toString() +
                            inputnumber6.getText().toString();

                    if (getbackendOtp!=null){

                        progressBarverifyotp.setVisibility(View.VISIBLE);
                        verifyOtpbtn.setVisibility(View.INVISIBLE);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getbackendOtp,entercodeotp
                        );
                        auth.signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(getApplicationContext(), com.message.mymessageapp.SetupProfileActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(OtpActivity.this, "enter correct otp", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else {
                        Toast.makeText(OtpActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(OtpActivity.this, "otp verify", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(OtpActivity.this, "invalid otp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        numberotpMove();

        binding.textResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("phoneNumber"),
                        60,
                        TimeUnit.SECONDS,
                        OtpActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newbackendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getbackendOtp = newbackendOtp;
                                Toast.makeText(OtpActivity.this, "otp send sucsesfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });

//        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
//                .setPhoneNumber(phoneNumber)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(OtpActivity.this)
//                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    @Override
//                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                    }
//
//                    @Override
//                    public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                    }
//
//                    @Override
//                    public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                        super.onCodeSent(verifyId, forceResendingToken);
//
//                        verificationId = verifyId;
//                    }
//                }).build();
//
//        PhoneAuthProvider.verifyPhoneNumber(options);



//        binding.continueBtnotp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(OtpActivity.this,SetupProfileActivity.class);
//                //dialog.dismiss();
//                startActivity(intent);
                //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,otp);

//                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()) {
//                            Toast.makeText(OtpActivity.this, "Logged In"+verificationId, Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            Toast.makeText(OtpActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            }

    private void numberotpMove() {
        inputnumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!s.toString().trim().isEmpty()){
                    inputnumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputnumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!s.toString().trim().isEmpty()){
                    inputnumber3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputnumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!s.toString().trim().isEmpty()){
                    inputnumber4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputnumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!s.toString().trim().isEmpty()){
                    inputnumber5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputnumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!s.toString().trim().isEmpty()){
                    inputnumber6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

//        });
//    }
}