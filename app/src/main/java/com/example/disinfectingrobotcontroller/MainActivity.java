package com.example.disinfectingrobotcontroller;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView directionTextView,pwmTextView,ipTextView;
    EditText debugEditText;
    Button reloadButton,clearButton;
    Button forwardButton,backwardButton,leftButton,rightButton,stopButton;
    Button changeIpButton;
    SeekBar pwmSeekBar;
    Switch uvSwitch;
    WebView webView;
    boolean lockTheFunction=false;

    //triggering flags
    boolean useFunctionFlag =false;

    //variables for repeatation
    private Handler repeatUpdateHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //keeping the display awake all the time
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //linking all the elements
        this.linkAllTheElements();
        //setting website in webview
        this.setUpTheWebView();
        //setting up the listeners to the buttons
        this.setUpListenersToTheButtons();
        //setting up listeners to the directional butttons
        this.setListenersToTheDirectionalButtons();
        //setting up listeners to seek bar
        this.setListenerToThePwmSeekBar();
        //setting listener to uv switch
        this.setListenerToUVSwitch();


    }
    //function to link all xml elements
    void linkAllTheElements(){
        //linking the switch
        uvSwitch=findViewById(R.id.uvSwitch);
        //linking the textviews
        directionTextView=findViewById(R.id.directionTextView);
        pwmTextView=findViewById(R.id.pwmTextView);
        //ipTextView=findViewById(R.id.ipAddressTextView);
        //linking the edit text
       // debugEditText=findViewById(R.id.debugEditText);

        //linking the buttons
        reloadButton=findViewById(R.id.reloadButton);
        //clearButton=findViewById(R.id.clearButton);

        forwardButton=findViewById(R.id.forwardButton);
        backwardButton=findViewById(R.id.back);
        leftButton=findViewById(R.id.leftButton);
        rightButton=findViewById(R.id.rightButton);
        stopButton=findViewById(R.id.stopButton);

      //  changeIpButton=findViewById(R.id.changeIpButton);

        //linking the seek bar
        pwmSeekBar=findViewById(R.id.pwmSeekBar);
        webView=findViewById(R.id.webView);

    }

    void setUpTheWebView(){
        //String path="https://www.youtube.com/";
        String path="http://192.168.0.100:8000/index.html";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
      /*  webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }
            }

        });*/
        webView.loadUrl(path);
    }

    void setUpListenersToTheButtons(){
        //Setting to reload and clear button
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String path="https://www.youtube.com/";
                String path="http://192.168.0.100:8000/index.html";
                webView.loadUrl(path);
            }
        });
