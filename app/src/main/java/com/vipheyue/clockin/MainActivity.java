package com.vipheyue.clockin;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private TextView tv_msg;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tv_msg = findViewById(R.id.tv_msg);
        initView();

        initData();
        SharePreferencesUtil.putSpString("定时任务", "已经开启任务", MainActivity.this);//test
        tv_msg.setText("已经开启任务");
    }

    private void initView() {
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferencesUtil.putSpString("定时任务", "已经清空所有日志", MainActivity.this);
                tv_msg.setText("已经清空所有日志");

            }
        });
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                initData();
                SharePreferencesUtil.putSpString("定时任务", "已经开启任务", MainActivity.this);
                tv_msg.setText("已经开启任务");

            }
        });
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance(MainActivity.this).cancelAllWork();
                String spString = SharePreferencesUtil.getSpString("定时任务", "无", MainActivity.this);
                SharePreferencesUtil.putSpString("定时任务", spString+"\r\n已经主动停止任务", MainActivity.this);
                tv_msg.setText(spString+"\r\n已经主动停止任务");
            }
        });

        CheckBox checkbox_6 = findViewById(R.id.checkbox_6);
        checkbox_6.setChecked(SharePreferencesUtil.getSpBoolean("周六不启动", true, MainActivity.this));
        checkbox_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePreferencesUtil.putSpBoolean("周六不启动", isChecked, MainActivity.this);
            }
        });


        CheckBox checkbox_7 = findViewById(R.id.checkbox_7);
        checkbox_7.setChecked(SharePreferencesUtil.getSpBoolean("周日不启动", true, MainActivity.this));
        checkbox_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePreferencesUtil.putSpBoolean("周日不启动", isChecked, MainActivity.this);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {


        PeriodicWorkRequest lvWorkRequest =
                new PeriodicWorkRequest.Builder(StartLvSmsWorker.class, 15, TimeUnit.MINUTES)
                        .build();
        WorkManager
                .getInstance(this)
                .enqueue(lvWorkRequest);


        PeriodicWorkRequest wxWorkRequest =
                new PeriodicWorkRequest.Builder(StartQiYeWXSmsWorker.class, 16, TimeUnit.MINUTES)
                        .build();
        WorkManager
                .getInstance(this)
                .enqueue(wxWorkRequest);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String spString = SharePreferencesUtil.getSpString("定时任务", "无", this);
//        if (spString.length() > 300) {
//            spString=spString.substring(spString.length() - 200, spString.length() - 1);
//
//        }
        tv_msg.setText(spString);
    }
}