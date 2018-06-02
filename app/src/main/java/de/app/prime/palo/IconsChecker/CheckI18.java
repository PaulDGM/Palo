package de.app.prime.palo.IconsChecker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import de.app.prime.palo.IconListJSON;
import de.app.prime.palo.MyApplicationContext;
import de.app.prime.palo.SendIconToDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class CheckI18 {
    private static String fileName = "5profilecalls.json";

    private static void createNewDBDeleteOld(String nameJSON) {
        try {
            System.out.println("New created DB: " + nameJSON);
            FileWriter file = new FileWriter(MyApplicationContext.getAppContext().getFilesDir().getPath() + "/" + fileName);

            file.write(nameJSON);
            file.flush();
            file.close();
        } catch (IOException e) {
            Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
        }
    }

    static String getData(Context context) {

        try {
            File f = new File(context.getFilesDir().getPath() + "/" + fileName);
            //check whether file exists
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
            return null;
        }
    }

    public void check18() {

        String old = getData(MyApplicationContext.getAppContext());
        System.out.println(old);
        try {
            if (old == null) {
                createNewDBDeleteOld("{ \"Profilecall\" : ['0']}");
                old = getData(MyApplicationContext.getAppContext());
            }


            JSONObject jsonObject = new JSONObject(old);
            JSONArray jsonArray = jsonObject.getJSONArray("Profilecall");


            IconListJSON iconListJSON = new IconListJSON();
            int isIcon = iconListJSON.getIcon(17);
            if (isIcon == 0) {
                TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                int currentCnt = Integer.parseInt(jsonArray.get(0).toString());
                currentCnt = currentCnt+1;
                jsonArray.put(0, String.valueOf(currentCnt));
                createNewDBDeleteOld("{ \"Profilecall\" : "+jsonArray.toString()+"}");

                final String android_id = tManager.getDeviceId();
                if (jsonArray.get(0).toString() == "5") {
                    SendIconToDB sendIconToDB = new SendIconToDB();
                    sendIconToDB.sendIcon("18", android_id);
                    iconListJSON.setIcon(18);
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
