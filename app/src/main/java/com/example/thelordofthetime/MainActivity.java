package com.example.thelordofthetime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView SecondsView;
    public Button StartPauseBTN;
    public Button StopAddBTN;
    public Button StartBTN;
    public Handler handler;
    public static int countMilisec =0;
    public static int countMicrosec =0;
    public static int countsec =0;
    public static int countDeciSec =0;
    public static int countmin =0;
    public static int countDecimin = 0;
    public static int counthour =0;
    public static int countDecihour =0;
    public static int DECI=10;
    public static int SIX=6;
    public static String A = ":";
    boolean stop = false;
    String time ="";
    Thread t;
    List<Thread> mythreads;
    int precommand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addAll();
        mythreads = new ArrayList<>();

        handler = new Handler(){
            @Override
            public void handleMessage( Message msg) {
                if (msg.what==0)
                SecondsView.setText(time);
            }
        };

    }
    public void inkrementtime(){
        countMicrosec++;
        if (countMicrosec == DECI) {
            countMicrosec = 0;
            countMilisec++;
        }
        if (countMilisec == DECI) {
            countMilisec = 0;
            countsec++;
        }
        if (countsec == DECI) {
            countsec = 0;
            countDeciSec++;
        }
        if (countDeciSec == SIX) {
            countDeciSec = 0;
            countmin++;
        }
        if (countmin == DECI) {
            countmin = 0;
            countDecimin++;
        }
        if (countDecimin == SIX) {
            countDecimin = 0;
            counthour++;
        }
        if (counthour == DECI) {
            counthour = 0;
            countDecihour++;
        }
        time = countmin+A+countDeciSec+countsec+A+countMilisec+countMicrosec;


    }
    public void setAlltoZero(){
        countMilisec =0;
        countMicrosec =-1;
        countsec =0;
        countDeciSec =0;
        countmin =0;
        countDecimin = 0;
        counthour =0;
        countDecihour =0;
    }
    public void addAll(){

        SecondsView = (TextView)findViewById(R.id.Sec);
        StartPauseBTN =(Button)findViewById(R.id.StartPause);
        StopAddBTN = (Button)findViewById(R.id.StopAdd);
        StartBTN =(Button)findViewById(R.id.Start);
        StartBTN.setOnClickListener(this);
        StopAddBTN.setOnClickListener(this);
        StartPauseBTN.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.StartPause){
            if (precommand==R.id.Start){
                mythreads.get(0).interrupt();
                precommand=R.id.StartPause;
                StartPauseBTN.setText("continue");

            } else if (precommand==R.id.StartPause){
                goSeconds();
                precommand=R.id.Start;
                StartPauseBTN.setText("Pause");
            }

        }
        else if(v.getId()==R.id.Start){
            StartBTN.setVisibility(View.INVISIBLE);
            precommand = R.id.Start;
            goSeconds();
        }else if(v.getId()== R.id.StopAdd){
            if (precommand==R.id.StartPause){
                setAlltoZero();
                StartBTN.setVisibility(View.VISIBLE);
            }else if(precommand==0){

            }
            else {
            mythreads.get(0).interrupt();
            StartBTN.setVisibility(View.VISIBLE);
            setAlltoZero();}

        }
    }
    public void goSeconds(){
        mythreads.clear();
        mythreads.add(new Thread(){
            @Override
            public void run() {
                while(!isInterrupted()) {
                    try {
                        Thread.sleep(10);

                    } catch (InterruptedException ex) {
                        interrupt();
                    }
                    inkrementtime();
                    handler.sendEmptyMessage(0);
                }
            }
        });
        mythreads.get(0).isDaemon();
        mythreads.get(0).start();
    }





}
