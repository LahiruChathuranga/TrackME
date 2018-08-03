package com.app.sarinda.trackme;

import android.app.AlertDialog;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private ImageView imageView;
    private TextView mModel;
    private TextView mNumber;
    private TextView mSpeed;
    String model;
    String number;
    private double latitude;
    private double longitude;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private String user=md5("user1@gmail.com");
    private String device=md5("device1");
    Handler handler=new Handler();
    LinkedList<LatLng> points;
    LatLng start=new LatLng(5.94851,80.53528);
    LatLng end =new LatLng(6.9895,81.0557);
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        //Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Init();
    }

    private void Init() {
        /**init image view*/
        imageView=findViewById(R.id.mMarker);
        /**database initialization*/
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference(user).child(device);
        mModel=findViewById(R.id.model);
        mNumber=findViewById(R.id.number);
        mSpeed=findViewById(R.id.speedno);
        model= (String) getIntent().getExtras().get("model");
        number=(String)getIntent().getExtras().get("number");
        alertDialog=new SpotsDialog(this);
        alertDialog.show();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng sl=new LatLng(7.8731,80.7718);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sl,8));
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.setBuildingsEnabled(false);
        mModel.setText(model);
        mNumber.setText(number);
        route();





        /**Get data from firebase*/
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Vehicle vehicledet= dataSnapshot.getValue(Vehicle.class);
                System.out.println(vehicledet.getLatitude());
                latitude=vehicledet.getLatitude();
                longitude=vehicledet.getLongitude();
                mSpeed.setText(vehicledet.getSpeed().toString());



                /**make marker on map*/

                LatLng point = new LatLng(latitude,longitude);
                if(mMap.getCameraPosition().zoom!=16) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16));

                }
                else
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(point));

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public void route(){
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start,end)
                .build();
        routing.execute();


    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
         points=new LinkedList<>(arrayList.get(i).getPoints());
         handler.postDelayed(thread,700);
         alertDialog.dismiss();



    }

    @Override
    public void onRoutingCancelled() {

    }
    Runnable thread=new Runnable() {
        @Override
        public void run() {
            if (!points.isEmpty()){
                LatLng point=points.poll();
                myRef.child("Latitude").setValue(point.latitude);
                myRef.child("Longitude").setValue(point.longitude);
                handler.postDelayed(this,3000);
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        handler.removeCallbacks(thread);
    }
}
