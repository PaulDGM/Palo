package com.example.lorcan.palo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by paul on 01.07.17.
 */

public class getLocFromDB {


    String result = null;
    InputStream is = null;
    StringBuilder sb;
    HashMap<String, LatLng> hashMapOtherUsers = new HashMap<>();

    private RequestQueue requestQueue;
    private static final String URL = "http://palo.square7.ch/getLocation.php";
    private StringRequest request;
    private Context context;

    public getLocFromDB(Context context){
        this.context = context;
    }

    public void getLocation(){
        this.requestQueue = Volley.newRequestQueue(context);
        this.request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.names().get(0).equals("success")){
                        Toast.makeText(getApplicationContext(),"SUCCESS "+jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Error" +jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                System.out.println("Antwort von PHP File: " + response);

                parseResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){

            // set of parameters in a hashmap, which will be send to the php file (server side)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String, String>();

                String zugang = "yep";
                hashMap.put("zugang",zugang);

                System.out.println("DAS WAS GESENDET WIRD: " + hashMap);

                return hashMap;
            }
        };

        requestQueue.add(request);

    }
    private void parseResponse(String response){
        String[] responseSplit = response.split("#"); //split at '#' --> PHP response looks like this: #email@test.com, 53.12, 12.123#email2@test.com, 42.123, 12.124 and so on
        for (int i = 1; i< responseSplit.length; i++){
            String[] t = responseSplit[i].split(","); //split the splitted at ","
            hashMapOtherUsers.put(t[0], new LatLng(Double.parseDouble(t[1]), Double.parseDouble(t[2])));

        }
        System.out.println("HASHMAP OUT: " + hashMapOtherUsers);
    }

}