//        clearButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                debugEditText.setText("");
//            }
//        });
//        changeIpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String ipTextViewText= String.valueOf(ipTextView.getText());
//                if(ipTextViewText.equals("192.168.0.100")){
//                    ipTextView.setText("192.168.0.101");
//                }else if(ipTextViewText.equals("192.168.0.101")){
//                    ipTextView.setText("192.168.0.100");
//                }
//            }
//        });
    }
    void setListenersToTheDirectionalButtons(){
        //Setting listeners to the directional buttons
        //forward button
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGetRequestToServerUsingButton("forward");
            }
        });

        forwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP && useFunctionFlag==true){
                    useFunctionFlag=false;
                }
                return false;
            }
        });
        //backward button
        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGetRequestToServerUsingButton("backward");
            }
        });

        backwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP && useFunctionFlag==true){
                    useFunctionFlag=false;
                }
                return false;
            }
        });

        //left button
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGetRequestToServerUsingButton("left");
            }
        });

        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP && useFunctionFlag==true){
                    useFunctionFlag=false;
                }
                return false;
            }
        });
        //Right Button
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGetRequestToServerUsingButton("right");
            }
        });

        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP && useFunctionFlag==true){
                    useFunctionFlag=false;
                }
                return false;
            }
        });

        //Right Button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGetRequestToServerUsingButton("stop");
            }
        });

        stopButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP && useFunctionFlag==true){
                    useFunctionFlag=false;
                }
                return false;
            }
        });

    }
    void setListenerToThePwmSeekBar(){
        pwmSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pwmTextView.setText("PWM:"+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendGetRequestToServerUsingSeekBarOrSwitch();
            }
        });
    }
    void setListenerToUVSwitch(){
        uvSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sendGetRequestToServerUsingSeekBarOrSwitch();
            }
        });
    }

    void sendGetRequestToServerUsingButton(String direction){
        //setting up the directional text view
        if(direction.equals("forward")){
            directionTextView.setText("F");
        }else if(direction.equals("backward")){
            directionTextView.setText("B");
        }else if(direction.equals("left")){
            directionTextView.setText("L");
        }else if(direction.equals("right")){
            directionTextView.setText("R");
        }else if(direction.equals("stop")){
            directionTextView.setText("S");
        }


        //setting  up the url
        String url="";
        if(String.valueOf(ipTextView.getText()).equals("192.168.0.100")){
            url="http://192.168.0.100/"+direction+"?pwm=";
        }else if(String.valueOf(ipTextView.getText()).equals("192.168.0.101")){
            url="http://192.168.0.101/"+direction+"?pwm=";
        }

        url+=String.valueOf( pwmTextView.getText()).replaceAll("[^0-9]", "");
        url+="&uvl=";
        if(uvSwitch.isChecked()){
            url+="1";
        }else{
            url+="0";
        }



        if(lockTheFunction==false){//the lock is open
            //Proceed
            Log.d("TAG1","You are free to go. But I am locking it");
            lockTheFunction=true;
        }else{//the lock is closed
            Log.d("TAG1","You are still locked");
            return;
        }



        debugEditText.append("\n"+url);
        //Now sending the request
        OkHttpClient client=new OkHttpClient();
        final String clientUrl=url;
        Request request=new Request.Builder().url(clientUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                Log.d("TAG1","ERROR"+e);
                debugEditText.setText(clientUrl+":\nERROR"+e.toString());
                lockTheFunction=false;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                /*if(response!=null){
                    if( response.isSuccessful()){
                    *//*debugEditText.append("\n****************************");
                    debugEditText.append("\nSending request to :"+clientUrl);*//*
                        //Log.d("TAG1","\nResponse:\n"+response.body().string());
                        debugEditText.setText("Response Received");
                        lockTheFunction=false;

                    }
                }else{
                    debugEditText.setText("Response is NULL");
                    lockTheFunction=false;
                }*/
            }
        });


    }

    void sendGetRequestToServerUsingSeekBarOrSwitch(){

        //Constructing the url
        String direction="";
        direction= String.valueOf(directionTextView.getText());
        if(direction.equals("F")){
            direction="forward";
        }else if(direction.equals("B")){
            direction="backward";
        }else if(direction.equals("L")){
            direction="left";
        }else if(direction.equals("R")){
            direction="right";
        }else if(direction.equals("S")){
            direction="stop";
        }
        //setting  up the url
        String url="";
        if(String.valueOf(ipTextView.getText()).equals("192.168.0.100")){
            url="http://192.168.0.100/"+direction+"?pwm=";
        }else if(String.valueOf(ipTextView.getText()).equals("192.168.0.101")){
            url="http://192.168.0.101/"+direction+"?pwm=";
        }
         url+=String.valueOf( pwmTextView.getText()).replaceAll("[^0-9]", "");
        url+="&uvl=";
        if(uvSwitch.isChecked()){
            url+="1";
        }else{
            url+="0";
        }

        if(lockTheFunction==false){//the lock is open
            //Proceed
            Log.d("TAG1","You are free to go. But I am locking it");
            lockTheFunction=true;
        }else{//the lock is closed
            Log.d("TAG1","You are still locked");
            return;
        }

        debugEditText.append("\n"+url);

        //Now sending the request
        OkHttpClient client=new OkHttpClient();
        final String clientUrl=url;
        Request request=new Request.Builder().url(clientUrl).build();





        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                Log.d("TAG1","ERROR"+e);
                debugEditText.setText(clientUrl+":\nERROR"+e.toString());
                lockTheFunction=false;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                /*if(response!=null){
                    if( response.isSuccessful()){
                    *//*debugEditText.append("\n****************************");
                    debugEditText.append("\nSending request to :"+clientUrl);*//*
                        //Log.d("TAG1","\nResponse:\n"+response.body().string());
                        debugEditText.setText("Response Received");
                        lockTheFunction=false;

                    }
                }else{
                    debugEditText.setText("Response is NULL");
                    lockTheFunction=false;
                }*/
            }
        });



    }



}