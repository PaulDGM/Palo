package com.example.lorcan.palo.IconsChecker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.example.lorcan.palo.IconListJSON;
import com.example.lorcan.palo.MyApplicationContext;
import com.example.lorcan.palo.SendIconToDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckI10 {
    public DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public DateFormat timeFormat = new SimpleDateFormat("HH");
    public Date date = new Date();
    public void check10(){

        IconListJSON iconListJSON = new IconListJSON();
        int isIcon = iconListJSON.getIcon(9);
        if(isIcon == 0){
            TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }


            final String android_id = tManager.getDeviceId();
            int currentTime = Integer.parseInt(timeFormat.format(date));
            if(currentTime < 5 && currentTime > 2) {
                SendIconToDB sendIconToDB = new SendIconToDB();
                sendIconToDB.sendIcon("10", android_id);
                iconListJSON.setIcon(10);
            }
        }
    }
}
