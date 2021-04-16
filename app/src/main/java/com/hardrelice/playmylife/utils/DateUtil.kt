package com.hardrelice.playmylife.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

const val millSecondsPerSecond = 1000
const val millSecondsPerMinute = millSecondsPerSecond * 60
const val millSecondsPerHour = millSecondsPerMinute * 60
const val millSecondsPerDay = millSecondsPerHour * 24

const val basicDateFormatString = "yyyy-MM-dd"

fun getDate(string: String, format: String = basicDateFormatString): Date? {
    val sdf = SimpleDateFormat(format)
    return sdf.parse(string)
}

fun getDateTimeStamp(string: String, format: String = basicDateFormatString): Long? {
    return getDate(string, format)?.time
}

fun getDateString(time:Long,format: String= basicDateFormatString):String{
    return SimpleDateFormat(format).format(Date(time))
}

fun daysBetween(
    firstDate: String,
    secondDate: String,
    format: String = basicDateFormatString
): Int? {
    val secondTimeStamp = getDateTimeStamp(secondDate, format)
    val firstTimeStamp = getDateTimeStamp(firstDate, format)
    if (firstTimeStamp != null && secondTimeStamp != null) {
        return ((secondTimeStamp - firstTimeStamp) / millSecondsPerDay).toInt()
    }
    return null
}

fun addDaysToDate(date: String, days: Int, format: String = basicDateFormatString): String? {
    val timeStamp = getDateTimeStamp(date, format)?.plus(days * millSecondsPerDay)
    return if (timeStamp != null) {
        getDateString(timeStamp)
    } else null
}

fun getToday():String{
    return getDateString(System.currentTimeMillis())
}