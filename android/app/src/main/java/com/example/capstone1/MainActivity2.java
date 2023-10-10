package com.example.capstone1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.concurrent.Executor;


// 도어록 비밀번호 입력

public class MainActivity2 extends AppCompatActivity {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    private String login_user_id; // 로그인된 유저 아이디
    private EditText input_door_pwd;
    private Button open_door_btn; // 도어록 잠금 해제
    private String real_doorLock_pwd; // db에 등록된 실제 도어록 비밀번호
    private String input_doorLock_pwd; // 사용자가 입력한 도어록 비밀번호 값
    private Button go_main;

    private int pwd_check_num = 0; // 비밀번호 잘못 입력한 횟수 체크

    int log_count = 3; // 출입 이력 로그 저장을 위한 카운트

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");
        input_door_pwd = findViewById(R.id.input_door_pwd);
        open_door_btn = findViewById(R.id.input_door_pwd_btn);
        go_main = findViewById(R.id.door_pwd_go_main);

        // 1. 로그인된 유저 아이디에 저장된 실제 비밀번호 값 가져오기
        databaseReference.child("user").child(login_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList group = dataSnapshot.getValue(userList.class);
                real_doorLock_pwd = group.getLock_pwd();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity2.this, "에러", Toast.LENGTH_SHORT).show();
            }
        });

        open_door_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2. edit text에서 입력된 비밀번호와 로그인된 유저 아이디에 저장된 비밀번호를 비교
                input_doorLock_pwd = input_door_pwd.getText().toString();

                if(input_doorLock_pwd.isEmpty()) {
                    Toast.makeText(MainActivity2.this, "값을 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else {
                    if(input_doorLock_pwd.equals(real_doorLock_pwd)) {
                        // 3. 2번이 true이면 도어록 잠금 해제 신호 db로 전송
                        databaseReference.child("check_pwd").setValue("true");
                        // 4. 출입 이력 로그 저장
                        DatabaseReference databaseReference2 = databaseReference.child("door_open").child(login_user_id).child("pwd_lock").child(Integer.toString(log_count));
                        databaseReference2.child("date").setValue(getTime());
                        databaseReference2.child("img").setValue("");
                        databaseReference2.child("success").setValue(true);
                        databaseReference2.child("user_id").setValue(login_user_id);
                        Toast.makeText(MainActivity2.this, "도어록 잠금을 해제합니다.", Toast.LENGTH_LONG).show();
                        log_count = log_count + 1;
                    }
                    else {
                        // 5. 2번이 false이면 에러 메시지 출력
                        Toast.makeText(MainActivity2.this, "비밀번호를 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                        pwd_check_num = pwd_check_num + 1; // 잘못 입력한 횟수 1 증가
                        databaseReference.child("pwd_num").setValue(pwd_check_num);
                        if(pwd_check_num == 5) {
                            Toast.makeText(MainActivity2.this, "도어록이 비활성화됩니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent);
            }
        });
    }

    // 현재 시간 가져오는 함수
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}