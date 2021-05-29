package com.example.lifeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private Button btnsignup;
    private TextInputLayout fullname,email,username,pass,passagain;
    FirebaseDatabase fAuth;
    DatabaseReference reference;
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        btnsignup = findViewById(R.id.btnSignup);
        fullname = findViewById(R.id.edtFullname);
        email = findViewById(R.id.edtEmail);
        username = findViewById(R.id.edtAccount);
        pass = findViewById(R.id.edtPass);
        passagain = findViewById(R.id.edtConfirmPass);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FullName = fullname.getEditText().getText().toString().trim();
                String Email = email.getEditText().getText().toString().trim();
                String UserName = username.getEditText().getText().toString().trim();
                String PassW = pass.getEditText().getText().toString().trim();
                String RePass = passagain.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(FullName)){
                    fullname.setError("Firstname is Required");
                    return;
                }else fullname.setError(null);


                if(TextUtils.isEmpty(UserName)){
                    username.setError("Username is Required");
                    return;
                }else username.setError(null);

                if(TextUtils.isEmpty(Email) || !isValidEmailId(email.getEditText().getText().toString().trim()) ){
                    email.setError("Email is Required");
                    email.requestFocus();
                    return;

                }else fullname.setError(null);

                if(TextUtils.isEmpty(PassW)){
                    pass.setError("Password is Required");
                    return;
                }

                if(pass.getEditText().getText().length() < 8){
                    pass.setError("Password must be >=8");
                    return;
                }else pass.setError(null);

                if(TextUtils.isEmpty(RePass)){
                    passagain.setError("Confirm Password is Required");
                    return;
                }
                if(!PassW.equals(RePass)){
                    passagain.setError("Password not matching ");
                    passagain.requestFocus();
                    return;
                }else passagain.setError(null);
                Intent intent = new Intent(getApplication(),VerifyPhone.class);

                intent.putExtra("fullname",FullName);
                intent.putExtra("email",Email);
                intent.putExtra("username",UserName);
                intent.putExtra("pass",PassW);

                startActivity(intent);
            }
        });


    }
}