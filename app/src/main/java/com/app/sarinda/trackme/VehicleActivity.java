package com.app.sarinda.trackme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class VehicleActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private String user=md5("user1@gmail.com");
    private String device=md5("device1");
    private AlertDialog.Builder mAlert;
    private AlertDialog alertDialog;
    ConstraintLayout layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        mRecyclerView=findViewById(R.id.vehi_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference().child(user);
        mAlert=new AlertDialog.Builder(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        layout=findViewById(R.id.vehicleList);
        /**check mobile data and Wifi conection*/

        if(!isMobileDataEnabled() && !isWifiEnabled()){

            mAlert.setTitle("No Network Found!")
                    .setMessage("Please turn ON Your Mobile Data or Connect to Wifi")
                    .setIcon(R.drawable.ic_error_black_24dp)
                    .setPositiveButton("Go to Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                            finish();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    startActivity(new Intent(VehicleActivity.this,HomeActivity.class));
                }
            });
            AlertDialog dialog=mAlert.create();
            dialog.show();

        }
        else {
            alertDialog= new SpotsDialog(this);
            alertDialog.show();

        }
    }

    @Override
    protected void onStart() {

        super.onStart();
        try{
            alertDialog.show();
            FirebaseRecyclerAdapter<Vehicle,VehicleViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Vehicle, VehicleViewHolder>(
                    Vehicle.class,
                    R.layout.vehicle,
                    VehicleViewHolder.class,
                    myRef
            ) {
                @Override
                protected void populateViewHolder(VehicleViewHolder viewHolder, final Vehicle model, int position) {
                    viewHolder.setVehi_model(model.getVehi_model());
                    viewHolder.setVehi_num(model.getVehi_num());
                    viewHolder.setInsurance_date(model.getInsurance_date());
                    viewHolder.setLicence_date(model.getLicence_date());
                    viewHolder.setService_date(model.getService_date());
                    alertDialog.dismiss();



                    viewHolder.livetrack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(VehicleActivity.this,MapsActivity.class);
                            intent.putExtra("model",model.getVehi_model());
                            intent.putExtra("number",model.getVehi_num());

                            startActivity(intent);
                        }
                    });
                    viewHolder.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(VehicleActivity.this,HistoryActivity.class));
                        }
                    });





                }


            };
            mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }



    }



    public static class VehicleViewHolder extends RecyclerView.ViewHolder{
        View mView;
        Button livetrack;
        Button more;

        public VehicleViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            livetrack=mView.findViewById(R.id.btn_track);
            more=mView.findViewById(R.id.btn_more);

        }
        public void setVehi_model(String vehi_model){
            TextView vehimodel=mView.findViewById(R.id.vehi_model);
            vehimodel.setText(vehi_model);
        }
        public void setVehi_num(String vehi_num){
            TextView vehinum=mView.findViewById(R.id.vehi_num);
            vehinum.setText(vehi_num);
        }
        public void setInsurance_date(String insurance_date){
            TextView insdate=mView.findViewById(R.id.insurance_date);
            insdate.setText(insurance_date);
        }
        public void setLicence_date(String licence_date){
            TextView licdate=mView.findViewById(R.id.licence_date);
            licdate.setText(licence_date);
        }
        public void setService_date(String service_date){
            TextView serdate=mView.findViewById(R.id.service_date);
            serdate.setText(service_date);
        }




    }
    /**method to get md5 string*/
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
    /**method to check mobile data*/
    private boolean isMobileDataEnabled(){
        boolean mobileDataEnabled = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);

            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileDataEnabled;
    }
    /**method to check wifi*/
   private boolean isWifiEnabled(){
       ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

       if (mWifi.isConnected()) {
           return true;
       }
       else {
           return false;
       }
   }

    public void snackBar(String msg){
        Snackbar snackbar= Snackbar.make(layout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }




}
