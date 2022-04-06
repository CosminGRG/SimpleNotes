package com.CalculMobil.simplenotes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CalculMobil.simplenotes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {
    EditText rUserName,rUserEmail,rUserPass,rUserConfPass;
    Button syncAccount;
    TextView loginAct;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("New account");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C53F15")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rUserName = findViewById(R.id.userName);
        rUserEmail = findViewById(R.id.userEmail);
        rUserPass = findViewById(R.id.password);
        rUserConfPass = findViewById(R.id.passwordConfirm);


        syncAccount = findViewById(R.id.createAccount);
        loginAct = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar4);

        fAuth = FirebaseAuth.getInstance();

        loginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        syncAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uUsername = rUserName.getText().toString();
                String uUserEmail = rUserEmail.getText().toString();
                String uUserPass = rUserPass.getText().toString();
                String uUserConfPass = rUserConfPass.getText().toString();

                if(uUserEmail.isEmpty() || uUsername.isEmpty() || uUserPass.isEmpty() || uUserConfPass.isEmpty())
                {
                    Toast.makeText(Register.this, "All field are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                    if(!uUserPass.equals(uUserConfPass))
                    {
                        rUserConfPass.setError("Password does not match");
                    }
                    progressBar.setVisibility(View.VISIBLE);

                AuthCredential credential = EmailAuthProvider.getCredential(uUserEmail,uUserPass);
                    fAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(Register.this, "Notes are synced", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));


                            //save user credential
                            FirebaseUser usr = fAuth.getCurrentUser();
                            UserProfileChangeRequest request =new UserProfileChangeRequest.Builder().setDisplayName(uUsername)
                                    .build();
                            usr.updateProfile(request);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Failed to Connect.Try Again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.VISIBLE);


                        }
                    });






            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        startActivity(new Intent(this,MainActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }
}
