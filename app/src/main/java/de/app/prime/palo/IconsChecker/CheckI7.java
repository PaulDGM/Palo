package de.app.prime.palo.IconsChecker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import de.app.prime.palo.IconListJSON;
import de.app.prime.palo.MyApplicationContext;
import de.app.prime.palo.SendIconToDB;

public class CheckI7 {
    public void check7(){
        IconListJSON iconListJSON = new IconListJSON();
        int isIcon = iconListJSON.getIcon(6);
        if(isIcon == 0){
            TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            final String android_id = tManager.getDeviceId();
            SendIconToDB sendIconToDB = new SendIconToDB();
            sendIconToDB.sendIcon("7", android_id);
            iconListJSON.setIcon(7);
        }
    }
}
