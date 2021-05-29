package com.example.lifeservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {
    private Button sendotp,verify;
    private TextView resend;
    private TextInputLayout phonenumber;//,otpcode;
    private PinView otpcode;
    String userPhoneNum,verrificationId;

    PhoneAuthProvider.ForceResendingToken token;
    FirebaseAuth fAuth;

    public String removeLeadingZeroes(String str) {
        String strPattern = "^0+(?!$)";
        str = str.replaceFirst(strPattern, "");
        return str;
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_verify_phone);
        //button
        verify = findViewById(R.id.btnVerify);
        sendotp = findViewById(R.id.btnSendotp);

        otpcode = findViewById(R.id.pinviewOTP);
        //textview
        resend = findViewById(R.id.txtResend);

        phonenumber = findViewById(R.id.edtPhoneNumber);
        fAuth = FirebaseAuth.getInstance();

        //otptext
        //otpcode = findViewById(R.id.edtOTP);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phonenumber.getEditText().getText().toString().trim().isEmpty())
                {
                    phonenumber.setError("Please input your phone number !");
                    return;
                }
                else{
                    userPhoneNum = "+84" + removeLeadingZeroes(phonenumber.getEditText().getText().toString().trim());
                    verifyPhoneNumber(userPhoneNum);
                    Toast.makeText(VerifyPhone.this,userPhoneNum,Toast.LENGTH_SHORT).show();
                }
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otpcode.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(VerifyPhone.this,"Enter OTP Code First",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verrificationId, otpcode.getText().toString().trim());
                    authenticateUser(credential);
                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                authenticateUser(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                Toast.makeText(VerifyPhone.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verrificationId = s;
                token = forceResendingToken;
                sendotp.setEnabled(false);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
    }

    public void verifyPhoneNumber(String phoneNum){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(fAuth)
                .setActivity(this)
                .setPhoneNumber(phoneNum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void authenticateUser(PhoneAuthCredential credential){
        String fullname = getIntent().getExtras().getString("fullname");
        String email = getIntent().getExtras().getString("email");
        String username = getIntent().getExtras().getString("username");
        String pass = getIntent().getExtras().getString("pass");
        String phone = phonenumber.getEditText().getText().toString().trim();

        fAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                User usercreate = new User(fullname,email,phone,pass,username);

                FirebaseDatabase.getInstance().getReference("users")
                        .child(username)
                        .setValue(usercreate)
                        ;
                Toast.makeText(VerifyPhone.this,"Success",Toast.LENGTH_LONG).show();

                startActivity(new Intent(getApplicationContext(),Menu.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(VerifyPhone.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}