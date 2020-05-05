package com.cj.base_common.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import com.cj.base_common.base.BaseApp

/**
 * Author:chris - jason
 * Date:2019-12-13.
 * Package:com.cj.base_common.util
 * app工具类
 */
object AppSystemUtil {


    /**
     * 获取App版本名
     */
    fun getAppVersionName(): String {
        var context: Context = BaseApp.getInstance()
        var manager: PackageManager = context.packageManager
        var pkgInfo: PackageInfo = manager.getPackageInfo(context.packageName, 0)
        return pkgInfo.versionName
    }

    //获取App版本号
    fun getAppVersionCode(): Int {
        var context: Context = BaseApp.getInstance()
        var manager: PackageManager = context.packageManager
        var pkgInfo: PackageInfo = manager.getPackageInfo(context.packageName, 0)
        return pkgInfo.versionCode
    }

    //获取App名
    fun getAppName(): String {
        var context: Context = BaseApp.getInstance()
        var manager: PackageManager = context.packageManager
        var applicationInfo = manager.getApplicationInfo(context.packageName, 0)
        return (manager.getApplicationLabel(applicationInfo)).toString()
    }


    //判断当前进程是否是主进程
     fun isCurrentMainProcess(context: Context): Boolean {

        var manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (manager != null) {
            //获取运行的进程列表
            var runningAppProcessInfoList: MutableList<ActivityManager.RunningAppProcessInfo> =
                manager.getRunningAppProcesses();

            if (runningAppProcessInfoList != null && runningAppProcessInfoList.size > 0) {

                for (info in runningAppProcessInfoList) {
                    //当前进程 id = 运行进程 id，默认主进程名等于包名
                    if (info.pid == android.os.Process.myPid()) {
                        if (TextUtils.equals(info.processName, context.packageName)) {
                            return true
                        }
                    }
                }

            }
        }

        return false
    }


}