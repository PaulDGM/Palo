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

public class CheckI11 {
    @SuppressLint("SimpleDateFormat")
    public DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    public DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    @SuppressLint("SimpleDateFormat")
    public DateFormat timeFormat = new SimpleDateFormat("ss");
    public Date date = new Date();

    private static String fileName = "10StatusPosts.json";

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

    public void check10(Date datePost) {

        String old = getData(MyApplicationContext.getAppContext());
        System.out.println(old);
        try {
            if (old == null) {
                createNewDBDeleteOld("{ \"StatusCount\" : ['0']}");
                old = getData(MyApplicationContext.getAppContext());
            }


            JSONObject jsonObject = new JSONObject(old);
            JSONArray jsonArray = jsonObject.getJSONArray("StatusCount");


            System.out.println(dateTimeFormat.format(date)); //2016/11/16 12:08:43

            IconListJSON iconListJSON = new IconListJSON();
            int isIcon = iconListJSON.getIcon(10);
            if (isIcon == 0) {
                TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                if(dateFormat.format(datePost) == dateFormat.format(date)){
                    int currentCnt = Integer.parseInt(jsonArray.get(0).toString());
                    currentCnt = currentCnt +1;
                    jsonArray.put(0, String.valueOf(currentCnt));
                    createNewDBDeleteOld("{ \"StatusCount\" : "+jsonArray.toString()+"}");
                }

                final String android_id = tManager.getDeviceId();
                if (jsonArray.get(0).toString() == "10") {
                    SendIconToDB sendIconToDB = new SendIconToDB();
                    sendIconToDB.sendIcon("11", android_id);
                    iconListJSON.setIcon(11);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
