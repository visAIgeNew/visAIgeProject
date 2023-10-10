package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

// 아이디를 입력해서 qr 코드를 생성

public class MainActivity4 extends AppCompatActivity {
    private Button createBtn;
    private EditText input_QR_user;
    private Button qr_go_main;
    private String login_user_id;
    private HashMap<String, String> qr_info = new HashMap<String, String>();

    private static final String SECRET_KEY = "1234567890123456"; // qr 문자열 암호화할 때 사용할 키

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference().child("user");

    private String tmp_name = "error"; // 존재하지 않는 아이디 입력 여부를 체크하기 위한 변수

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");

        input_QR_user = findViewById(R.id.input_QR_user);
        qr_go_main = findViewById(R.id.qr_go_main);

        // 메인화면으로 이동
        qr_go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity4.this, MainActivity.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent);
            }
        });

        createBtn = findViewById(R.id.create_QR);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input_QR_user.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity4.this, "값을 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    // 입력한 특정 아이디를 통해 db에서 해당 아이디의 데이터를 가져옴
                    databaseReference.child(input_QR_user.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userList group = dataSnapshot.getValue(userList.class);
                            // {id:"입력한 아이디"}
                            qr_info.put("id",input_QR_user.getText().toString());
                            try {
                                // {name : "입력한 아이디의 이름"}
                                qr_info.put("name", group.getName());
                                tmp_name = group.getName();
                            }catch (NullPointerException e) {
                                Toast.makeText(MainActivity4.this, "존재하지 않는 아이디입니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity4.this, "에러", Toast.LENGTH_SHORT).show();
                        }
                    });

                    if(tmp_name.equals("error")) {
                        // 존재하지 않는 아이디를 입력한 경우 아무 동작도 실행하지 않음
                    }
                    else {
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try{
                            String qr_info_str = qr_info.toString();

                            // qr에 담을 문자열 암호화 과정
                            qr_encryption crypto = new qr_encryption(SECRET_KEY);
                            String encryptText = crypto.encrypt(qr_info_str);

                            // qr 생성
                            BitMatrix bitMatrix = multiFormatWriter.encode(encryptText, BarcodeFormat.QR_CODE,200,200);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                            // qr을 string으로 변환
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] reviewImage = stream.toByteArray();
                            String simage = byteArrayToBinaryString(reviewImage);

                            // qr 이미지를 db에 저장
                            databaseReference.child(input_QR_user.getText().toString()).child("qr_code").child("img").setValue(simage);

                            // qr이 생성된 날짜 저장
                            databaseReference.child(input_QR_user.getText().toString()).child("qr_code").child("date").setValue(getTime());

                            // 만들어진 qr에 담긴 아이디를 저장
                            databaseReference.child(input_QR_user.getText().toString()).child("qr_code").child("lock_user").setValue(login_user_id);

                            Toast.makeText(MainActivity4.this, "QR코드 생성 성공", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                }
            }
        });
    }

    // 생성된 QR 코드를 String으로 변환하기 위해 필요한 함수
    public static String byteArrayToBinaryString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<b.length; ++i) {
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }

    public static String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for(int bit = 0; bit<8; bit++) {
            if(((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    // 현재 시간 가져오는 함수
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}