package com.cj.base_common.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.cj.base_common.BuildConfig
import com.cj.base_common.log.CrashHandler
import com.cj.base_common.model.MyObjectBox
import com.cj.base_common.module.IModuleDelegate
import com.cj.base_common.module.ModuleManager
import com.cj.base_common.states.OnEmptyStateCallback
import com.cj.base_common.states.OnPlaceHolderCallback
import com.cj.base_common.states.OnTimeoutStateCallback
import com.cj.base_common.util.AppSystemUtil
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import io.objectbox.BoxStore
import java.lang.ref.WeakReference

/**
 * Author:chris - jason
 * Date:2019-12-10.
 * Package:com.cj.base_common.base
 */
open class BaseApp : Application() {

    private var mCurrentCount = 0//onStart执行后，activity数量
    private var isCurrent = false
    private var delegateList: MutableList<IModuleDelegate>? = mutableListOf()
    private var mCurrentActivity: WeakReference<Activity>? = null
    var mActivityList: MutableList<Activity> = mutableListOf()
    private var mResumeActivity: MutableList<Activity> = mutableListOf()

    companion object {
        private var instance: Application? = null
        //获取Application实例
        fun getInstance() = instance!!
    }

    object ObjectBox {
        lateinit var boxStore: BoxStore
            private set

        fun init(context: Context) {
            boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
        }

    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        /**
         * 多进程app会重复启动Application，只在主进程中执行一次即可
         */
        if (AppSystemUtil.isCurrentMainProcess(this)) {

            //注册统一异常捕获
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler)

            this.mCurrentCount = 0

            //注册Application生命周期观测
            this.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

                override fun onActivityPaused(activity: Activity?) {

                }

                override fun onActivityResumed(activity: Activity?) {
                    if (!mResumeActivity.contains(activity)) {
                        mResumeActivity.add(activity!!)
                    }
                    mCurrentActivity = WeakReference(activity!!)
                }

                override fun onActivityStarted(activity: Activity?) {
                    if (mCurrentCount == 0 && !isCurrent) {
                        isCurrent = true
                        for (delegate in delegateList!!) {
                            delegate.enterForeground()
                        }
                        Log.d("BaseApplication", "The App go to foreground")
                    }
                    mCurrentCount++
                }

                override fun onActivityDestroyed(activity: Activity?) {
                    if (mActivityList.contains(activity)) {
                        mActivityList.remove(activity)
                    }
                }

                override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

                }

                override fun onActivityStopped(activity: Activity?) {
                    if (mResumeActivity.contains(activity)) {
                        mResumeActivity.remove(activity)
                        mCurrentCount--
                    }

                    if (mCurrentCount == 0 && isCurrent) {
                        isCurrent = false
                        for (delegate in delegateList!!) {
                            delegate.enterBackground()
                        }
                    }
                }

                override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                    mActivityList.add(0, activity!!)
                }

            })

            //加载各个组件
            ModuleManager.loadModules()
            delegateList = ModuleManager.delegateList

            //回调onCreate()
            if (delegateList != null) {
                for (delegate in delegateList!!) {
                    delegate.onCreate(this)
                }
            }


            //初始化ARouter
            if (BuildConfig.DEBUG) {
                ARouter.openLog()
                ARouter.openDebug()
            }
            ARouter.init(this)

            //初始化objectBox
            ObjectBox.init(this)

            //注册多布局管理器
            registerLoadSir()

        }

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //开启multiDex多分包
        MultiDex.install(this)
    }

    //获取当前activity
    fun getCurrentActivity(): Activity? {
        if (mCurrentActivity != null) {
            return mCurrentActivity!!.get()
        }
        return null
    }

    //手动杀死app
    fun exit() {
        try {
            if (mActivityList != null) {
                var iterator = mActivityList.iterator()
                while (iterator.hasNext()) {
                    var activity = iterator.next()
                    activity.finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }

    /**
     * 初始化多状态布局全局策略
     */
    private fun registerLoadSir() { //全局配置
        LoadSir.beginBuilder()
            .addCallback(OnEmptyStateCallback()) //空数据页面
            .addCallback(OnTimeoutStateCallback()) //连接超时、网络错误页面
            .addCallback(OnPlaceHolderCallback()) //占位页面
            .setDefaultCallback(SuccessCallback::class.java) //默认显示加载成功页面
            .commit()
    }

}