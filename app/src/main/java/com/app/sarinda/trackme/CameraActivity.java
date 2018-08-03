package com.app.sarinda.trackme;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sarinda.trackme.tools.RequestPermissionsTool;
import com.app.sarinda.trackme.tools.RequestPermissionsToolImpl;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class CameraActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = CameraActivity.class.getSimpleName();
    private ImageView mImageCam;
    private StorageReference mStorage;
    private Uri imageuri;
    StorageReference filepath;
    private Button captureImg;
    private Button submit;
    private ConstraintLayout layout;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder mProgress;
    private TextView mFuel;
    private AlertDialog.Builder mAlert;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mImageCam = findViewById(R.id.imgCard);
        captureImg = findViewById(R.id.btnSnap);
        submit=findViewById(R.id.fuelSubmit);
        progressDialog=new ProgressDialog(this);
        mProgress=new AlertDialog.Builder(this);
        layout=findViewById(R.id.camera);
        mFuel=findViewById(R.id.fuelAmount);
        mAlert=new AlertDialog.Builder(this);
        mStorage= FirebaseStorage.getInstance().getReference();
        captureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postImage();
            }
        });
    }



    /** to get high resolution image from camera*/

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data !=null) {
            Bundle extras = data.getExtras();
            imageuri=data.getData();
            //Toast.makeText(getApplicationContext(),imageuri.toString(),Toast.LENGTH_LONG).show();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageCam.setImageBitmap(imageBitmap);
        }


    }
    private String createImageFile(){
        /***Create an image file name*/
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String imageFileName = "JPEG_" + formattedDate;

        return imageFileName;
    }
    private void postImage() {
        /***upload file to firebase*/

        final String fuelVal = mFuel.getText().toString();
        final String imageUrl = createImageFile();

        /** check fields */
        if (!TextUtils.isEmpty(fuelVal) && imageuri != null) {
            mProgress.setTitle("Confirm Fuel Amount");
            mProgress.setMessage("Fuel Amount: " + fuelVal + "Litres");
            mProgress.setCancelable(true);
            mProgress.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            mProgress.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();

                            if(!isMobileDataEnabled() && !isWifiEnabled()){

                                mAlert.setTitle("No Network Found!")
                                        .setMessage("Please turn ON Your Mobile Data or Connect to Wifi")
                                        .setIcon(R.drawable.ic_error_black_24dp)
                                        .setPositiveButton("Go to Setting", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                            }
                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        startActivity(new Intent(CameraActivity.this,HomeActivity.class));
                                    }
                                });
                                AlertDialog dialog=mAlert.create();
                                dialog.show();

                            }
                            else {

                                progressDialog.setMessage("Sending");
                                progressDialog.show();
                                StorageReference filePath = mStorage.child("photos").child(imageUrl).child(imageuri.getLastPathSegment());
                                filePath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        snackBar("Update Successfully");
                                        startActivity(new Intent(CameraActivity.this,HomeActivity.class));

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        snackBar("Failed To Update");
                                    }
                                });
                            }
                        }
                    });
            AlertDialog alertDialog=mProgress.create();
            alertDialog.show();
        }

        else
        {
            snackBar("Take a Picture And Fill the Field");
        }
    }

    public void snackBar(String msg){
        Snackbar snackbar= Snackbar.make(layout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
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
}