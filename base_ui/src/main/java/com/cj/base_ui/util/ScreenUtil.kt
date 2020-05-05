package com.cj.base_ui.util

/**
 * Author:chris - jason
 * Date:2020-02-13.
 * Package:com.cj.base_ui.util
 */
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import java.lang.Exception
import java.lang.reflect.Field



/**
 * 获取 DisplayMetrics
 */
fun getDisplayMetrics(context: Context): DisplayMetrics {
    var displayMetrics = DisplayMetrics()
    var windowManager =
        context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}

fun getDensity(context: Context): Float {
    return context.resources.displayMetrics.density
}

fun getFontDensity(context: Context): Float {
    return context.resources.displayMetrics.scaledDensity
}

/**
 * 获取屏幕宽度
 */
fun getScreenWidth(context: Context): Int {
    return getDisplayMetrics(context).widthPixels
}

/**
 * 获取屏幕高度
 */
fun getScreenHeight(context: Context): Int {
    return getDisplayMetrics(context).heightPixels
}

fun dip2px(context: Context, dpValue: Float): Int {
    var scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun px2dip(context: Context, pxValue: Float): Int {
    var scale = context.resources.getDisplayMetrics().density
    return (pxValue / scale + 0.5f).toInt()
}

fun sp2px(context: Context, sp: Int): Int {
    return (getFontDensity(context) * sp + 0.5).toInt()
}

fun px2sp(context: Context, px: Int): Int {
    return (px / getFontDensity(context) + 0.5).toInt()
}


/**
 * 获取ActionBar高度
 */
fun getActionBarHeight(context: Context): Int {
    var actionBarHeight = 0
    var tv = TypedValue()
    if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        actionBarHeight =
            TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
    }
    return actionBarHeight
}

/**
 * 获取状态栏高度
 */
fun getStatusBarHeight(context: Context): Int {
    val c: Class<*>
    val obj: Any
    val field: Field
    val x: Int
    try {
        c = Class.forName("com.android.internal.R\$dimen")
        obj = c.newInstance()
        field = c.getField("status_bar_height")
        x = Integer.parseInt(field.get(obj).toString())
        return context.resources
            .getDimensionPixelSize(x)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return 0
}








