package de.app.prime.palo;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import de.app.prime.palo.GetFromDatabase.GetStatusFromDB;
import de.app.prime.palo.IconsChecker.CheckI10;
import de.app.prime.palo.IconsChecker.CheckI11;
import de.app.prime.palo.IconsChecker.CheckI121314;
import de.app.prime.palo.IconsChecker.CheckI1516;
import de.app.prime.palo.IconsChecker.CheckI17;
import de.app.prime.palo.IconsChecker.CheckI19;
import de.app.prime.palo.IconsChecker.CheckI20;
import de.app.prime.palo.IconsChecker.CheckI4;
import de.app.prime.palo.IconsChecker.CheckI5;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    GoogleMap map;
    public LatLng currLocation;
    User user;
    MarkerOptions markerOptions;
    String status;
    String studyCourse;
    Bundle bundle;
    Bundle bundleColor;
    Bundle bundleCurrLoc;
    Float markerColorFloat;
    ArrayList<String> args = new ArrayList<>();
    MarkerOptions markerOptions1;
    String filename = "user_status";


    FloatingActionButton btnChangeInMap;
    EditText etStatusInMap;
    ImageView imageView;
    String android_id;

    Bitmap b;
    Bitmap smallMarker;
    OnClickSendToDB onClickSendToDB = new OnClickSendToDB();

    int cnt_status_map;

    public MapFragment() {
        // Required empty public constructor
    }

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            String blockCharacterSet = "\\";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @SuppressLint("HardwareIds")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        if(view == null){
            MapFragment fragment = null;
            if (getFragmentManager() != null) {
                fragment = (MapFragment)
                        getFragmentManager().findFragmentById(R.id.map);
            }

            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .detach(fragment)
                        .attach(fragment)
                        .commit();
            }
        }

        etStatusInMap = (EditText) view.findViewById(R.id.etStatusInMap);
        imageView = (ImageView) view.findViewById(R.id.message);

        CheckI19 checkI19 = new CheckI19();
        checkI19.check19();

        CheckI20 checkI20 = new CheckI20();
        checkI20.check20();

        etStatusInMap.setFilters(new InputFilter[] { filter });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                onClickSendToDB.sendBtnClick(android_id, "2");

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.isMessageThere3();



                }catch(Exception e){

                    e.getStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Stacktrace");
                    builder.setMessage(Arrays.toString(e.getStackTrace()));
                    builder.show();
                }

            }
        });
        TelephonyManager tManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }

        if (tManager != null) {
            android_id = tManager.getDeviceId();
        }

        // Receive status from database.
        GetStatusFromDB getStatusFromDB = new GetStatusFromDB();
        getStatusFromDB.getStatus(android_id, this, etStatusInMap);
        //getStatusFromDB.getStatusViaContext(android_id, MapFragment.this.getActivity().getApplicationContext(), etStatusInMap);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.isMessage(this);

        // Get the data from ProfileFragment.java here
        bundle = getArguments();

        if (bundle != null) {
            System.out.println("THE BUNDLE: " + bundle);
            status = bundle.getString("status");
            studyCourse = bundle.getString("study course");
            if (bundle.getStringArrayList("args") != null) {
                args = bundle.getStringArrayList("args");
                System.out.println(args);

                currLocation = new LatLng(Double.parseDouble(args.get(0)), Double.parseDouble(args.get(1)));

            }
        }

        // Get the data from SettingsFragment.java here
        bundleColor = getArguments();

        if ((((bundleColor != null
                && bundleColor.getStringArrayList("args") == null)
                && bundleColor.getString("status") == null)
                && bundleColor.getString("study course") == null)) {
            markerColorFloat = bundleColor.getFloat("markerColor");
        } else {
            // default marker color
            markerColorFloat = BitmapDescriptorFactory.HUE_RED;
        }

        bundleCurrLoc = getArguments();
        if (bundleCurrLoc.getDoubleArray("currLoc") != null) {

            double[] latLng = bundleCurrLoc.getDoubleArray("currLoc");
            if (latLng != null) {
                currLocation = new LatLng(latLng[0], latLng[1]);
            }
        }

        btnChangeInMap = (FloatingActionButton) view.findViewById(R.id.btnChangeInMap);
        btnChangeInMap.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HardwareIds")
            @Override
            public void onClick(View view) {

                // check if status is empty
                onClickSendToDB.sendBtnClick(android_id, "3");
                if (etStatusInMap.getText().toString().isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.alert_empty_status_title);
                    builder.setMessage(R.string.alert_empty_status_message);
                    builder.show();
                }

                else {

                    status = String.valueOf(etStatusInMap.getText());
                    cnt_status_map += 1;

                    InputMethodManager inputMethodManager = (InputMethodManager) MyApplicationContext.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(btnChangeInMap.getWindowToken(), 0);
                    }

                    TelephonyManager tManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(MyApplicationContext.getAppContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }

                    if (tManager != null) {
                        android_id = tManager.getDeviceId();
                    }

                    CheckI4 checkI4 = new CheckI4();
                    checkI4.check4();




                    sendStatusToDB statusToDB = new sendStatusToDB();

                    //---- add Points +1 ----

                    PunkteJSON punkteJSON = new PunkteJSON();
                    int points = punkteJSON.getPoints();
                    punkteJSON.setPoints(points + 1);

                    //-----------------------
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yy");
                    Date date = new Date();

                    CheckI5 checkI5 = new CheckI5();
                    checkI5.check5(status, "map",date);

                    CheckI10 checkI10 = new CheckI10();
                    checkI10.check10();


                    CheckI11 checkI11 = new CheckI11();
                    checkI11.check11(date);


                    CheckI121314 checkI121314 = new CheckI121314();
                    checkI121314.check121314(currLocation);

                    CheckI1516 checkI1516 = new CheckI1516();
                    checkI1516.check1516(date);

                    String time = dateFormat.format(date);
                    double latitude = currLocation.latitude;
                    double longitude = currLocation.longitude;
                    MarkerColorJSON markerColorJSON = new MarkerColorJSON();
                    int marker = markerColorJSON.getActColor();
                    statusToDB.sendStatus(status, latitude, longitude, time, android_id, marker);
                    OldStatus oldList = new OldStatus();
                    oldList.addNewEntry(status);

                    // call method to update map after setting a new status
                    updateMap();
                }
            }
        });

        if (currLocation != null) {
            markerOptions = new MarkerOptions()
                    .position(currLocation);
        }

        user = new User();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final FloatingActionButton fabUpdate = (FloatingActionButton) view.findViewById(R.id.fabUpdate);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMap();
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        System.out.println("CURRENT LOCATION: " + currLocation);

        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);

        if (currLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 13));

            if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            map.setMyLocationEnabled(true);
        }

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        if (((args == null || args.size() == 0) && bundle == null) && bundleCurrLoc == null ){
            System.out.println("Bundle args ist null");
        }
        else {
            //only if Bundle is an ArrayList

            //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.element1hdpi);

            int height = 125;
            int width = 170;

            for (int i = 2; i < args.size(); i = i + 7) {
                int lvl = Integer.parseInt(args.get(i+6));
            if(args.get(i+5).equals("1")) {
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker1);

                b = bitmapDraw.getBitmap();

                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }


            }
            else if(args.get(i+5).equals("2")) {
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker2);
                b = bitmapDraw.getBitmap();

                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }



            }
            else if(args.get(i+5).equals("3")) {
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker3);
                b = bitmapDraw.getBitmap();

                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }



            }
            else if(args.get(i+5).equals("4")) {
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker4);


                b = bitmapDraw.getBitmap();
                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }


            }
                else if(args.get(i+5).equals("5")) {
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker5);

                b = bitmapDraw.getBitmap();

                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }



                }
                else if(args.get(i+5).equals("6")) {
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker6);


                b = bitmapDraw.getBitmap();
                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }



                }
                else if(args.get(i+5).equals("7")) {
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker7);

                b = bitmapDraw.getBitmap();

                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }



                }
                else if(args.get(i+5).equals("8")) {
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker8);


                b = bitmapDraw.getBitmap();
                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }



                }
                else if(args.get(i+5).equals("9")) {
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker9);


                b = bitmapDraw.getBitmap();
                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }



                }
                else if(args.get(i+5).equals("10")) {
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker10);

                b = bitmapDraw.getBitmap();

                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }



                }else{
                BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.element2mdpi);


                b = bitmapDraw.getBitmap();
                if(lvl < 3){

                    smallMarker = Bitmap.createScaledBitmap(b, 155, 110, false);
                }
                if(lvl > 2 && lvl < 6){
                    smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                }
                if(lvl > 5 && lvl < 8){
                    smallMarker = Bitmap.createScaledBitmap(b, 195, 140, false);
                }
                if(lvl > 7){
                    smallMarker = Bitmap.createScaledBitmap(b, 210, 165, false);
                }



                }


                String snippet = args.get(i);
                markerOptions1 = new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(args.get(i+1)), Double.parseDouble(args.get(i + 2))))
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .title(args.get(i+4) + " | " + args.get(i+3) + " | Lvl: " + args.get(i+6))
                        .snippet(snippet)
                        .anchor(1,1);

                map.addMarker(markerOptions1);

            }
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {


                    CheckI17 checkI17 = new CheckI17();
                    checkI17.check17();

                    String title = arg0.getTitle();

                    String[] titleArray = title.split("|");
                    StringBuilder name = new StringBuilder();
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    Bundle bundle = new Bundle();

                    for (String aTitleArray : titleArray) {
                        System.out.println("Char: " + aTitleArray);
                        if (aTitleArray.equals("|")) {
                            break;
                        } else {
                            name.append(aTitleArray);
                        }
                    }
                    // the last whitespace in name is unnecessary, so it has to be removed.
                    JSONChatDB jsonChatDB = new JSONChatDB();
                    jsonChatDB.addNewChatUser(name.toString());
                    user.setName(name.toString());

                    //---- add Points +1 ----

                    PunkteJSON punkteJSON = new PunkteJSON();
                    int points = punkteJSON.getPoints();
                    punkteJSON.setPoints(points + 1);

                    //-----------------------

                    bundle.putString("name", name.toString());
                    intent.putExtra("name", name.toString());
                    startActivity(intent);
                }
            });
        }
        //------------------------------- 

        Toast.makeText(getContext(), getString(R.string.current_location) + " " + currLocation, Toast.LENGTH_LONG).show();

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = map.setMapStyle(

                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style1));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

    }

    public void updateMap() {
        CurrLocUpdate upFragment = new CurrLocUpdate();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left)
                .replace(R.id.relativelayout_for_fragments,
                        upFragment,
                        upFragment.getTag()
                ).commit();


    }

    public void setImageViewVisibility(Boolean isImageVisible) {

        if (isImageVisible) {
            try {
                imageView.setBackground((getResources().getDrawable(R.drawable.message)));
            }catch(Exception e){
                Toast.makeText(getContext(), "Das Postfachsymbol kann im Moment nicht angezeigt werden. Einfach kurz neu laden :)", Toast.LENGTH_SHORT).show();

            }
        }
        else {
            try {
                imageView.setBackground((getResources().getDrawable(R.drawable.nomessage)));
            }catch(Exception e){
                Toast.makeText(getContext(), "Das Postfachsymbol kann im Moment nicht angezeigt werden. Einfach kurz neu laden :)", Toast.LENGTH_SHORT).show();
                }
         }

    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(getContext(), "Current Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();

        this.currLocation = new LatLng(location.getLatitude(), location.getLongitude());

        // set individual Controls and Gestures for the Google Map
        GoogleMapOptions options = new GoogleMapOptions();
        options.zoomControlsEnabled(false);
        options.compassEnabled(true);
        options.mapToolbarEnabled(false);
    }



    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


}