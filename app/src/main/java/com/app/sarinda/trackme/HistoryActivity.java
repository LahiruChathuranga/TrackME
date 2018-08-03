package com.app.sarinda.trackme;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sarinda.trackme.tools.VehicleHistoryAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private ImageButton fromDate;
    private ImageButton toDate;
    private TextView mFrom;
    private TextView mTo;
    private ConstraintLayout layout;
    private boolean click=true;
    Date d1;
    Date d2;
    private RecyclerView recyclerView;
    private VehicleHistoryAdapter adapter;
    private List<VehicleHistoryData> vlist;
    private Button mShow;
    private RecyclerView.LayoutManager layoutManager;
    SimpleDateFormat sdf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        init();
        mShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mFrom.getText()) && !TextUtils.isEmpty(mTo.getText()))
                {
                    try {
                        Date datefrom=sdf.parse("21/07/2018");
                        Date dateto=sdf.parse("22/07/2018");
                        if(d1.equals(datefrom) && d2.equals(dateto)){
                            SetAdapter();
                        }
                        else{
                            snackBar("No History Data Found!!");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    snackBar("Select Date Fields First !!");
                }




            }
        });
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDate();
                click = true;

            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate();
                click=false;
            }
        });
    }

    private void init() {
        fromDate=findViewById(R.id.btnFrom);
        toDate=findViewById(R.id.btnTo);
        mFrom=findViewById(R.id.fromDate);
        mTo=findViewById(R.id.toDate);
        layout = findViewById(R.id.history);
        recyclerView=findViewById(R.id.rview);
        mShow=findViewById(R.id.btnShow);
        vlist=new ArrayList<>();
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        if(click){
            mFrom.setText(date);
        }
        else
        {
            sdf=new SimpleDateFormat("dd/mm/yyyy");

            try {
                d1=sdf.parse(mFrom.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                 d2=sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(TextUtils.isEmpty(mFrom.getText())){
                snackBar("Select 'FROM DATE' First");
            }
            else
            {
                if(d1.after(d2))
                {
                    snackBar("Pickup Correct Date!!");
                }
                else
                {
                    mTo.setText(date);
                }
            }




        }





    }
    public void SetAdapter(){
        VehicleHistoryData data=new VehicleHistoryData("54kmph","10km","2018/07/21");
        vlist.add(data);
        data=new VehicleHistoryData("65kmph","6km","2018/07/22");
        vlist.add(data);
        adapter=new VehicleHistoryAdapter(vlist);
        recyclerView.setAdapter(adapter);



    }

    public void getDate(){

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                HistoryActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
    public void snackBar(String msg){
        Snackbar snackbar= Snackbar.make(layout,msg,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
