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

import de.app.prime.palo.IconListJSON;

public class CheckI121314 {

        private static String fileName = "checkIcon11.json";

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

        public void check121314(LatLng latLngNew){
            String latNew = String.valueOf(latLngNew.latitude);
            String lngNew = String.valueOf(latLngNew.longitude);
            System.out.println(">>>>>>LATNEW: "+latNew);
            String old = getData(MyApplicationContext.getAppContext());
            System.out.println(old);
            try {
                if(old == null){
                    createNewDBDeleteOld("{ \"LastSpot\" : ['0', '0']}"); //[Lat, Lng]
                    old = getData(MyApplicationContext.getAppContext());
                }
                JSONObject jsonObject = new JSONObject(old);
                JSONArray jsonArray = jsonObject.getJSONArray("LastSpot");
                Double latOld = Double.parseDouble(jsonArray.get(0).toString());
                Double lngOld = Double.parseDouble(jsonArray.get(1).toString());

                LatLng latLngOld = new LatLng(latOld, lngOld);

                jsonArray.remove(0);
                jsonArray.remove(1);
                jsonArray.put(0, latNew);
                jsonArray.put(1, lngNew);
                if(latOld != 0 && lngOld != 0) {
                TelephonyManager tManager = (TelephonyManager) MyApplicationContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                final String android_id = tManager.getDeviceId();
                IconListJSON iconListJSON = new IconListJSON();
                int isIcon11 = iconListJSON.getIcon(10);
                int isIcon12 = iconListJSON.getIcon(11);
                int isIcon13 = iconListJSON.getIcon(12);

                    float distance = checkForDistance(latLngOld, latLngNew);
                    System.out.println(">>>>>Distance: " +distance);
                    if (distance >= 20000 && isIcon11 == 0) {
                        System.out.println("distance größer als 20k");
                        SendIconToDB sendIconToDB = new SendIconToDB();
                        sendIconToDB.sendIcon("11", android_id);
                        iconListJSON.setIcon(11);
                    }else{
                        System.out.println(distance +"<<<<<>>>>>"+ isIcon11);
                    }
                    if (distance >= 100000 && isIcon12 == 0) {
                        System.out.println("distance größer als 100k");
                        SendIconToDB sendIconToDB = new SendIconToDB();
                        sendIconToDB.sendIcon("12", android_id);
                        iconListJSON.setIcon(12);
                    }
                    if (distance >= 1000000 && isIcon13 == 0) {
                        System.out.println("distance größer als 1000k");
                        SendIconToDB sendIconToDB = new SendIconToDB();
                        sendIconToDB.sendIcon("13", android_id);
                        iconListJSON.setIcon(13);
                    }

                }
                createNewDBDeleteOld("{ \"LastSpot\" : " + jsonArray.toString() + "}");
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        public int getCheck11(int i){
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

        public float checkForDistance(LatLng latLngOld, LatLng latLngNew) {
            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(latLngNew.latitude-latLngOld.latitude);
            double dLng = Math.toRadians(latLngNew.longitude-latLngOld.longitude);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(latLngOld.latitude)) * Math.cos(Math.toRadians(latLngNew.latitude)) *
                            Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            float dist = (float) (earthRadius * c);

            return dist;
        }
    }
