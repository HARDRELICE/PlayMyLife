package com.hardrelice.playmylife.utils

import android.media.MediaPlayer
import com.hardrelice.playmylife.R

object MediaCenter {

    fun playAudio(id:Int){
        Thread{
            val mp = MediaPlayer.create(ApplicationContext, id)
            mp.start()
            while(mp.isPlaying);
            mp.release()
        }.start()
    }

}