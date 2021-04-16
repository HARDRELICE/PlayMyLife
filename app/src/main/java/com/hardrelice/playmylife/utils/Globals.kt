package com.hardrelice.playmylife.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.hardrelice.playmylife.MainActivity
import com.hardrelice.playmylife.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Global Value
 */
lateinit var handler: UIHandler
lateinit var ApplicationContext: Context
lateinit var GlobalActivity: MainActivity

/**
 * Extend Infix Functions
 */
infix fun String.times(other: Int): String {
    var string = ""
    for (x in 0 until other) {
        string += this
    }
    return string
}

infix fun Int.times(other: String): String {
    var string = ""
    for (x in 0 until this) {
        string += other
    }
    return string
}

infix fun Char.times(other: Int): String {
    var string = ""
    for (x in 0 until other) {
        string += this
    }
    return string
}

infix fun Int.times(other: Char): String {
    var string = ""
    for (x in 0 until this) {
        string += other
    }
    return string
}

/**
 * Extend Class Functions
 */
fun Int.getColor(): Int {
    return ApplicationContext.resources.getColor(this)
}

fun Int.getDrawable(): Drawable {
    return ApplicationContext.resources.getDrawable(this, ApplicationContext.theme)
}

fun Int.getString():String{
    return ApplicationContext.resources.getString(this)
}

fun View.onSingleClick(listener: (View) -> Unit, interval: Int = 500, isShareSingleClick: Boolean = true) {
    setOnClickListener {
        val target = if (isShareSingleClick) getActivity(this)?.window?.decorView ?: this else this
        val millis = target.getTag(R.id.single_click_tag_last_single_click_millis) as? Long ?: 0
        if (SystemClock.uptimeMillis() - millis >= interval) {
            target.setTag(
                R.id.single_click_tag_last_single_click_millis, SystemClock.uptimeMillis()
            )
            listener.invoke(this)
        }
    }
}

fun View.closeKeyboard() {
    handler.post {
        GlobalActivity.currentFocus?.clearFocus()
        val imm =
            GlobalActivity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

fun MutableList<Pair<String,Int>>.removeEmpty():MutableList<Pair<String,Int>>{
    val newList = mutableListOf<Pair<String,Int>>()
    for (pair in this){
        if (pair.second!=0) newList.add(pair)
    }
    return if (newList.size!=0) newList else mutableListOf("" to 0)

}

/**
 * Extend Normal Functions
 */
fun getActivity(view: View): Activity? {
    var context = view.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun quickSort(arr: MutableList<Long>, low: Int, high: Int) {
    if (low >= high) {
        return
    }
    var t: Long
    var i: Int = low
    var j: Int = high
    //temp就是基准位
    val temp: Long = arr[low]
    while (i < j) {
        //先看右边，依次往左递减
        while (temp <= arr[j] && i < j) {
            j--
        }
        //再看左边，依次往右递增
        while (temp >= arr[i] && i < j) {
            i++
        }
        //如果满足条件则交换
        if (i < j) {
            t = arr[j]
            arr[j] = arr[i]
            arr[i] = t
        }
    }
    //最后将基准为与i和j相等位置的数字交换
    arr[low] = arr[i]
    arr[i] = temp
    //递归调用左半数组
    quickSort(arr, low, j - 1)
    //递归调用右半数组
    quickSort(arr, j + 1, high)
}


/**
 * Extend Basic Functions
 */
fun getFormattedDate():String{
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    val date = Date(System.currentTimeMillis())
    return sdf.format(date)
}

/**
 * Global Static Value
 */
val fakeTask = """taskId|1
taskTitle|写一篇随笔
taskDescription|60分钟完成
taskHasDeadLine|false
taskDeadlineTime|0
taskHasDuration|false
taskDuration|3600
taskHasNotification|false
taskNotificationTime|0
taskRewardList|点数,100;心愿,5;
taskCycle|1
taskCycleLimit|10
taskCycleCount|0
taskCycleStartDate|0
taskExclusive|true
exclusiveWeekday|
exclusiveMonthDate|1;
exclusiveYearDate|02-14;
exclusiveSpecificDate|2022-03-04;
"""