package com.example.uccquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private DatabaseReference dbRef;
    private FirebaseDatabase pathDatabase;

    //Sign-In
//    EditText emailSignInTxt;
//    EditText passSignInTxt;
//    EditText teamLeaderName;
//    EditText teamID;
//    Button signInBtn;
    //Log-In
    EditText emailLogInTxt;
    EditText passLogInTxt;
    TextView signUpText;
    Button logInBtn;
    //progress Dialog
    ProgressDialog progressDialog;

//    int max = 4;
//    int min = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

//        emailSignInTxt = findViewById(R.id.signInEmail);
//        passSignInTxt = findViewById(R.id.signInPass);
//        teamLeaderName = findViewById(R.id.leaderNameEdt);
//        teamID = findViewById(R.id.teamId);
//        signInBtn = findViewById(R.id.signIn);

        emailLogInTxt = findViewById(R.id.loginInEmail);
        passLogInTxt = findViewById(R.id.loginInPass);
        logInBtn = findViewById(R.id.logIn);
        signUpText = findViewById(R.id.signUpTxt);

        progressDialog = new ProgressDialog(this);

        if(currUser!= null){
            //jump to next Activity
            Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
            startActivity(intent);
        }

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

//        signInBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String emailSignIn = emailSignInTxt.getText().toString();
//                String passSignIn = passSignInTxt.getText().toString();
//                String teamLd = teamLeaderName.getText().toString();
//                String teamId = teamID.getText().toString();
//                if(TextUtils.isEmpty(emailSignIn)) {
//                    emailSignInTxt.setError("This field is mandatory");
//                    return;
//                }
//                if(TextUtils.isEmpty(passSignIn)) {
//                    passSignInTxt.setError("This field is mandatory");
//                    return;
//                }
//                if(TextUtils.isEmpty(teamLd)) {
//                    teamLeaderName.setError("This field is mandatory");
//                    return;
//                }
//                if(TextUtils.isEmpty(teamId)) {
//                    teamID.setError("This field is mandatory");
//                    return;
//                }
//                registerUser(emailSignIn, passSignIn, teamLd, teamId);
//            }
//        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailLogIn = emailLogInTxt.getText().toString();
                String passLogIn = passLogInTxt.getText().toString();
                if(TextUtils.isEmpty(emailLogIn)){
                    emailLogInTxt.setError("This Field is Mandatory");
                    return;
                }
                if(TextUtils.isEmpty(passLogIn)){
                    passLogInTxt.setError("This Field is Mandatory");
                    return;
                }
                logInUser(emailLogIn,passLogIn);
            }
        });

    }

    private void logInUser(String email, String password) {

        progressDialog.setMessage("Logging In...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                            startActivity(intent);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Error while Log-In", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

//    private void registerUser(String email, String password, String leaderName, String teamId) {
//        progressDialog.setMessage("Registering..");
//        progressDialog.show();
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            progressDialog.dismiss();
//                            // Sign in success, update UI with the signed-in user's information
//                            int pathNo = (int)(Math.random()*(max*min))+min;
//                            int progCount = 0;
//                            User userInfo = new User(email, leaderName, teamId, pathNo, "NO", progCount);
////                            User pathNumber = new User(pathNo);
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            String currUserKey = user.getUid();
//                            dbRef.child("users").child(currUserKey).setValue(userInfo);
//                            Intent intent = new Intent(getApplicationContext(),QuizActivity.class);
////                            intent.putExtra("PathNo",pathNo);
//                            startActivity(intent);
//                        } else {
//                            progressDialog.dismiss();
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(MainActivity.this, "Error while Sign-In", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}