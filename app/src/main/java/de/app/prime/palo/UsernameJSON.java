package de.app.prime.palo;

import android.content.Context;
import android.util.Log;

import de.app.prime.palo.GetFromDatabase.GetUsernameFromDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import de.app.prime.palo.GetFromDatabase.GetUsernameFromDB;

public class UsernameJSON {
    private static String fileName = "name.json";

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

    public void setUserName(String username){

        String old = getData(MyApplicationContext.getAppContext());
        try {
            if(old == null){
                GetUsernameFromDB getUsernameFromDB = new GetUsernameFromDB();
                String name = getUsernameFromDB.getName();
                createNewDBDeleteOld("{ \"Name\" : ['"+name+"']}");
                old = getData(MyApplicationContext.getAppContext());
            }
            JSONObject jsonObject = new JSONObject(old);
            JSONArray jsonArray = jsonObject.getJSONArray("Name");

            jsonArray.put(0, username);
            createNewDBDeleteOld("{ \"Name\" : "+jsonArray.toString()+"}");

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public String getUserName(){
        String old = getData(MyApplicationContext.getAppContext());
        String username = "";
        try {
            if(old == null){
                GetUsernameFromDB getUsernameFromDB = new GetUsernameFromDB();
                username = getUsernameFromDB.getName();
                setUserName(username);
            }else{
                JSONObject jsonObject =  new JSONObject(old);
                JSONArray jsonArray = jsonObject.getJSONArray("Name");
                username = jsonArray.get(0).toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return username;
    }
}
