package com.cj.base_common.log

import android.os.Environment
import com.cj.base_common.base.BaseApp
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.BuildConfig
import com.orhanobut.logger.Logger
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author:chris - jason
 * Date:2020-02-02.
 * Package:com.cj.base_common.log
 * 日志打印工具类
 */

/**
 * //todo 日志加密、筛选、定时上传
 *   推送命令触发日志回捞
 */
object LOG {

    init {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    //日志等级
    private var Log_Level_Normal = 1//普通日志
    private var Log_Level_Error =2//错误信息日志

    fun log_e(message: String) {
        if (BuildConfig.DEBUG) {
            Logger.e(message)
        }
    }

    fun log_d(message: String) {
        Logger.d(message)
    }

    fun log_json(json: String) {
        Logger.json(json)
    }

    //将日志保存为文件
    fun log_file(content: String) {

        try {
            //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                log_e("sd卡信息异常，无法写入日志")
                return
            }

            var path =
                BaseApp.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + "/api/log/"
            var dir = File(path)

            if (!dir.exists()) {
                dir.mkdirs()
            }
            var current = System.currentTimeMillis()
            var time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date(current))

            var file = File(path + "_api_" + time + ".txt")

            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            var pw = PrintWriter(BufferedWriter(FileWriter(file)))
            pw.println()
            pw.println(content)
            pw.println()
            pw.close()
            log_e("日志保存成功:--> " + file.getAbsolutePath())
        } catch (e: Exception) {
            e.printStackTrace()
            log_e("日志保存失败")
        }
    }


}