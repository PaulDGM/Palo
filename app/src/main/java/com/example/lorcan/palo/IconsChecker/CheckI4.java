package com.example.lorcan.palo.IconsChecker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.example.lorcan.palo.IconListJSON;
import com.example.lorcan.palo.MyApplicationContext;
import com.example.lorcan.palo.SendIconToDB;

public class CheckI4 {
    public void check4(){
        IconListJSON iconListJSON = new IconListJSON();
        int isIcon = iconListJSON.getIcon(3);
        if(isIcon == 0){
            TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            final String android_id = tManager.getDeviceId();
            SendIconToDB sendIconToDB = new SendIconToDB();
            sendIconToDB.sendIcon("4", android_id);
            iconListJSON.setIcon(4);
        }
    }
}
