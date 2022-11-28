package com.example.uccquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    //firebase
    private FirebaseDatabase pathDatabase;
    private DatabaseReference userRef;
    private DatabaseReference pathRef;
    private DatabaseReference userInformation;

    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    //lists
    List<String> listPath;
    List<String> listVal;
    //image views and all
    TextView hint;
    TextView wrongCntOne;
    TextView wrongCntTot;
    Button submitBtn;
    ImageView instruc;
//    Button logOutBtn;
    EditText clueEnter;

    int count = 0;
    int wrongCount= 0;
    int wrongCountOneStage = 0;
//    private int pathNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        pathDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        userRef = pathDatabase.getReference().child("users").child(currUser.getUid());

        getPathNumber(new MyCallBack() {
            @Override
            public void onCallBack(int pathNumber, int progCount) {
                Log.d("TAG",String.valueOf(pathNumber));
                setPathNumber(pathNumber,progCount);
            }
        });

        hint = findViewById(R.id.hintPlace);
        submitBtn = findViewById(R.id.submit);
        wrongCntOne = findViewById(R.id.oneStageWrong);
        wrongCntTot = findViewById(R.id.totalWrong);
        clueEnter = findViewById(R.id.enterClue);
        instruc = findViewById(R.id.instruction);
        instruc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instructionDialog();
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();
        pathDatabase.getReference().child("users").child(currUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                wrongCount = user.getWrongCnt();
                wrongCountOneStage = user.getWrongCountOneStage();
                wrongCntOne.setText("This Stage : "+String.valueOf(wrongCountOneStage));
                wrongCntTot.setText("Total : "+String.valueOf(wrongCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void instructionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.instruction,null);
        builder.setView(view1);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView instruction = view1.findViewById(R.id.instruction1);
        Button confirm = view1.findViewById(R.id.confirm);
        instruction.setTextSize(18);
        instruction.setText("1) The riddles shown to you corresponds to a spot in the College, identify that spot and visit it.\n2) You will  find an alphabet at that spot, enter that alphabet in the space provided.\n3) If the answer is correct, you will proceed to next riddle. If the answer is wrong you have to re enter the correct answer to move forward with the game.\n4) Remember if you enter wrong answer 3 times in a row or a total of 5 times, you will be disqualified and will be automatically logged out of the game.");
        confirm.setText("DONE");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void pathNo(){

    }
    public interface MyCallBack{
        void onCallBack(int pathNumber, int progCount);
    }
    public void getPathNumber(MyCallBack myCallBack){
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userInfo = snapshot.getValue(User.class);
                int pathNumber = userInfo.getPathNo();
                int progCount = userInfo.getProgCount();
                if(progCount == 8){
                    Intent intent = new Intent(QuizActivity.this,FinalStage.class);
                    startActivity(intent);
                }
                myCallBack.onCallBack(pathNumber,progCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setPathNumber(int pathNumber, int progCount){

        listPath = new ArrayList<>();
        listVal = new ArrayList<>();

        pathRef = pathDatabase.getReference().child(String.valueOf(pathNumber));
        pathRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String val = dataSnapshot.getValue(String.class);
                    assert val != null;
                    if(count<progCount){
                        count++;
                        continue;
                    }
                    String val1 = val.substring(0,val.length()-1);
                    String val2 = val.substring(val.length()-1);
                    listPath.add(val1);
                    listVal.add(val2);

                }
                if(listPath.size() != 0){
                    playQuiz(listPath, listVal,progCount);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void playQuiz(List<String> listPath, List<String> listVal, int progCount) {
        hint.setText(listPath.get(0));
        String currUserKey = currUser.getUid();
        userInformation = pathDatabase.getReference().child("users").child(currUserKey);
        count = progCount;
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clueEnter.getText().toString().trim().equalsIgnoreCase(listVal.get(0))){
                    count++;
                    wrongCountOneStage = 0;
                    userInformation.child("wrongCountOneStage").setValue(wrongCountOneStage);
                    wrongCntOne.setText("This Stage : "+String.valueOf(wrongCountOneStage));
                    userInformation.child("progCount").setValue(count);
                    listPath.remove(0);
                    listVal.remove(0);
                    clueEnter.setText("");
                    if(count == 8){
//                        userInformation.child("status").setValue("YES");
                        Intent intent = new Intent(QuizActivity.this,FinalStage.class);
                        startActivity(intent);
//                        return;
                    }
                    playQuiz(listPath,listVal,count);

                }
                else{
                    clueEnter.setError("Wrong Answer");
                    clueEnter.setText("");
                    wrongCountOneStage++;
                    wrongCount++;
                    if( wrongCountOneStage == 3 || wrongCount == 5){
                        showDisqualifyDialog();
                    }
                    userInformation.child("wrongCnt").setValue(wrongCount);
                    userInformation.child("wrongCountOneStage").setValue(wrongCountOneStage);
                    wrongCntOne.setText("This Stage : "+String.valueOf(wrongCountOneStage));
                    wrongCntTot.setText("Total : "+String.valueOf(wrongCount));
                }
            }
        });


    }

    public void showDisqualifyDialog(){
        pathDatabase.getReference().child("users").child(currUser.getUid()).child("disqualified").setValue("YES");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You are Disqualified");
        builder.setCancelable(false);
        builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(QuizActivity.this, "Disqualified", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}