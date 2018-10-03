package com.rimson.c.dailyarticle.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rimson.c.dailyarticle.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_launch);

        TextView textView=(TextView)findViewById(R.id.launchText);
        textView.setText("每日一文");

        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(800);
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
