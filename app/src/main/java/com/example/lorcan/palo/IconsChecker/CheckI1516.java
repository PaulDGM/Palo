package com.example.lorcan.palo.IconsChecker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.lorcan.palo.IconListJSON;
import com.example.lorcan.palo.MyApplicationContext;
import com.example.lorcan.palo.SendIconToDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckI1516 {
    private static String fileName = "Date.json";

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

    public void check1516(Date date) {

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("mm");
        DateFormat dayFormat = new SimpleDateFormat("dd");

        String month = dateFormat.format(date);
        String day = dayFormat.format(date);

        String old = getData(MyApplicationContext.getAppContext());
        System.out.println(old);
        try {
            if (old == null) {
                createNewDBDeleteOld("{ \"Date\" : ['0', '0', '0']}"); // [day, month, count]
                old = getData(MyApplicationContext.getAppContext());
            }
            JSONObject jsonObject = new JSONObject(old);
            JSONArray jsonArray = jsonObject.getJSONArray("Date");

            int count = Integer.parseInt(jsonArray.get(0).toString());



            IconListJSON iconListJSON = new IconListJSON();
            int isIcon = iconListJSON.getIcon(14);
            if (isIcon == 0) {
                TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                final String android_id = tManager.getDeviceId();



                int oldday = Integer.parseInt(jsonArray.get(0).toString());
                int currentday = Integer.parseInt(day);

                int oldmonth = Integer.parseInt(jsonArray.get(1).toString());
                int currentmonth = Integer.parseInt(month);

                if(currentday == oldday+1 && currentmonth == oldmonth) {
                    count = count + 1;
                }
                jsonArray.put(0, String.valueOf(day));
                jsonArray.put(1, String.valueOf(month));
                jsonArray.put(2, String.valueOf(count));


                if(count == 7){
                    SendIconToDB sendIconToDB = new SendIconToDB();
                    sendIconToDB.sendIcon("15", android_id);
                    iconListJSON.setIcon(15);
                }

                createNewDBDeleteOld("{ \"Date\" : "+jsonArray.toString()+"}");
            }
            int isIcon1 = iconListJSON.getIcon(15);
            if (isIcon1 == 0) {
                TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                final String android_id = tManager.getDeviceId();

                int oldday = Integer.parseInt(jsonArray.get(0).toString());
                int currentday = Integer.parseInt(day);

                int oldmonth = Integer.parseInt(jsonArray.get(1).toString());
                int currentmonth = Integer.parseInt(month);

                if(currentday == oldday+1 && currentmonth == oldmonth) {
                    count = count + 1;
                }
                jsonArray.put(0, String.valueOf(day));
                jsonArray.put(1, String.valueOf(month));
                jsonArray.put(2, String.valueOf(count));



                if(count == 30){
                    SendIconToDB sendIconToDB = new SendIconToDB();
                    sendIconToDB.sendIcon("16", android_id);
                    iconListJSON.setIcon(16);
                }

                createNewDBDeleteOld("{ \"Date\" : "+jsonArray.toString()+"}");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
