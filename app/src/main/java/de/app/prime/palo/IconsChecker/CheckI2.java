package de.app.prime.palo.IconsChecker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import de.app.prime.palo.IconListJSON;
import de.app.prime.palo.MyApplicationContext;
import de.app.prime.palo.SendIconToDB;

import de.app.prime.palo.IconListJSON;

public class CheckI2 {

    public void check2(){
        IconListJSON iconListJSON = new IconListJSON();
        int isIcon2 = iconListJSON.getIcon(1);
        if(isIcon2 == 0){
            TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            final String android_id = tManager.getDeviceId();
            SendIconToDB sendIconToDB = new SendIconToDB();
            sendIconToDB.sendIcon("2", android_id);
            iconListJSON.setIcon(2);
        }
    }

}
