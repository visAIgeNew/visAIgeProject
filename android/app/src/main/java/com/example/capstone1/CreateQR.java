package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 1. 사용자가 특정 id를 editText에 입력하면
 * 2. 해당 아이디로 db에서 모든 정보를 가져온 후 qr 코드를 생성한다
 * 3. 생성된 qr코드 정보를 db에 저장한다.
 * 3-1. 현재 qr코드에 사용자 이름을 넣어서 db에 qr정보-사용자 이름을 넣는다 -> 이름 + 아이디 + 이메일 ?? 등등 추가적인 정보 등록
 * 3-2. rpi에서 qr코드를 인식한 후 인식된 이름이 db에 저장되어 있는지 판별 -> 이건 어떻게 수정...?
 * **/

public class CreateQR extends AppCompatActivity {
    private ImageView iv;
    private String login_user_id;
    private Button main_btn;
    private Button camera_btn;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference().child("user");

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        iv = (ImageView)findViewById(R.id.qrcode);

        // db에 로그인된 사용자 아이디로 생성된 qr 코드가 저장되어 있다고 가정
        databaseReference.child(login_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList group = dataSnapshot.getValue(userList.class);

                String image = group.getQr_code().get("img");
                if(image.isEmpty()) {
                    Toast.makeText(CreateQR.this, "등록된 qr코드가 없습니다.", Toast.LENGTH_SHORT).show();
                    iv.setImageResource(R.drawable.no_qr_img);
                }
                else {
                    Date today = null;
                    Date qr_date = null;
                    try {
                        qr_date = mFormat.parse(group.getQr_code().get("date"));
                        today = mFormat.parse(getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(qr_date);
                        cal.add(Calendar.DATE, 7);
                        qr_date = cal.getTime();

                        if(qr_date.compareTo(today) <= 0 ){
                            //데이터베이스에서 qr 정보 삭제
                            delete_qr();
                            Toast.makeText(CreateQR.this, "QR코드는 일주일 후 삭제됩니다.", Toast.LENGTH_SHORT).show();
                            iv.setImageResource(R.drawable.no_qr_img);
                        }
                        else {
                            byte[] b = binaryStringToByteArray(image);
                            ByteArrayInputStream is = new ByteArrayInputStream(b);
                            Drawable reviewImage = Drawable.createFromStream(is, "reviewImage");
                            iv.setImageDrawable(reviewImage);
                        }
                    }catch (NullPointerException e) {}
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CreateQR.this, "에러", Toast.LENGTH_SHORT).show();
            }
        });

        main_btn = findViewById(R.id.main_button);
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateQR.this, MainActivity.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });

        camera_btn = findViewById(R.id.camera_button);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.getReference().child("qr_camera").setValue("true");
                Toast.makeText(CreateQR.this, "QR을 인식해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static byte[] binaryStringToByteArray(String s) {
        int count = s.length() / 8;
        byte[] b = new byte[count];
        for (int i = 1; i<count; i++) {
            String t = s.substring((i-1) * 8, i*8);
            b[i-1] = binaryStringToByte(t);
        }
        return b;
    }

    public static byte binaryStringToByte(String s) {
        byte ret = 0, total = 0;
        for (int i = 0; i<8; i++) {
            ret = (s.charAt(7-i) == '1') ? (byte)(1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    private void delete_qr() {
        databaseReference.child(login_user_id).child("qr_code").child("date").setValue("");
        databaseReference.child(login_user_id).child("qr_code").child("img").setValue("");
        databaseReference.child(login_user_id).child("qr_code").child("lock_user").setValue("");
    }
}