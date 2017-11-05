package com.zxp.apmusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private Button btn_play,btn_pause,btn_stop;

    private String TAG="MainActivity";
    private MyButtonListener myButtonListener;

    private MusicService musicService;

    private MusicTHread musicTHread;

    private SeekBar seekBar;

    private boolean isThreadRun=true;

    private boolean isPlayButton=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myButtonListener=new MyButtonListener();

        musicTHread=new MusicTHread();

        seekBar=(SeekBar)findViewById(R.id.sb_music_progress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.setMusicProgress(seekBar.getProgress());
            }
        });

        Intent intent=new Intent(MainActivity.this,MusicService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                musicService=((MusicService.MusicServiceBinder)service).getMusicService();
                //Toast.makeText(MainActivity.this,"bind sucess",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                //Toast.makeText(MainActivity.this,"I am sorry,I try my best……",Toast.LENGTH_SHORT).show();
            }
        },BIND_AUTO_CREATE);



       // mediaPlayer.prepareAsync();

        btn_play=(Button)findViewById(R.id.btn_play);
        btn_stop=(Button)findViewById(R.id.btn_stop);
        btn_pause=(Button)findViewById(R.id.btn_pause);
        btn_play.setOnClickListener(myButtonListener);
        btn_stop.setOnClickListener(myButtonListener);
        btn_pause.setOnClickListener(myButtonListener);
    }

    private class MyButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_play:
                    Log.i(TAG,"I am playing……");
                    if (isPlayButton){
                        btn_play.setBackgroundResource(R.drawable.btn_stop);
                        musicService.playMusic();
                    }else{
                        btn_play.setBackgroundResource(R.drawable.btn_play);
                        musicService.pauseMusic();
                    }
                    isPlayButton=!isPlayButton;

                    if(isThreadRun){
                        musicTHread.start();
                        isThreadRun=false;
                    }
                    break;
                case R.id.btn_pause:
                    Log.i(TAG,"Please pause……");

                    break;
                case R.id.btn_stop:
                    Log.i(TAG,"Stoping……");
                    musicTHread.start();
                    break;
            }

        }
    }

    private class MusicTHread extends Thread{
        @Override
        public void run() {
            while (true){
                int musicposition=musicService.getMusicProgress();
                int musicDuration=musicService.getMusicDuration();
                float musicprogress= (float)musicposition/musicDuration;
                final int progress=(int)(musicprogress*100) ;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        seekBar.setProgress(progress);
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
