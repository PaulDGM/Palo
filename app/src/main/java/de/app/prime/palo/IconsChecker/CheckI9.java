package de.app.prime.palo.IconsChecker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import de.app.prime.palo.IconListJSON;
import de.app.prime.palo.MyApplicationContext;
import de.app.prime.palo.SendIconToDB;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class CheckI9 {
    private static String fileName = "colorClicked.json";

    private static void createNewDBDeleteOld(String nameJSON) {
        try {
            System.out.println("New created DB 10 Markercolors: " + nameJSON);
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

    public void checkI9(int indexColor){
        String old = getData(MyApplicationContext.getAppContext());
        System.out.println(old);
        try {
            if(old == null){
                createNewDBDeleteOld("{ \"Color\" : ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0']}");
                old = getData(MyApplicationContext.getAppContext());
            }

            System.out.println("MARKERNUM COLOR IN CHECKI9: " + indexColor);
            IconListJSON iconListJSON = new IconListJSON();


            int isIcon = iconListJSON.getIcon(8);
            if(isIcon == 0) {
                JSONObject jsonObject = new JSONObject(old);
                JSONArray jsonArray = jsonObject.getJSONArray("Color");
                if(indexColor >= 0) {
                    jsonArray.put(indexColor, "1");
                    createNewDBDeleteOld("{ \"Color\" : " + jsonArray.toString() + "}");

                }
                TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                final String android_id = tManager.getDeviceId();
                String old1 = getData(MyApplicationContext.getAppContext());
                JSONObject jsonObject1 = new JSONObject(old1);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("Color");

                boolean bool = true;
                for(int i = 0 ; i < jsonArray1.length(); i++){
                    if(Integer.parseInt(jsonArray1.get(i).toString()) == 0){
                        bool = false;
                        i = jsonArray1.length();// save end of loop because i is set to high for next iteration
                    }
                }
                if(bool){
                    SendIconToDB sendIconToDB = new SendIconToDB();
                    sendIconToDB.sendIcon("9", android_id);
                    iconListJSON.setIcon(9);
                }
                createNewDBDeleteOld("{ \"Color\" : " + jsonArray.toString() + "}");
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public int getCheck9(int i){
        String old = getData(MyApplicationContext.getAppContext());
        int cnt = 0;
        try {
            JSONObject jsonObject =  new JSONObject(old);

            JSONArray jsonArray = jsonObject.getJSONArray("Clicks");

            cnt = java.lang.Integer.parseInt(jsonArray.get(i).toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cnt;
    }

}
