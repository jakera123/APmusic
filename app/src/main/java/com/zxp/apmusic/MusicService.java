package com.zxp.apmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by xiaoxin on 2017/11/5.
 */

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private String TAG="MusicService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mediaPlayer=MediaPlayer.create(this,R.raw.closer);
        return new MusicServiceBinder();
    }

    public void playMusic(){
        mediaPlayer.start();
        Log.i(TAG,"play.....");
    }

    public void pauseMusic(){
        mediaPlayer.pause();
        Log.i(TAG,"pause....");
    }

    public int getMusicProgress(){
        return mediaPlayer.getCurrentPosition();
    }
    public int getMusicDuration(){
        return mediaPlayer.getDuration();
    }
    public void setMusicProgress(int progress){
        //注意这里是进度不是以百分制来算！所以得经过计算
        mediaPlayer.seekTo(progress*mediaPlayer.getDuration()/100);
    }



    public class MusicServiceBinder extends Binder{

        public MusicService getMusicService(){
            return MusicService.this;
        }
    }

}
