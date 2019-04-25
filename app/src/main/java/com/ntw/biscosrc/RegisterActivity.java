package com.ntw.biscosrc;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegister;
    private TextView mLogin;
    private EditText mName, mEmail, mPhone, mPassword, mRePassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(RegisterActivity.this, ScannerActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mName = (EditText)findViewById(R.id.name);
        mEmail = (EditText)findViewById(R.id.email);
        mPhone = (EditText)findViewById(R.id.phone);
        mPassword = (EditText)findViewById(R.id.password);
        mRePassword = (EditText)findViewById(R.id.repassword);
        mRegister = (Button)findViewById(R.id.register);

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String name = mName.getText().toString();
                final String email = mEmail.getText().toString();
                final String phone = mPhone.getText().toString();
                final String password = mPassword.getText().toString();
                final String repassword = mRePassword.getText().toString();
                if(password.equals(repassword)){
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Oops! Something went wrong.", Toast.LENGTH_LONG).show();
                            } else {
                                String user_id = mAuth.getCurrentUser().getUid();
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("users").child(user_id).child("name").setValue(name);
                                mDatabase.child("users").child(user_id).child("email").setValue(email);
                                mDatabase.child("users").child(user_id).child("phone").setValue(phone);
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("users").child("riders").child(user_id);
                                current_user_db.setValue(true);
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "The passwords entered do not match. Please try again", Toast.LENGTH_LONG).show();


                }
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    public void login(View v){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}

