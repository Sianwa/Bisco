package com.ntw.biscosrc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetActivity extends AppCompatActivity {

    private EditText mEmail;
    private Button mReset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset);

        mEmail = (EditText) findViewById(R.id.email);
        mReset = (Button) findViewById(R.id.reset);

        mAuth = FirebaseAuth.getInstance();
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();

                if(!isEmailValid(email)){
                    Toast.makeText(ResetActivity.this, "Please enter a viable address", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ResetActivity.this, ResetActivity.class);
                    startActivity(intent);
                }else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetActivity.this, "Email sent successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ResetActivity.this, "Please check your details and try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    public void login(View v){
        Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }
    public void register(View v){
        Intent intent = new Intent(ResetActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
