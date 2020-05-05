package com.cj.base_common.log

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import com.cj.base_common.base.BaseApp
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author:chris - jason
 * Date:2020-02-02.
 * Package:com.cj.base_common.log
 * 统一异常捕获
 */
object CrashHandler : Thread.UncaughtExceptionHandler {

    //磁盘/Android/data/
    private var PATH: String =
        BaseApp.getInstance().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + "/crash/log/"//日志保存路径
    //日志前缀
    private var FILE_PREFIX: String = "_crash_"
    //日志后缀
    private var FILE_NAME_SUFFIX = ".txt"

    private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()


    override fun uncaughtException(thread: Thread?, ex: Throwable?) {
        var handled = handleException(ex)
        if (!handled && mDefaultCrashHandler != null) {
            //如果自己没处理交给系统处理
            mDefaultCrashHandler.uncaughtException(thread, ex)
        } else {
            //弹出提示框告知用户app崩溃，由用户选择是否重启app
            object :Thread(){
                override fun run() {
                    super.run()
                    //为这个子线程初始化一个消息队列
                    //主线程的Looper的ThreadLocal不为null
                    Looper.prepare()
                    showDialog()
                    //开启消息队列
                    Looper.loop()
                }
            }.start()
        }
    }

    private fun handleException(ex: Throwable?): Boolean {
        try {
            //保存日志文件
            saveExceptionToSDCard(ex)
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    private fun saveExceptionToSDCard(ex: Throwable?) {

        try {
            //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                LOG.log_e("sd卡信息异常，无法写入崩溃日志")
                return
            }

            var dir = File(PATH)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            var current = System.currentTimeMillis()
            var time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date(current))

            var file = File(PATH + FILE_PREFIX + time + FILE_NAME_SUFFIX)

            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            var pw = PrintWriter(BufferedWriter(FileWriter(file)))
            pw.println(time)
            pw.println()
            pw.println(getPhoneInformation())
            pw.println()
            ex?.printStackTrace(pw)
            pw.close()
            LOG.log_e("崩溃日志保存成功:--> " + file.getAbsolutePath())

        } catch (e: java.lang.Exception) {
            LOG.log_e("崩溃日志保存失败")
            e.printStackTrace()
        }

    }

    //弹出崩溃提示框
    private fun showDialog(){


    }


    //获取手机设备信息
    private fun getPhoneInformation(): String {

        var context = BaseApp.getInstance()
        var pm = context.packageManager

        var versionName =
            pm.getPackageInfo(context.packageName, PackageManager.GET_CONFIGURATIONS).versionName
        var versionCode =
            pm.getPackageArchiveInfo(context.packageName, PackageManager.GET_CONFIGURATIONS)
                .versionCode

        var sb = StringBuffer()

        //获取app的版本号
        sb.append("App version name:")
            .append(versionName)
            .append(", version code:")
            .append(versionCode)
            .append("\n")

        //Android版本号
        sb.append("Android OS Version: ")
        sb.append(Build.VERSION.RELEASE)
        sb.append("_")
        sb.append(Build.VERSION.SDK_INT).append("\n")

        //手机制造商
        sb.append("Vendor: ")
        sb.append(Build.MANUFACTURER).append("\n")

        //手机型号
        sb.append("Model: ")
        sb.append(Build.MODEL).append("\n")

        //CPU架构
        sb.append("CPU ABI:")
        sb.append(Build.CPU_ABI)

        sb.append("\n")

        return sb.toString()
    }

    //获取启动信息
    private fun getLauncherIntent(): Intent {
        var context = BaseApp.getInstance()
        var pkm = context.getPackageManager()
        var pkName =
            pkm.getPackageInfo(context.packageName, PackageManager.GET_CONFIGURATIONS).packageName
        return pkm.getLaunchIntentForPackage(pkName)
    }

    private fun exit() {
        (BaseApp.getInstance() as BaseApp).exit()
    }


}