package com.example.uccquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerificationActivity extends AppCompatActivity {


    TextView emailVerify;
    EditText token;
    Button submit;
    Button logout;
    FirebaseAuth mAuth;
    FirebaseUser currUser;
    ImageView instructionVerification;
    DatabaseReference pathDatabase;

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        emailVerify = findViewById(R.id.verified);
        token = findViewById(R.id.token);
        submit = findViewById(R.id.submitBtn);
        instructionVerification = findViewById(R.id.instructionVerify);
        logout = findViewById(R.id.logoutVerify);
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        pathDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currUser.getUid());

        if(currUser.isEmailVerified()){
            emailVerify.setText("Email Verified !!");

        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignOut();
            }
        });
        instructionVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInstruction();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tokenAns = token.getText().toString();
                if(tokenAns.trim().equalsIgnoreCase("ucc@hunt") && currUser.isEmailVerified()){
                    Intent intent = new Intent(VerificationActivity.this, QuizActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(VerificationActivity.this, "Error: Either token is wrong or email is not verified!!", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        pathDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currUser.getUid());
        pathDatabase.child("disqualified").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue().toString().equals("YES")){
                            showDisDial();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void showDisDial(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You are Disqualified");
        builder.setCancelable(false);
        builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(VerificationActivity.this, "Disqualified", Toast.LENGTH_SHORT).show();
                userSignOut();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void userSignOut(){
        mAuth.signOut();
        Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void dialogInstruction(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.instruction,null);
        builder.setView(view1);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView instruction = view1.findViewById(R.id.instruction1);
        Button confirm = view1.findViewById(R.id.confirm);
        instruction.setTextSize(20);
        instruction.setText("1) If you didn't receive any verification mail, please logout and sign-up again with valid Email.\n2)Once your email is verified, logout and login again with same id and password.This time you will get your updated verification status.\n3) Enter the token provided by team to start the Hunt.");
        confirm.setText("Done");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}