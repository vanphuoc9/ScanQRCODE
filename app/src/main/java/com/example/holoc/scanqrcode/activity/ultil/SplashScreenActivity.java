package com.example.holoc.scanqrcode.activity.ultil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.holoc.scanqrcode.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        mAuth = FirebaseAuth.getInstance();
//        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//        Thread myThread = new Thread(){
//            @Override
//            public void run() {
//                try {
//                    sleep(3000);
//                    Intent intent = new Intent(SplashScreenActivity.this, DangNhapActivity.class);
//                    startActivity(intent);
//                    finish();
//                }catch(InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        myThread.start();
    }


}
