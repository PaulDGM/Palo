

package de.app.prime.palo.GetFromDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import de.app.prime.palo.MyApplicationContext;
import de.app.prime.palo.ProfilActivity;

import java.util.HashMap;
import java.util.Map;

public class GetPointsDB {

    private static final String STR_URL = "http://palo.square7.ch/getPointsGami.php";
    public RequestQueue requestQueue;
    public StringRequest request;
    public ResponseTask1 responseTask1;
    public int responseInt;
    public String name;
    public GetPointsDB() {
    }

    public void getPoints(ProfilActivity profilActivity, String name){
        this.name = name;
        responseTask1 = new ResponseTask1(profilActivity);
        responseTask1.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class ResponseTask1 extends AsyncTask<Void, Void, Void> {
        ProfilActivity profilActivity;

        public ResponseTask1(ProfilActivity profilActivity) {
            this.profilActivity = profilActivity;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            requestQueue = Volley.newRequestQueue(MyApplicationContext.getAppContext());
            request = new StringRequest(Request.Method.POST, STR_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    handleResponse(response, profilActivity);
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
    public void handleResponse(String response, ProfilActivity profilActivity){
        profilActivity.setPointsAndWidth(response);
    }
}


