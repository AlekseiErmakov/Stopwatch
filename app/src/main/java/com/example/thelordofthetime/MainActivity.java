package com.example.thelordofthetime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView SecondsView;
    public Button StartPauseBTN;
    public Button StopAddBTN;
    public Button StartBTN;
    public Handler handler;
    public ScrollView scrollView;
    public ListView listView;
    public ArrayList<String> leaders;
    public ArrayAdapter<String> adapter;
    ImageView dogview;
    public static volatile int countMilisec =0;
    public static volatile int countMicrosec =0;
    public static volatile int countsec =0;
    public static volatile int countDeciSec =0;
    public static volatile int countmin =0;
    public static volatile int countDecimin = 0;
    public static volatile int counthour =0;
    public static volatile int countDecihour =0;
    public static int DECI=10;
    public static int SIX=6;
    public static String A = ":";
    boolean stop = false;
    String time ="";
    Thread t;
    List<Thread> mythreads;
    List<Thread> DogThreads;
    int precommand;
    int i=1;
    float x =10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dogview = (ImageView)findViewById(R.id.imageView);
        dogview.setX(x);
        leaders = new ArrayList<>();
        listView = (ListView)findViewById(R.id.list);

        addAll();
        mythreads = new ArrayList<>();
        DogThreads = new ArrayList<>();
        adapter= new ArrayAdapter<>(this,R.layout.leadersofus,this.leaders);
        listView.setAdapter(adapter);
        dogview.setX(x);

        handler = new Handler(){
            @Override
            public void handleMessage( Message msg) {
                switch (msg.what){
                    case (0):
                        SecondsView.setText(time);
                        dogview.setX(x);
                        break;
                }

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
        time = countDecimin+countmin+A+countDeciSec+countsec+A+countMilisec+countMicrosec;


    }
    public void setAlltoZero(){
        countMilisec =0;
        countMicrosec =0;
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


        switch (v.getId()) {
            case (R.id.StartPause):
                if (precommand == R.id.Start) {
                    mythreads.get(0).interrupt();
                    DogThreads.get(0).interrupt();
                    precommand = R.id.StartPause;
                    StopAddBTN.setText(R.string.Stop);
                    StartPauseBTN.setText(R.string.Continue);
                } else if (precommand == R.id.StartPause) {
                    goSeconds();
                    dogThread();
                    StopAddBTN.setText(R.string.Add);
                    precommand = R.id.Start;
                    StartPauseBTN.setText(R.string.Pause);
                } else if(precommand==0){

                }

                break;
            case (R.id.Start):
                StartBTN.setVisibility(View.INVISIBLE);
                precommand = R.id.Start;
                StopAddBTN.setText(R.string.Add);
                dogThread();

                goSeconds();
                break;
            case (R.id.StopAdd):
                if (precommand == R.id.StartPause) {
                    StartPauseBTN.setText(R.string.Pause);
                    setAlltoZero();
                    SecondsView.setText("0:00:00");
                    StartBTN.setVisibility(View.VISIBLE);
                    leaders.clear();
                    DogThreads.clear();
                    i=1;
                    adapter.notifyDataSetChanged();
                    precommand=0;
                } else if (precommand == 0) {

                } else if (precommand==R.id.Start){

                    leaders.add("№"+i+"\t"+time);
                    i++;
                    adapter.notifyDataSetChanged();

                }
                break;
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
    public void dogThread(){
        DogThreads.clear();
        DogThreads.add(new Thread(){
            @Override
            public void run() {
                while(!isInterrupted()) {
                    try {
                        Thread.sleep(500);
                    }catch (InterruptedException ie ){
                        interrupt();
                    }
                    dogRun();

                }
            }
        });
        DogThreads.get(0).isDaemon();
        DogThreads.get(0).start();


    }
    public void dogRun(){
        while(x<700){
            x++;
            try {
                Thread.sleep(20);
            }catch (InterruptedException ie){

            }

        }
        while (x>50){
            try {
            Thread.sleep(20);
            }catch (InterruptedException ie){

            }
            x--;
        }
        dogRun();
    }






}
