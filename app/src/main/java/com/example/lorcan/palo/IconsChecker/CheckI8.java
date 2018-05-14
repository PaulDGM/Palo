package com.example.lorcan.palo.IconsChecker;

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

public class CheckI8 {
    private static String fileName = "5colorsClicked.json";

    private static void createNewDBDeleteOld(String nameJSON) {
        try {
            System.out.println("New created DB Color Changer: " + nameJSON);
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

    public void checkI8(int markerNum){
        String old = getData(MyApplicationContext.getAppContext());
        System.out.println(old);
        try {
            if(old == null){
                createNewDBDeleteOld("{ \"Color\" : ['0', '0', '0', '0', '0']}");
                old = getData(MyApplicationContext.getAppContext());
            }

            System.out.println("MARKERNUM COLOR IN CHECKI8: " + markerNum);
            IconListJSON iconListJSON = new IconListJSON();
            int isIcon = iconListJSON.getIcon(7);
            if(isIcon == 0) {
                JSONObject jsonObject = new JSONObject(old);
                JSONArray jsonArray = jsonObject.getJSONArray("Color");
                boolean bool1 = true;

                for(int k = 0; k < jsonArray.length(); k++){
                    if(Integer.parseInt(jsonArray.get(k).toString()) == markerNum) {
                        bool1 = false;
                        k = jsonArray.length(); // save end of loop because k is set to high for next iteration
                    }
                }

                if(bool1) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        if (jsonArray.get(j).toString() == "0") {
                            jsonArray.put(j, markerNum);
                            j = jsonArray.length(); // save end of loop because j is set to high for next iteration
                        }
                    }
                }

                TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                final String android_id = tManager.getDeviceId();
                boolean bool = true;
                for(int i = 0 ; i < jsonArray.length(); i++){
                    if(jsonArray.get(i).toString() == "0"){
                        bool = false;
                        i = jsonArray.length(); // save end of loop because i is set to high for next iteration
                    }
                }
                if(bool){
                    SendIconToDB sendIconToDB = new SendIconToDB();
                    sendIconToDB.sendIcon("8", android_id);
                    iconListJSON.setIcon(8);
                }
                createNewDBDeleteOld("{ \"Color\" : " + jsonArray.toString() + "}");
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public int getCheck8(int i){
        String old = getData(MyApplicationContext.getAppContext());
        int cnt = 0;
        try {
            JSONObject jsonObject =  new JSONObject(old);

            JSONArray jsonArray = jsonObject.getJSONArray("Color");

            cnt = java.lang.Integer.parseInt(jsonArray.get(i).toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cnt;
    }
}
