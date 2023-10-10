package com.example.capstone1;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.BindingAdapter;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

// 메인화면
public class MainActivity extends AppCompatActivity {

    private Button btn_password;
    private Button btn_otp;
    private Button btn_check_QR;
    private Button btn_log;
    private Button btn_user_info;
    private Button btn_create_QR;

    //private ImageView imageView;
    //private Toolbar toolbar;
    //private NavigationView navigationView;
    private String login_user_id;

    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_main_interface);

        Intent login_intent = getIntent();
        login_user_id = login_intent.getExtras().getString("login_user_id");

        View bottomSheet = findViewById(R.id.sheet);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        //bottomSheetBehavior.setState(STATE_COLLAPSED);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == STATE_DRAGGING){

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });






        btn_password = findViewById(R.id.input_password);

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });


        btn_user_info = findViewById(R.id.button3);
        btn_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, user_info.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });

        btn_create_QR = findViewById(R.id.button4);
        btn_create_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });

        btn_check_QR = findViewById(R.id.button5);
        btn_check_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateQR.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });

        btn_log = findViewById(R.id.button6);
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity5.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });

        btn_otp = findViewById(R.id.button7);
        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                intent.putExtra("login_user_id", login_user_id);
                startActivity(intent); //실제 화면 이동
            }
        });

        /*

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView)findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.menu_pwd){
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("login_user_id", login_user_id);
                    startActivity(intent);
                }else if(id == R.id.menu_otp){
                    Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                    intent.putExtra("login_user_id", login_user_id);
                    startActivity(intent);
                }else if(id == R.id.menu_lock){
                    Intent intent = new Intent(MainActivity.this, MainActivity5.class);
                    intent.putExtra("login_user_id", login_user_id);
                    startActivity(intent);
                }else if(id == R.id.menu_create_qr){
                    Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                    intent.putExtra("login_user_id", login_user_id);
                    startActivity(intent);
                }else if(id == R.id.menu_qr){ // 생성된 qr 확인
                    Intent intent = new Intent(MainActivity.this, CreateQR.class);
                    intent.putExtra("login_user_id", login_user_id);
                    startActivity(intent);
                }else if(id == R.id.menu_user_info){ // 생성된 qr 확인
                    Intent intent = new Intent(MainActivity.this, user_info.class);
                    intent.putExtra("login_user_id", login_user_id);
                    startActivity(intent);
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


         */

        /*
        imageView = (ImageView) findViewById(R.id.main_image_view);
        registerForContextMenu(imageView);
         */
    }
}
/*
class ViewBindingAdapter {
    @BindingAdapter(value = {"bgCornerRadius"})
    public static void setBackgroundCornerRadius(View view, float cornerRadius){
        if(view==null){
            return;
        }
        Drawable drawable = view.getBackground();
        cornerRadius *= view.getResources().getDisplayMetrics().density;
        GradientDrawable gradientDrawable = GradientDrawableUtil.getGradientDrawable(drawable, cornerRadius);
        view.setBackground(gradientDrawable);
    }
}

class GradientDrawableUtil {

    public static GradientDrawable getGradientDrawable(Drawable drawable, float cornerRadius){
        GradientDrawable gradientDrawable;
        if(drawable instanceof GradientDrawable){
            gradientDrawable = (GradientDrawable) drawable;
        }else{
            gradientDrawable = new GradientDrawable();
            if(drawable instanceof ColorDrawable){
                ColorDrawable colorDrawable = (ColorDrawable) drawable;
                gradientDrawable.setAlpha(colorDrawable.getAlpha());
                gradientDrawable.setColor(colorDrawable.getColor());
            }
        }
        gradientDrawable.setCornerRadius(cornerRadius);
        return gradientDrawable;
    }
}
 */