package de.app.prime.palo;

import android.content.Context;
import android.util.Log;

import de.app.prime.palo.IconsChecker.CompareIconsListWithDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class IconListJSON {
    private static String fileName = "icons.json";

    private static void createNewDBDeleteOld(String nameJSON) {
        try {
            System.out.println("New created DB Icons Points: " + nameJSON);
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

    public void setIcon(int i){

        String old = getData(MyApplicationContext.getAppContext());
        System.out.println(old);
        try {
            if(old == null){
                createNewDBDeleteOld("{ \"Icons\" : ['0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0']}");
                old = getData(MyApplicationContext.getAppContext());
            }
            JSONObject jsonObject = new JSONObject(old);
            JSONArray jsonArray = jsonObject.getJSONArray("Icons");
            int cnt = java.lang.Integer.parseInt(jsonArray.get(i-1).toString());

            cnt = cnt + 1;
            jsonArray.put(i-1, cnt); // -1 because we have to insert it at the index
            createNewDBDeleteOld("{ \"Icons\" : "+jsonArray.toString()+"}");

        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    public void resetIcon(int index){
        String old = getData(MyApplicationContext.getAppContext());
        System.out.println(old);
        try {
            if(old == null){
                createNewDBDeleteOld("{ \"Icons\" : ['0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0']}");
                old = getData(MyApplicationContext.getAppContext());
            }
            JSONObject jsonObject = new JSONObject(old);
            JSONArray jsonArray = jsonObject.getJSONArray("Icons");

            jsonArray.put(index, "0");
            createNewDBDeleteOld("{ \"Icons\" : "+jsonArray.toString()+"}");

        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    public int getIcon(int index){
        String old = getData(MyApplicationContext.getAppContext());
        int cnt = 0;
        CompareIconsListWithDB compareIconsListWithDB = new CompareIconsListWithDB();

        try {
            if(old == null){
                createNewDBDeleteOld("{ \"Icons\" : ['0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0']}");
                old = getData(MyApplicationContext.getAppContext());
            }
            JSONObject jsonObject =  new JSONObject(old);

            JSONArray jsonArray = jsonObject.getJSONArray("Icons");

            cnt = java.lang.Integer.parseInt(jsonArray.get(index).toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cnt;
    }
}
