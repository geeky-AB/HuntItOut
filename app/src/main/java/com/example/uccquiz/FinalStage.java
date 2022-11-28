package com.example.uccquiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FinalStage extends AppCompatActivity {

    private EditText usernameInsta;
    private Button submit;
//    private Button instaLogo;
    private Button logoutBtn;
    private FloatingActionButton instaLogo;
    private String answer = "";
    private DatabaseReference instaRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ImageView instruc;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_stage);
        usernameInsta = findViewById(R.id.answerEdtText);
        submit = findViewById(R.id.submitBtn);
        instaLogo = findViewById(R.id.igProf);
        logoutBtn = findViewById(R.id.logout);
        instruc = findViewById(R.id.instructionFinal);
        instruc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInstruction();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        instaRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer =  usernameInsta.getText().toString();
                if(answer.trim().equalsIgnoreCase("Triangle")){
                    instaLogo.setVisibility(View.VISIBLE);
                }
                else{
                    usernameInsta.setError("Wrong Answer");
                    usernameInsta.setText("");
                }
            }
        });
        instaLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instaRef.child("status").setValue("YES");
                Uri uri = Uri.parse("https://instagram.com/hunt.it.out?igshid=YTY2NzY3YTc=");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://instagram.com/hunt.it.out?igshid=YTY2NzY3YTc=")));
                }
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(FinalStage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

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
        instruction.setText("1) You are required to guess the correct answer to the riddle using the alphabets you already discovered.\n2) Submit your answer in the given field.\n3) If your answer is correct, a button will be visible to redirect you to that IG- Profile.\n4) DM your Team Name and Leader name to that profile to successfully complete the HUNT.");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}