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
import java.util.Date;

public class CheckI5 {
    private static String fileName = "MapAndProfile.json";

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

    public void check5(String post, String mapOrProfile, Date date) {

        String old = getData(MyApplicationContext.getAppContext());
        System.out.println(old);
        try {
            if (old == null) {
                createNewDBDeleteOld("{ \"ProfileMap\" : ['0', '0']}");
                old = getData(MyApplicationContext.getAppContext());
            }
            JSONObject jsonObject = new JSONObject(old);
            JSONArray jsonArray = jsonObject.getJSONArray("ProfileMap");

            String oldPost = jsonArray.get(0).toString();
            String oldMapOrProfile = jsonArray.get(1).toString();



            IconListJSON iconListJSON = new IconListJSON();
            int isIcon = iconListJSON.getIcon(4);
            if (isIcon == 0) {
                TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                final String android_id = tManager.getDeviceId();
                if(oldPost != post) {
                    if ((mapOrProfile == "map" && oldMapOrProfile == "profile") || (mapOrProfile == "profile" && oldMapOrProfile == "map")) {
                        SendIconToDB sendIconToDB = new SendIconToDB();
                        sendIconToDB.sendIcon("5", android_id);
                        iconListJSON.setIcon(5);
                    }
                }
                jsonArray.put(0, post);
                jsonArray.put(1, mapOrProfile);
                createNewDBDeleteOld("{ \"ProfileMap\" : "+jsonArray.toString()+"}");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
