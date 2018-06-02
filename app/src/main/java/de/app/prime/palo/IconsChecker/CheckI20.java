package de.app.prime.palo.IconsChecker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import de.app.prime.palo.IconListJSON;
import de.app.prime.palo.LevelPointsConverter;
import de.app.prime.palo.MyApplicationContext;
import de.app.prime.palo.PunkteJSON;
import de.app.prime.palo.SendIconToDB;

public class CheckI20 {

    public void check20() {

        PunkteJSON punkteJSON = new PunkteJSON();
        int punkte = punkteJSON.getPoints();
        LevelPointsConverter levelPointsConverter = new LevelPointsConverter();
        String lvl = levelPointsConverter.convertPointsToLevel(punkte);
        if (lvl == "10") {
            IconListJSON iconListJSON = new IconListJSON();
            int isIcon1 = iconListJSON.getIcon(19);
            if (isIcon1 == 0) {
                TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                final String android_id = tManager.getDeviceId();
                SendIconToDB sendIconToDB = new SendIconToDB();
                sendIconToDB.sendIcon("20", android_id);
                iconListJSON.setIcon(20);
            }
        }
    }
}
