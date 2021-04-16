package com.hardrelice.playmylife.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeController {

    fun time2stamp(time: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = sdf.parse(time)
        return date!!.time
    }

    fun stamp2time(stamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(stamp)
        return sdf.format(date)
    }

    fun formatRightTime(stamp: Long):String{
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = Date(stamp)
        return sdf.format(date)
    }

    fun formatLeftTime(stamp: Long):String{
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date(stamp)
        return sdf.format(date)
    }

    fun seconds2string(seconds:Int):String{
        val s = seconds%60
        var m = seconds/60
        val h = m/60
        m %= 60
        var str = ""
        str += (if(h!=0) h.toString().formatChar() else "00")+":"
        str += (if(m!=0) m.toString().formatChar() else "00")+":"
        str += s.toString().formatChar()
        return str
    }

    private fun String.formatChar():String{
        return if(this.length==1) "0$this"
        else this
    }


}