package de.app.prime.palo.GetFromDatabase;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import de.app.prime.palo.Fragments.ProfileFragment;
import de.app.prime.palo.MapFragment;
import de.app.prime.palo.MyApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class GetStatusFromDB {

    private static final String strUrl = "http://palo.square7.ch/getOneStatusGami.php";
    private String android_id;
    private ProfileFragment profileFragment;
    private MapFragment mapFragment;
    private EditText etStatus;
    private EditText etStatusInMap;
    public GetStatusAsyncTask getStatusAsyncTask;

    public void getStatus(final String android_id, ProfileFragment profileFragment, EditText etStatus) {
        this.android_id = android_id;
        this.profileFragment = profileFragment;
        this.etStatus = etStatus;
        getStatusAsyncTask = new GetStatusAsyncTask();
        getStatusAsyncTask.execute();

    }

    public void getStatus(final String android_id, MapFragment mapFragment, EditText etStatusInMap) {
        this.android_id = android_id;
        this.mapFragment = mapFragment;
        this.etStatusInMap = etStatusInMap;
        new GetStatusAsyncTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class GetStatusAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            if (profileFragment != null) {
                etStatus.setEnabled(false);
            }

            else if (mapFragment != null) {
                etStatusInMap.setEnabled(false);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            RequestQueue requestQueue = Volley.newRequestQueue(MyApplicationContext.getAppContext());
            StringRequest request = new StringRequest(Request.Method.POST, strUrl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    System.out.println("This is what the PHP File responses: " + response);

                    if (profileFragment != null) {
                        handleResponse(response);
                        onPostExecute(null);
                    }

                    else if (mapFragment != null) {
                        handleResponseMap(response);
                        onPostExecute(null);
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
                    hashMap.put("android_id", android_id);
                    return hashMap;
                }
            };
            requestQueue.add(request);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (profileFragment != null) {
                etStatus.setEnabled(true);
            }

            else if (mapFragment != null) {
                etStatusInMap.setEnabled(true);
            }
            this.cancel(true);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            System.out.println("GET STATUS FROM DB IS CANCELLED");
        }
    }

    private void handleResponse(String response){
        etStatus.setText(response);
        etStatus.setSelection(response.length());
    }

    private void handleResponseMap(String response){
        etStatusInMap.setText(response);
        etStatusInMap.setSelection(response.length());
    }
}
