package com.hardrelice.playmylife.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import com.hardrelice.playmylife.R
import java.io.FileOutputStream


object IconManager {

    val iconDir = FileHandler.iconsDir

    val defaultIcon = hashMapOf(
        "点数" to R.drawable.coin_p,
        "心愿" to R.drawable.wish_heart
    )

    fun init() {
        FileHandler.checkDir(iconDir)
        saveIcon(R.drawable.coin_p, "点数")
        saveIcon(R.drawable.wish_heart, "心愿")
        saveIcon(R.drawable.ic_present_fill, "礼物")
    }

    fun saveIcon(resId: Int, name: String){
        val buffer = FileOutputStream("$iconDir/$name.png")
        val bitmap = ApplicationContext.resources.getDrawable(resId).toBitmap()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer)
    }

    fun getIconBitmap(name: String):Bitmap?{
        if (name==""){
            return R.drawable.ic_present_fill.getDrawable().toBitmap()
        }
        return try{
            BitmapFactory.decodeFile("$iconDir/$name.png")
        } catch (e: Exception){
            try {
                (defaultIcon[name] as Int).getDrawable().toBitmap()
            }catch (e: Exception){
                R.drawable.ic_present_fill.getDrawable().toBitmap()
            }
        }
    }

    fun Int.getDrawable(): Drawable {
        return ApplicationContext.resources.getDrawable(this, ApplicationContext.theme)
    }

    fun makeTintBitmap(inputBitmap: Bitmap?, tintColor: Int): Bitmap? {
        if (inputBitmap == null) {
            return null
        }
        val outputBitmap =
            Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, inputBitmap.config)
        val canvas = Canvas(outputBitmap)
        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(inputBitmap, 0F, 0F, paint)
        return outputBitmap
    }


}