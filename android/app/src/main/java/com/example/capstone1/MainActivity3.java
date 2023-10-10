package com.example.capstone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// otp key를 입력만 하면 되기 때문에 ui 수정 필요
public class MainActivity3 extends AppCompatActivity {
    private EditText otp_input;
    private Button main_btn2;
    private Button input_otp_btn;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRootRef = firebaseDatabase.getReference();

    private String otpkey;
    private String login_user_id;

    // otp 화면으로 넘어올 때 메인화면에서 로그인된 사용자의 아이디 넘겨줘야 함
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");

        otp_input = findViewById(R.id.otp_input);
        main_btn2 = findViewById(R.id.main_btn2);
        input_otp_btn = findViewById(R.id.input_otp_btn);

        // 메인화면으로 이동
        main_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity3.this, MainActivity.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });

        // 로그인된 아이디에서 otp 계정 가져오기
        mRootRef.child("user").child(login_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList group = dataSnapshot.getValue(userList.class);
                otpkey = group.getOtp_key();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // 올바른 otp key를 입력했는지 확인
        input_otp_btn.setOnClickListener(new View.OnClickListener() {
            DatabaseReference conditionRef = mRootRef.child("check_otp");
            
            @Override
            public void onClick(View view) {
                if(otp_input.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "값을 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean check = checkCode(otp_input.getText().toString(), otpkey); //EditText에 입력한 otp 코드와 생성된 otp 코드 비교
                    if(check) {
                        Toast.makeText(getApplicationContext(), "도어락 키패드가 활성화됩니다.", Toast.LENGTH_SHORT).show();
                        conditionRef.setValue("true");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "otp 번호를 다시 입력해주세요!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // 이 함수가 otp key를 생성하는 부분이라서 회원가입할 때 한번 호출하기만 하면 됨 그 이후에는 호출 필요없음
    public static void generate(String userId) {
        byte[] userId_byte = userId.getBytes();
        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(userId_byte, 10);
        byte[] bEncodedKey = codec.encode(secretKey);

        String encodedKey = new String(bEncodedKey);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(userId).child("otp_key");
        databaseReference.setValue(encodedKey);
    }

    // 올바른 otp 코드를 입력했는지 체크
    public boolean checkCode(String userCode, String otpkey) {
        long otpnum = Integer.parseInt(userCode); // Google OTP 앱에 표시되는 6자리 숫자
        long wave = new Date().getTime() / 30000; // Google OTP의 주기는 30초
        boolean result = false;
        try {
            Base32 codec = new Base32();
            byte[] decodedKey = codec.decode(otpkey);
            int window = 3;
            for (int i = -window; i <= window; ++i) {
                long hash = verify_code(decodedKey, wave + i);
                if (hash == otpnum) result = true;
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[20 - 1] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return (int) truncatedHash;
    }

//    public static String getQRBarcodeURL(String user, String host, String secret) {
//        // QR코드 주소 생성
//        String format2 = "http://chart.apis.google.com/chart?cht=qr&chs=200x200&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s&chld=H|0";
//        return String.format(format2, user, host, secret);
//    }
}