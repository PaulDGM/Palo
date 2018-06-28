

package de.app.prime.palo.GetFromDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.app.prime.palo.IconsChecker.ComparePointsWithDB;
import de.app.prime.palo.MyApplicationContext;
import de.app.prime.palo.ProfilActivity;
import de.app.prime.palo.PunkteJSON;
import de.app.prime.palo.User;
import de.app.prime.palo.UsernameJSON;

import java.util.HashMap;
import java.util.Map;

public class GetPointsDB {

    private static final String STR_URL = "http://palo.square7.ch/getPointsGami.php";
    public RequestQueue requestQueue;
    public StringRequest request;
    public ResponseTask1 responseTask1;
    public ResponseTask2 responseTask2;
    public int responseInt;
    public String name;
    public PunkteJSON punkteJSON;


    public GetPointsDB() {
    }

    public void getPoints(PunkteJSON punkteJSON, String name){
        this.name = name;
        if(name == null){
            GetUsernameFromDB getUsernameFromDB = new GetUsernameFromDB();
            getUsernameFromDB.getNamePunkteJSON(this, punkteJSON);
        }else {
            responseTask1 = new ResponseTask1(punkteJSON);
            responseTask1.execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ResponseTask1 extends AsyncTask<Void, Void, Void> {
        PunkteJSON punkteJSON;

        public ResponseTask1(PunkteJSON punkteJSON) {
            this.punkteJSON = punkteJSON;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            requestQueue = Volley.newRequestQueue(MyApplicationContext.getAppContext());
            request = new StringRequest(Request.Method.POST, STR_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if(name != null) {
                        handleResponse(response, punkteJSON);
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {

                // set of parameters in a hashmap, which will be send to the php file (server side)
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    String name1 = "";
                    if (name.substring(name.length() - 1).equals(" ")) {
                        name1 = name.substring(0, name.length() - 1);
                    } else {
                        name1 = name;
                    }
                    hashMap.put("name", name1);
                    return hashMap;
                }
            };

            requestQueue.add(request);
            return null;
        }
    }


    public void handleResponse(String response, PunkteJSON punkteJSON){
        System.out.println("RESPONSE:" + response.trim());
        System.out.println("NAME: " + this.name);

        punkteJSON.getPointsFromDB(Integer.parseInt(response.trim()));
    }
    //----------

    public void getPoints2(ComparePointsWithDB comparePointsWithDB){
        UsernameJSON usernameJSON = new UsernameJSON();
        this.name = usernameJSON.getUserName();
        System.out.println("NAME: " + this.name);
        responseTask2 = new ResponseTask2(comparePointsWithDB);
        responseTask2.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class ResponseTask2 extends AsyncTask<Void, Void, Void> {
        ComparePointsWithDB comparePointsWithDB;

        public ResponseTask2(ComparePointsWithDB comparePointsWithDB) {
            this.comparePointsWithDB = comparePointsWithDB;
        }
        @Override
        protected Void doInBackground(Void... voids) {

            requestQueue = Volley.newRequestQueue(MyApplicationContext.getAppContext());
            request = new StringRequest(Request.Method.POST, STR_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    handleResponse2(response, comparePointsWithDB);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {

                // set of parameters in a hashmap, which will be send to the php file (server side)
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> hashMap = new HashMap<>();
                    String name1 = "";
                    if(name.substring(name.length() - 1).equals(" ")){
                        name1 = name.substring(0, name.length() - 1);
                    }else{
                        name1 = name;
                    }
                    hashMap.put("name", name1);
                    return hashMap;
                }
            };

            requestQueue.add(request);
            return null;
        }
    }

    public void handleResponse2(String response, ComparePointsWithDB comparePointsWithDB){
        System.out.println("RESPONSE PUNKTE 2: " + response);
        //comparePointsWithDB.setPoints(Integer.parseInt(response));

    }
}


