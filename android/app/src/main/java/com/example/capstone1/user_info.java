package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// 사용자 정보 출력

public class user_info extends AppCompatActivity {

    private Button userInfo_main;
    private TextView user_name_data;
    private TextView user_phone_data;
    private TextView user_otp_data;
    private TextView user_id;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String login_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");

        user_name_data = findViewById(R.id.user_info_name_data);
        user_phone_data = findViewById(R.id.user_info_num_data);
        user_otp_data = findViewById(R.id.user_info_otp_data);
        user_id = findViewById(R.id.user_info_id);

        userInfo_setting();

        userInfo_main = findViewById(R.id.userInfo_main_btn);

        // 메인화면으로 이동
        userInfo_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_info.this, MainActivity.class);
                intent.putExtra("login_user_id", login_user_id); // 메인화면으로 이동할 때 id값을 계속 넘겨줘야함
                startActivity(intent); //실제 화면 이동
            }
        });
    }

    void userInfo_setting() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("user");

        // 로그인된 아이디에 해당하는 사용자 정보를 db에서 가져옴
        databaseReference.child(login_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList group = dataSnapshot.getValue(userList.class);
                //Toast.makeText(user_info.this, group.getName(), Toast.LENGTH_SHORT).show();

                // 각각 이름, 아이디, otp 계정을 출력 (get 코드를 더 추가하여 정보를 더 가져올 수 있음)
                user_name_data.setText(group.getName());
                user_phone_data.setText(group.getPhone_num());
                user_otp_data.setText(group.getOtp_key());
                user_id.setText(login_user_id + "님의 정보");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(user_info.this, "에러", Toast.LENGTH_SHORT).show();
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

    }
}