package com.example.lorcan.palo;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SendIconToDB {

    private static final String URL = "http://palo.square7.ch/setNewIconGami.php";
    private String iconNum;
    private String android_id;

    public SendIconToDB() {

    }

    /*
    ICONS SETTING

    INDEX-SET = KIND OF ICON

    1) Patient Zero - Register at Palo during the first test phase
    2) Pic Yourself - Upload a profile photo
    3) Profile Poster - Post your first status from the profile
    4) Map Poster - Post your first status from the map
    5) Sophisticated Poster - Post a status from your profile and directly from the map
    6) Old But Gold - Post an old status again
    7) Color Changer - Change the marker color
    8) Color Picker - Post a status in 5 different colors
    9) Sophisticated in color - Post a status in all 10 colors
    10) Night Owl - Post a status after 3am
    11) Quick Poster - Post 10 statuses in one day
    12) Traveller - Post two statuses in a row at least 20km apart
    13) Travel Guru - Post two statuses in a row at least 100km apart
    14) Travel God - Post two statuses in a row at least 1000km apart
    15) Perfect Week - Post at least one status per day for one week
    16) Perfect Month - Post at least one status per day for one month
    17) Marker Clicker - Click 20 markers from other Palo users
    18) Profile Viewer - Open the profile of five other Palo-Users
    19) Halfway Done - Reach Level 5
    20) Finisher - Reach Level 10

     */


    public void sendIcon(final String iconNum, final String android_id) {

        this.iconNum = iconNum;
        this.android_id = android_id;
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplicationContext.getAppContext());
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                PunkteJSON punkteJSON = new PunkteJSON();
                int points = punkteJSON.getPoints();

                System.out.println("Response setIcon: " + response);

                if(iconNum == "1") {
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_1, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "2") {
                    punkteJSON.setPoints(points + 2);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_2, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "3") {
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_3, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "4") {
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_4, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "5") {
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_5, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "6") {
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_6, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "7") {
                    punkteJSON.setPoints(points + 2);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_7, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "8") {
                    punkteJSON.setPoints(points + 5);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_8, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "9") {
                    punkteJSON.setPoints(points + 10);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_9, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "10") {//
                    punkteJSON.setPoints(points + 4);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_10, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "11") {
                    punkteJSON.setPoints(points + 5);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_11, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "12") {
                    punkteJSON.setPoints(points + 1);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_12, Toast.LENGTH_LONG).show();
                }
                if(iconNum == "13") {
                    punkteJSON.setPoints(points + 5);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_13, Toast.LENGTH_LONG).show();
                }

                if(iconNum == "14") {
                    punkteJSON.setPoints(points + 10);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_14, Toast.LENGTH_LONG).show();
                }

                if(iconNum == "15") {
                    punkteJSON.setPoints(points + 5);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_15, Toast.LENGTH_LONG).show();
                }

                if(iconNum == "16") {
                    punkteJSON.setPoints(points + 10);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_16, Toast.LENGTH_LONG).show();
                }

                if(iconNum == "17") {
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_17, Toast.LENGTH_LONG).show();
                }

                if(iconNum == "18") {
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_18, Toast.LENGTH_LONG).show();
                }

                if(iconNum == "19") {
                    punkteJSON.setPoints(points + 2);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_19, Toast.LENGTH_LONG).show();
                }

                if (iconNum == "20") {
                    punkteJSON.setPoints(points + 5);
                    Toast.makeText(MyApplicationContext.getAppContext(), R.string.achievement_20, Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            // set of parameters in a HashMap, which will be send to the php file (server side)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("iconNum", iconNum);
                hashMap.put("android_id", android_id);
                System.out.println("WHAT IS SEND FROM THE SetIcon: " + hashMap);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }
}
