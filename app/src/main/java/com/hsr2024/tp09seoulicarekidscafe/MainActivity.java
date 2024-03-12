package com.hsr2024.tp09seoulicarekidscafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // http://openapi.seoul.go.kr:8088/key/xml/tnFcltySttusInfo1011/1/23/

        findViewById(R.id.tv_btn1).setOnClickListener(v -> {
            //Intent intent1 =new Intent(this,SecondActivity.class);
            //startActivity(intent1);
            Intent intent= new Intent();
            intent.setAction("tab1");
            startActivity(intent);

        });

        findViewById(R.id.tv_btn2).setOnClickListener(v -> {
           // Intent intent2 =new Intent(this,SecondActivity.class);
           //startActivity(intent2);
            Intent intent= new Intent();
            intent.setAction("tab2");
            startActivity(intent);

        });

    }
}
