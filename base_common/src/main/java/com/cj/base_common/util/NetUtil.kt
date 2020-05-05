package com.cj.base_common.util

import android.content.Context
import android.net.ConnectivityManager
import com.cj.base_common.base.BaseApp

/**
 * Author:chris - jason
 * Date:2019-12-17.
 * Package:com.cj.base_common.util
 * 网络工具类
 */
class NetUtil private constructor(){

    companion object{

        /**
         * 判断网络是否连接
         */
        fun isConnected():Boolean{
            var manager: ConnectivityManager = BaseApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var info =  manager.activeNetworkInfo
            if(info!=null && info.isConnected){
                return true
            }
            return false
        }

        /**
         * 判断当前网络是否是wifi
         * wifi是否可用不做判断
         */
        fun isWifi():Boolean{
            var manager: ConnectivityManager = BaseApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var info =  manager.activeNetworkInfo
            if(info!=null && info.type == ConnectivityManager.TYPE_WIFI){
                return true
            }
            return false
        }










    }

}