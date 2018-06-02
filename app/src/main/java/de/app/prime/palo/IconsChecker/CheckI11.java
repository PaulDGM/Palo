package de.app.prime.palo.IconsChecker;

import android.annotation.SuppressLint;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckI11 {
    public DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    public DateFormat timeFormat = new SimpleDateFormat("ss");
    public Date date = new Date();

    private static String fileName = "10StatusPosts.json";

    private static void createNewDBDeleteOld(String nameJSON) {
        try {
            System.out.println("New created DB from StatusCounter: " + nameJSON);
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

    public void check11(Date datePost) {

        String old = getData(MyApplicationContext.getAppContext());
        System.out.println(old);
        try {
            if (old == null) {
                createNewDBDeleteOld("{ \"StatusCount\" : ['0', '0']}");
                old = getData(MyApplicationContext.getAppContext());
            }

            JSONObject jsonObject = new JSONObject(old);
            JSONArray jsonArray = jsonObject.getJSONArray("StatusCount");
            String lastDate = "";
            try {
                lastDate = jsonArray.get(1).toString();

                jsonArray.put(1, dateFormat.format(datePost));
            }catch(JSONException je){
                createNewDBDeleteOld("{ \"StatusCount\" : ['0', '0']}");
                old = getData(MyApplicationContext.getAppContext());
            }
            IconListJSON iconListJSON = new IconListJSON();
            int isIcon = iconListJSON.getIcon(10);
            System.out.println("ICON OF STATUSCOUNT: " + isIcon);
            if (isIcon == 0) {
                TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                System.out.println(dateFormat.format(datePost) +" != "+ lastDate);

                if(dateFormat.format(datePost).equals(lastDate)){
                    int currentCnt = Integer.parseInt(jsonArray.get(0).toString());
                    System.out.println("CurrentCount from Statuscounter: " + currentCnt);
                    currentCnt = currentCnt +1;
                    jsonArray.put(0, String.valueOf(currentCnt));
                    createNewDBDeleteOld("{ \"StatusCount\" : "+jsonArray.toString()+"}");
                }else{
                    jsonArray.put(0, "0");
                    jsonArray.put(1, dateFormat.format(datePost));
                    createNewDBDeleteOld("{ \"StatusCount\" : "+jsonArray.toString()+"}");
                }


                final String android_id = tManager.getDeviceId();
                if (Integer.parseInt(jsonArray.get(0).toString()) == 10) {
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
