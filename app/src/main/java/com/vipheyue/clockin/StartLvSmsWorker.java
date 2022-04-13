package com.vipheyue.clockin;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.sql.Time;

public class StartLvSmsWorker extends Worker {
    Context context;
    public StartLvSmsWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context=context;
    }

    @Override
    public Result doWork() {
        Time time = new Time(System.currentTimeMillis());
        if (time.getHours() ==9 || time.getHours() == 18) {
            //继续任务
        }else{
            return Result.success();
        }

        String msg = time.getHours() + " : " + time.getMinutes() + "  设置界面  doWork";
        Log.e("定时任务", msg);
        String spString = SharePreferencesUtil.getSpString("定时任务", "无", context);
        SharePreferencesUtil.putSpString("定时任务",spString+"\r\n"+msg,context);




//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        ComponentName cmp = new ComponentName("com.lvyatech.wxapp.smstowx","com.lvyatech.wxapp.smstowx.MainActivity");// 这儿看换成自己想要调用的APP
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setComponent(cmp);
//        context.startActivity(intent);


        Intent intents = new Intent();
        intents.setAction("android.settings.WIFI_SETTINGS");
        intents.putExtra("back", "true");
        context.startActivity(intents);

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }

}