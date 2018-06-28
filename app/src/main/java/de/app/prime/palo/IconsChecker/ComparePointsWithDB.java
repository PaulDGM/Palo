package de.app.prime.palo.IconsChecker;

import de.app.prime.palo.GetFromDatabase.GetPointsDB;
import de.app.prime.palo.PunkteJSON;
import de.app.prime.palo.UsernameJSON;

public class ComparePointsWithDB {
    public ComparePointsWithDB(){

    }
    public void compare(){
        UsernameJSON usernameJSON = new UsernameJSON();
        String name = usernameJSON.getUserName();
        GetPointsDB getPointsDB = new GetPointsDB();
        try{
            getPointsDB.getPoints2(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setPoints(int points){
        PunkteJSON punkteJSON = new PunkteJSON();
        punkteJSON.setPoints(points);
    }
}
