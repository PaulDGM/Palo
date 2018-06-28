

package de.app.prime.palo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import de.app.prime.palo.GetFromDatabase.GetPointsDB;
import de.app.prime.palo.GetFromDatabase.GetProfilInfoFromDB;

import java.io.Serializable;
import java.util.ArrayList;

import de.app.prime.palo.GetFromDatabase.GetPointsDB;
import de.app.prime.palo.GetFromDatabase.GetProfilInfoFromDB;
import de.app.prime.palo.IconsChecker.ComparePointsWithDB;
import pl.droidsonroids.gif.GifTextView;

public class ProfilActivity extends AppCompatActivity {

    String name;
    public int points;
    public String lvl;

    Point size = new Point();
    int width = size.x;
    int height = size.y;
    RelativeLayout progressBarBox;
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        setContentView(R.layout.activity_profil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ComparePointsWithDB comparePointsWithDB = new ComparePointsWithDB();
        comparePointsWithDB.compare();

        Serializable k = getIntent().getSerializableExtra("name");
        name = k.toString();
        if(name == null || name.equals("null")){
            UsernameJSON usernameJSON = new UsernameJSON();
            name = usernameJSON.getUserName();
        }
        ProfilActivity.this.setTitle("Profil von " + name);

        GetProfilInfoFromDB getProfilInfoFromDB = new GetProfilInfoFromDB();
        getProfilInfoFromDB.getInfo(ProfilActivity.this, name);

        progressBarBox = new RelativeLayout(this);
        txtView = new TextView(this);

        display.getSize(size);
        width = size.x;
        height = size.y;

    }

