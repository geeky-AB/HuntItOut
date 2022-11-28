package com.example.uccquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private DatabaseReference dbRef;
    private FirebaseDatabase pathDatabase;

    EditText emailSignInTxt;
    EditText passSignInTxt;
    EditText teamLeaderName;
    EditText teamID;
    EditText phn;
    Button signInBtn;

    ProgressDialog progressDialog;

    int max = 4;
    int min = 1;
//    int ct = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        emailSignInTxt = findViewById(R.id.signInEmail);
        passSignInTxt = findViewById(R.id.signInPass);
        teamLeaderName = findViewById(R.id.leaderNameEdt);
        teamID = findViewById(R.id.teamId);
        signInBtn = findViewById(R.id.signIn);
        phn = findViewById(R.id.phnNo);

        progressDialog = new ProgressDialog(this);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailSignIn = emailSignInTxt.getText().toString().trim();
                String passSignIn = passSignInTxt.getText().toString();
                String teamLd = teamLeaderName.getText().toString();
                String teamId = teamID.getText().toString();
                String phnNo = phn.getText().toString();
                if(TextUtils.isEmpty(emailSignIn)) {
                    emailSignInTxt.setError("This field is mandatory");
                    return;
                }
                if(TextUtils.isEmpty(passSignIn)) {
                    passSignInTxt.setError("This field is mandatory");
                    return;
                }
                if(passSignIn.length()<6){
                    passSignInTxt.setError("Password must be at least 6 digits long");
                    return;
                }
                if(TextUtils.isEmpty(teamLd)) {
                    teamLeaderName.setError("This field is mandatory");
                    return;
                }
                if(TextUtils.isEmpty(teamId)) {
                    teamID.setError("This field is mandatory");
                    return;
                }
                if(TextUtils.isEmpty(phnNo)){
                    phn.setError("This field is mandatory");
                    return;
                }
                if(phnNo.length()<6){
                    passSignInTxt.setError("Phone number must be 10 digits long");
                    return;
                }

                registerUser(emailSignIn, passSignIn, teamLd, teamId, phnNo);
            }
        });

    }

    private void registerUser(String email, String password, String leaderName, String teamId, String phnNo) {
        progressDialog.setMessage("Registering..");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String currUserKey = user.getUid();

                            // Sign in success, update UI with the signed-in user's information
                            int pathNo = (int)(Math.random()*(max*min))+min;
                            int progCount = 0;
                            User userInfo = new User(email, leaderName, teamId, pathNo, "NO", progCount, phnNo,0,0, "NO", 2);
//                            User pathNumber = new User(pathNo);

                            dbRef.child("users").child(currUserKey).setValue(userInfo);

                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignupActivity.this, "Email verification Sent !!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignupActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
//                            boolean flag = verifyEmail(email, user);

                            Intent intent = new Intent(getApplicationContext(),VerificationActivity.class);
//                            intent.putExtra("PathNo",pathNo);

                            startActivity(intent);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
//                            Toast.makeText(SignupActivity.this, "Error while Sign-In", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    public boolean verifyEmail(String email, FirebaseUser user){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//        LayoutInflater inflator= LayoutInflater.from(getApplicationContext());
//        View view =inflator.inflate(R.layout.verification_email,null);
//        builder.setView(view);
//        AlertDialog dialog =builder.create();
//        TextView emailCon = view.findViewById(R.id.emailTxt);
//        emailCon.setText(email);
//
//
//
//        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(SignupActivity.this, "Email verification sent!!", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//                ct = 1;
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(SignupActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//                ct = 0;
//            }
//        });
//        dialog.setCancelable(false);
//        dialog.show();
//        if(ct == 0) return false;
//        else return true;
//    }

}