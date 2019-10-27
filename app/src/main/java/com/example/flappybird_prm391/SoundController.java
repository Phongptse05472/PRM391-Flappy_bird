package com.example.flappybird_prm391;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundController {

    MediaPlayer swoosh, point, hit, die, wing;

    public SoundController(Context context){
        swoosh = MediaPlayer.create(context, R.raw.swoosh);
        point = MediaPlayer.create(context, R.raw.point);
        hit = MediaPlayer.create(context, R.raw.hit);
        die = MediaPlayer.create(context, R.raw.die);
        wing = MediaPlayer.create(context, R.raw.wing);
    }

    public void playSwoosh(){
        if(swoosh != null){
            swoosh.start();
        }
    }

    public void playPoint(){
        if(point != null){
            point.start();
        }
    }

    public void playHit(){
        if(hit != null){
            hit.start();
        }
    }

    public void playDie(){
        if(die != null){
            die.start();
        }
    }

    public void playWing(){
        if(wing != null){
            wing.start();
        }
    }
}
