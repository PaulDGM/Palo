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

    INDEX SET KIND OF ICON

    1 = Patient Zero - Registriere dich bei Palo während der ersten Testphase
    2 = Pic Yourself! - Lade ein Profilfoto hoch
    3 = Profile Poster - Poste deinen ersten Status aus dem Profil
    4 = Poster - Poste deinen ersten Status aus der Karte
    5 = Sophisticated Poster - Poste einen Status aus deinem Profil und direkt aus der Karte
    6 = Old but Gold = Poste einen alten Status erneut
    7 = Color Changer - Ändere die Marker Farbe
    8 = Color Picker - Poste einen Status in allen 5 Farben
    9 = Sophisticated in color – Poste einen Status in allen (10) Farben
    10 = Nightowl - Poste einen Status nach 3 Uhr nachts bis 5 Uhr morgens
    11 = Poste 10 Status an einem Tag
    12 = Poste zwei Status hintereinander, die mindestens 20km auseinander liegen
    13 = Poste zwei Status hintereinander, die mindestens 100km auseinander liegen
    14 = Poste zwei Status hintereinander, die mindestens 1000km auseinander liegen
    15 = Perfekt week - Poste eine Woche lang mindestens einen Status pro Tag
    16 = Perfect month - Poste einen Monat lang mindestens einen Status pro Tag
    17 = Marker clicker - Klicke zehn Marker von anderen Palo Usern
    18 = Profile Viewer - Öffne das Profil von fünf anderen Palo-Usern
    19 = Halfway done - Erreiche Lvl 5
    20 = Finisher - Erreiche Lvl 10

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
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Patient Zero - Registriere dich bei Palo während der ersten Testphase", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "2") {
                    punkteJSON.setPoints(points + 2);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Pic Yourself! - Lade ein Profilfoto hoch", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "3") {
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Profile Poster - Poste deinen ersten Status aus dem Profil", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "4") {
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Poster - Poste deinen ersten Status aus der Karte", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "5") {
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Sophisticated Poster - Poste einen Status aus deinem Profil und direkt aus der Karte", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "6") {
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Old but Gold - Poste einen alten Status erneut", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "7") {
                    punkteJSON.setPoints(points + 2);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Color Changer - Ändere die Marker Farbe", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "8") {
                    punkteJSON.setPoints(points + 5);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Color Picker - Poste einen Status in 5 Farben", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "9") {
                    punkteJSON.setPoints(points + 10);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Sophisticated in color – Poste einen Status in allen (10) Farben", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "10") {//
                    punkteJSON.setPoints(points + 4);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Nightowl - Poste einen Status nach 3 Uhr nachts bis 5 Uhr morgens", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "11") {
                    punkteJSON.setPoints(points + 5);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Poste 10 Status an einem Tag", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "12") {
                    punkteJSON.setPoints(points + 1);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Poste zwei Status hintereinander, die mindestens 20km auseinander liegen", Toast.LENGTH_LONG).show();
                }
                if(iconNum == "13") {
                    punkteJSON.setPoints(points + 5);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Poste zwei Status hintereinander, die mindestens 100km auseinander liegen", Toast.LENGTH_LONG).show();
                }

                if(iconNum == "14") {
                    punkteJSON.setPoints(points + 10);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Poste zwei Status hintereinander, die mindestens 1000km auseinander liegen", Toast.LENGTH_LONG).show();
                }

                if(iconNum == "15") {
                    punkteJSON.setPoints(points + 5);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Perfekt week - Poste eine Woche lang mindestens einen Status pro Tag", Toast.LENGTH_LONG).show();
                }

                if(iconNum == "16") {
                    punkteJSON.setPoints(points + 10);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Perfect month - Poste einen Monat lang mindestens einen Status pro Tag", Toast.LENGTH_LONG).show();
                }

                if(iconNum == "17") {
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Marker clicker - Klicke zwanzig Marker von anderen Palo Usern", Toast.LENGTH_LONG).show();
                }

                if(iconNum == "18") {
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Profile Viewer - Öffne das Profil von fünf anderen Palo-Usern", Toast.LENGTH_LONG).show();
                }

                if(iconNum == "19") {
                    punkteJSON.setPoints(points + 2);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Halfway done - Erreiche Lvl 5", Toast.LENGTH_LONG).show();
                }

                if (iconNum == "20") {
                    punkteJSON.setPoints(points + 5);
                    Toast.makeText(MyApplicationContext.getAppContext(), "Neues Achievement: Finisher - Erreiche Lvl 10", Toast.LENGTH_LONG).show();
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
