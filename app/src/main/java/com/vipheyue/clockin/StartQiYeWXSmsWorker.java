package com.vipheyue.clockin;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.sql.Time;

public class StartQiYeWXSmsWorker extends Worker {
    Context context;

    public StartQiYeWXSmsWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Result doWork() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(calendar.DAY_OF_WEEK); // 星期六 为 7  星期日为 1
        Boolean saturdayOff = SharePreferencesUtil.getSpBoolean("周六不启动", true, context);
        if (saturdayOff && dayOfWeek == 7) {
            return Result.success();
        }
        Boolean sundayoff = SharePreferencesUtil.getSpBoolean("周日不启动", true, context);
        if (sundayoff && dayOfWeek == 1) {
            return Result.success();
        }


        Time time = new Time(System.currentTimeMillis());

        String msg = "日期:" + calendar.get(Calendar.DAY_OF_MONTH) + "   " + time.getHours() + " : " + time.getMinutes() + "  企业微信  doWork";
        Log.e("定时任务", msg);
        String spString = SharePreferencesUtil.getSpString("定时任务", "无", context);



        if (time.getHours() == 9 || time.getHours() == 18) {
            //继续任务
            SharePreferencesUtil.putSpString("定时任务", spString + "\r\n" + msg, context);

        } else {
            SharePreferencesUtil.putSpString("定时任务", spString + "\r\n" + msg+" 没到时间 暂停启动", context);

            return Result.success();
        }




        if (time.getMinutes() < 35) {
            Uri uri = Uri.parse("http://s.welightworld.com:8083/push/3981f6243ff1b65286bbb29b986f21cc647200ac1dae981cd562a7408cbb8ce6?send=%E4%BC%81%E4%B8%9A%E5%BE%AE%E4%BF%A1%E5%B7%B2%E5%90%AF%E5%8A%A8---->" + time.getHours() + " : " + time.getMinutes());
            Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent2);
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        ComponentName cmp = new ComponentName("com.tencent.wework", "com.tencent.wework.launch.LaunchSplashActivity");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);


        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }
}