    public void setInfoToScreen(ArrayList<String> list){

        RelativeLayout rel = (RelativeLayout) findViewById(R.id.profilLayout);
        GifTextView gif = (GifTextView) findViewById(R.id.imageView2);
        rel.removeView(gif);

        RelativeLayout.LayoutParams layoutImageView = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageView ivImage = new ImageView(this);
        layoutImageView.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ivImage.setLayoutParams(layoutImageView);
        String image = list.get(0);

        setPointsAndWidth();


        if(image != null && image.length() > 0) {


            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivImage.setRotation(90);
            ivImage.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 400, 400, false));
            ivImage.setBackground(getResources().getDrawable(R.drawable.layout_bg));
        }else{
            ivImage.setImageDrawable(getResources().getDrawable(R.drawable.trophy));
            ivImage.setBackgroundColor(getResources().getColor(R.color.hhu_blue));
        }

        TextView statusTextView = new TextView(this);

        RelativeLayout.LayoutParams layoutTextView = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutTextView.setMargins(0, 600, 0, 0);
        layoutTextView.addRule(RelativeLayout.BELOW, ivImage.getId());
        layoutTextView.addRule(RelativeLayout.CENTER_HORIZONTAL);
        statusTextView.setLayoutParams(layoutTextView);
        statusTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        statusTextView.setWidth(600);
        statusTextView.setTextColor(Color.LTGRAY);
        String status = "\"" + list.get(1) + "\"";
        statusTextView.setText(status);


        //---ICON LIST---
        ScrollView scrollView = new ScrollView(this);

        RelativeLayout.LayoutParams iconListLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        iconListLayoutParams.setMargins(0, 700, 0, 0);
        iconListLayoutParams.addRule(RelativeLayout.BELOW, statusTextView.getId());
        iconListLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        scrollView.setLayoutParams(iconListLayoutParams);
        scrollView.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
        iconListLayoutParams.width = 780;
        iconListLayoutParams.height = 550;
        scrollView.setBackgroundColor(Color.LTGRAY);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);


        int cnt = 0;
        for(int i = 3; i < list.size(); i++){
            list.get(i).trim();
            if(list.get(i).equals("1")){
                cnt = cnt +1;
                ImageView iconIV = new ImageView(this);
                iconIV.setScaleX(0.50f);
                iconIV.setScaleY(0.50f);

                if(i == 3) {
                    iconIV.setBackgroundResource(R.drawable.achievement_starter);
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Patient Zero - Register at Palo during the first test phase");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 4) {
                    iconIV.setBackgroundResource(R.drawable.achievement_camera); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Pic Yourself - Upload a profile photo");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 5) {
                    iconIV.setBackgroundResource(R.drawable.achievement_profile_poster); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Profile Poster - Post your first status from the profile");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 6) {
                    iconIV.setBackgroundResource(R.drawable.achievement_map_poster); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Map Poster - Post your first status from the map");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 7) {
                    iconIV.setBackgroundResource(R.drawable.achievements_sophisticated_poster); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Sophisticated Poster - Post a status from your profile and directly from the mapb");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 8) {
                    iconIV.setBackgroundResource(R.drawable.achievement_old_but_gold); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Old But Gold - Post an old status again");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 9) {
                    iconIV.setBackgroundResource(R.drawable.achievement_change_marker_color); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Color Changer - Change the marker color");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 10) {
                    iconIV.setBackgroundResource(R.drawable.achievement_color_picker); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Color Picker - Post a status in 5 different colors");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 11) {
                    iconIV.setBackgroundResource(R.drawable.achievement_sophisticated_in_color); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Sophisticated in color - Post a status in all 10 colors");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 12) {
                    iconIV.setBackgroundResource(R.drawable.achievement_night_owl); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Night Owl - Post a status after 3am");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 13) {
                    iconIV.setBackgroundResource(R.drawable.achievement_quick_poster); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Quick Poster - Post 10 statuses in one day");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 14) {
                    iconIV.setBackgroundResource(R.drawable.achievement_20km); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Traveller - Post two statuses in a row at least 20km apart");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 15) {
                    iconIV.setBackgroundResource(R.drawable.achievement_100km); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Travel Guru - Post two statuses in a row at least 100km apart");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 16) {
                    iconIV.setBackgroundResource(R.drawable.achievement_1000km); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Travel God - Post two statuses in a row at least 1000km apart");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }

                if(i == 17) {
                    iconIV.setBackgroundResource(R.drawable.achievement_perfect_week); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Perfect Week - Post at least one status per day for one week");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }
                if(i == 18) {
                    iconIV.setBackgroundResource(R.drawable.achievement_perfect_month); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Perfect Month - Post at least one status per day for one month");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }
                if(i == 19) {
                    iconIV.setBackgroundResource(R.drawable.achievement_marker_klicker); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Marker Clicker - Click 20 markers from other Palo users");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }
                if(i == 20) {
                    iconIV.setBackgroundResource(R.drawable.achievement_profile_viewer); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Profile Viewer - Open the profile of five other Palo-Users");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }
                if(i == 21) {
                    iconIV.setBackgroundResource(R.drawable.achievement_halfway_done); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Halfway Done - Reach Level 5");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }
                if(i == 22) {
                    iconIV.setBackgroundResource(R.drawable.achievement_finisher); //"icon"+ i-1
                    iconIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            alert.setMessage("Finisher - Reach Level 10");
                            alert.setPositiveButton("Ok", null);
                            alert.show();
                        }
                    });//"icon"+ i-1
                }
                linearLayout1.addView(iconIV);
                if(cnt == 4){
                    cnt = 0;
                    linearLayout.addView(linearLayout1);
                    linearLayout1 = new LinearLayout(this);
                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                }
            }
        }
        if(cnt<4){
            cnt = 0;
            linearLayout.addView(linearLayout1);
            linearLayout1 = new LinearLayout(this);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        }






        txtView.setPadding(0,8, 0, 0);
        txtView.setTextColor(Color.WHITE);



        RelativeLayout.LayoutParams progressBarBoxLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 90);
        progressBarBox.setBackgroundColor(Color.LTGRAY);
        progressBarBoxLayout.setMargins(0, 450, 0, 0);
        progressBarBoxLayout.addRule(RelativeLayout.BELOW, ivImage.getId());
        progressBarBox.setPadding(5, 5, 5, 5);
        progressBarBox.setLayoutParams(progressBarBoxLayout);



        //----------------
        scrollView.addView(linearLayout);


        //---------------

        rel.addView(ivImage);
        rel.addView(statusTextView);
        rel.addView(scrollView);
        rel.addView(progressBarBox);


    }

    public void setPointsAndWidth(){
        PunkteJSON punkteJSON = new PunkteJSON();
        points = punkteJSON.getPoints();
        LevelPointsConverter levelPointsConverter = new LevelPointsConverter();
        lvl = levelPointsConverter.convertPointsToLevel(points);
        txtView.setText(String.valueOf("   Level: "+lvl+ " // Punkte: " + points));

        int x = 0;
        int y = 0;
        if(lvl == "1"){
            x = 1;
            y = 0;
        }
        if(lvl == "2"){
            //1-9
            x = 9;
            y = 0;
        }if(lvl == "3"){
            //10-24
            x = 15;
            y = 9;
        }if(lvl == "4"){
            //25-49
            x = 25;
            y = 24;
        }if(lvl == "5"){
            //50-99
            x = 50;
            y = 49;
        }if(lvl == "6"){
            //100-199
            x = 100;
            y = 99;
        }if(lvl == "7"){
            //200-499
            x = 200;
            y = 199;
        }if(lvl == "8"){
            //500-999
            x = 500;
            y = 499;
        }if(lvl == "9"){
            //1000-1999
            x = 1000;
            y = 999;
        }if(lvl == "10"){
            //1999
            x = 10000;
        }
        int progress = ((width-10)/x)*(points-y);
        System.out.println(width);
        RelativeLayout progressBar = new RelativeLayout(this);
        RelativeLayout.LayoutParams progressBarLayout = new RelativeLayout.LayoutParams(progress, 80);
        progressBar.setBackgroundColor(getResources().getColor(R.color.hhu_blue));
        progressBar.setLayoutParams(progressBarLayout);
        progressBarBox.addView(progressBar);
        progressBarBox.addView(txtView);

    }

}

