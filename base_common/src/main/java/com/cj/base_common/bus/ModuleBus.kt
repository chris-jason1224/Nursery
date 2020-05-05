package com.cj.base_common.bus

import android.content.Context
import android.content.res.AssetManager
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.ExternalLiveData
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.cj.annotations.MODULE_EVENT_FILE_PRE
import com.cj.annotations.bus.model.ModuleEventCenterEntity
import com.cj.base_common.base.BaseApp
import com.cj.base_common.util.JSONUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

/**
 * Author:chris - jason
 * Date:2019-08-28.
 * Package:com.cj.common.bus
 * 组件间消息总线
 */

object ModuleBus {

    var TAG: String = "ModuleBus"
    var eventCenterEntityList: ArrayList<ModuleEventCenterEntity>? = null
    var bus: HashMap<String?, HashMap<String?, LiveEvent<*>>>? = null

    init {
        eventCenterEntityList = ArrayList()
        bus = HashMap()

        //首次使用时初始化
        init(BaseApp.getInstance())
    }

    fun init(context: Context) {

        var assetManager: AssetManager = context.assets
        try {
            var fileList = assetManager.list("")
            for (file in fileList) {

                if (file.startsWith(MODULE_EVENT_FILE_PRE) && file.endsWith(".json")) {
                    var moduleEventCenterEntity: ModuleEventCenterEntity? =
                        parse(assetManager.open(file))
                    if (moduleEventCenterEntity == null) {
                        continue
                    }
                    eventCenterEntityList!!.add(moduleEventCenterEntity)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //读取Module_Event_At_XXX.json文件
    private fun parse(inputstream: InputStream): ModuleEventCenterEntity? {

        try {
            var ins = InputStreamReader(inputstream, "UTF-8")
            var reader = BufferedReader(ins)

            var line: String = ""
            while (true) {
                line = reader.readLine() ?: break
            }
            if (!TextUtils.isEmpty(line)) {
                var j = JSONObject(line)
                var moduleName = j.getString("moduleName")
                var delegateName = j.getString("delegateName")
                var pkgName = j.getString("pkgName")
                var fromJson = ModuleEventCenterEntity(moduleName, delegateName, pkgName)
                return fromJson
            }

            reader.close()
            ins.close()
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "解析Module_Event_At_XXX.json出错")
        }

        return null
    }


    class LiveEvent<T> : Observable<T> {

        private var key: String? = null
        private var liveData: LifeCycleLiveData<T>? = null
        private var observerMap: HashMap<Observer<*>, ObserverWrapper<T>>? = null
        private var mainHandler: Handler? = null

        constructor(key: String) {
            this.key = key
            this.liveData = LifeCycleLiveData()
        }

        init {
            observerMap = HashMap()
            mainHandler = Handler(Looper.getMainLooper())
        }

        inner class LifeCycleLiveData<T> : ExternalLiveData<T>() {

            override fun observerActiveLevel(): Lifecycle.State {
                return Lifecycle.State.CREATED
            }

            override fun removeObserver(observer: Observer<in T>) {
                super.removeObserver(observer)
                if (!liveData!!.hasObservers()) {
                    bus!!.remove(key)
                }
            }
        }

        inner class PostValueTask : Runnable {

            private var newValue: Any? = null

            constructor(newValue: Any) {
                this.newValue = newValue
            }

            override fun run() {
                postInternal(newValue as T)
            }

        }

        @MainThread
        private fun postInternal(value: T) {
            liveData!!.setValue(value)
        }

        @MainThread
        private fun broadcastInternal(value: T, foreground: Boolean) {
//            Intent intent = new Intent(ModuleBusConstant.intent_name)
//            if (foreground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
//            }
//            intent.putExtra(ModuleBusConstant.event_key, key)
//            try {
//                encoder.encode(intent, value)
//                context.getApplicationContext().sendBroadcast(intent)
//            } catch (Exception e) {
//                e.printStackTrace()
//            }

            //todo 跨进程发送消息，暂时不支持

        }

        @MainThread
        private fun observeInternal(owner: LifecycleOwner, observer: Observer<T>) {
            var observerWrapper: ObserverWrapper<T> = ObserverWrapper(observer)
            observerWrapper.preventNextEvent =
                liveData!!.getVersion() > ExternalLiveData.START_VERSION
            liveData!!.observe(owner, observerWrapper)
        }

        @MainThread
        private fun observeStickyInternal(owner: LifecycleOwner, observer: Observer<T>) {
            var observerWrapper: ObserverWrapper<T> = ObserverWrapper(observer)
            liveData!!.observe(owner, observerWrapper)
        }

        @MainThread
        private fun observeForeverInternal(observer: Observer<T>) {
            var observerWrapper: ObserverWrapper<T> = ObserverWrapper(observer)
            observerWrapper.preventNextEvent =
                liveData!!.getVersion() > ExternalLiveData.START_VERSION
            observerMap!!.put(observer, observerWrapper)
            liveData!!.observeForever(observerWrapper)
        }

        @MainThread
        private fun observeStickyForeverInternal(observer: Observer<T>) {
            var observerWrapper: ObserverWrapper<T> = ObserverWrapper(observer)
            observerMap!!.put(observer, observerWrapper)
            liveData!!.observeForever(observerWrapper)
        }

        @MainThread
        private fun removeObserverInternal(observer: Observer<T>) {
            var realObserver: Observer<T>
            if (observerMap!!.containsKey(observer)) {
                realObserver = observerMap!!.remove(observer)!!
            } else {
                realObserver = observer
            }
            liveData!!.removeObserver(realObserver)
        }


        override fun post(t: T) {
            if (isMainThread()) {
                postInternal(t)
            } else {
                mainHandler!!.post(PostValueTask(t as Any))
            }
        }

        override fun broadcast(value: T) {
            broadcast(value, false)
        }

        override fun broadcast(value: T, foreground: Boolean) {
            if (isMainThread()) {
                broadcastInternal(value, foreground)
            } else {
                mainHandler!!.post { broadcastInternal(value, foreground) }
            }
        }

        override fun postDelay(value: T, delay: Long) {
            mainHandler!!.postDelayed(PostValueTask(value as Any), delay)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            if (isMainThread()) {
                observeInternal(owner, observer)
            } else {
                mainHandler!!.post { observeInternal(owner, observer) }
            }
        }

        override fun observerSticky(owner: LifecycleOwner, observer: Observer<T>) {
            if (isMainThread()) {
                observeStickyInternal(owner, observer)
            } else {
                mainHandler!!.post { observeStickyInternal(owner, observer) }
            }
        }

        override fun observeForever(observer: Observer<T>) {
            if (isMainThread()) {
                observeForeverInternal(observer)
            } else {
                mainHandler!!.post { observeForeverInternal(observer) }
            }
        }

        override fun observeStickyForever(observer: Observer<T>) {
            if (isMainThread()) {
                observeStickyForeverInternal(observer)
            } else {
                mainHandler!!.post { observeStickyForeverInternal(observer) }
            }
        }

        override fun removeObserver(observer: Observer<T>) {
            if (isMainThread()) {
                removeObserverInternal(observer)
            } else {
                mainHandler!!.post { removeObserverInternal(observer) }
            }
        }

    }

    class ObserverWrapper<T> : Observer<T> {

        private var observer: Observer<T>? = null
        internal var preventNextEvent: Boolean = false

        constructor(observer: Observer<T>) {
            this.observer = observer
        }

        override fun onChanged(t: T) {
            if (preventNextEvent) {
                preventNextEvent = false
                return
            }
            try {
                observer!!.onChanged(t)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    private fun isMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }


    /**
     * @param clz Gen_fun_business_Interface.class
     * @param <T>
     * @return
     */
    inline fun <reified T> of(clz: Class<T>): T? {
        var t: T? = null
        var moduleName: String? = ""
        for (entity in eventCenterEntityList!!) {
            var intfName: String = clz.getName()
            if (!TextUtils.isEmpty(intfName)) {
                var splits = intfName.split(".")
                var nameArray = splits[splits.size-1].split("_")
                if("${nameArray[1]}_${nameArray[2]}" == entity.moduleName){
                    moduleName = entity.moduleName
                    break
                }
            }
        }

        if (!TextUtils.isEmpty(moduleName)) {
            //动态生成的接口名
            //接口名：gen.com.cj.bus.Gen_ +moduleName+ _Interface
            var interfaceName = "com.cj.bus.Gen_" + moduleName + "_Interface"

            if (!TextUtils.equals(interfaceName, clz.getCanonicalName())) {
                return null
            }
            try {
                var intf = Class.forName(interfaceName)
                if (intf != null && intf.isInterface()) {
                    var handler = ModuleBusInvocationHandler(moduleName)
                    var classLoader = clz.getClassLoader()
                    var interfaces = arrayOf(clz)
                    t = Proxy.newProxyInstance(classLoader, interfaces, handler) as T?
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }

        }

        return t
    }

    class ModuleBusInvocationHandler : InvocationHandler {

        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {

            var methodName = method!!.getName()

            var array: List<String> = methodName.split("_")

            //消息名
            var eventName: String = array[1]

            var liveEvent: LiveEvent<*>?

            if (!bus!!.containsKey(moduleName)) {
                var map_1: HashMap<String?, LiveEvent<*>> = HashMap()
                liveEvent = LiveEvent<Any>(eventName)
                map_1.put(eventName, liveEvent)
                bus!!.put(moduleName, map_1)
            } else {
                var map_1: java.util.HashMap<String?, LiveEvent<*>>? = bus!!.get(moduleName)
                if (map_1!!.containsKey(eventName)) {
                    liveEvent = map_1.get(eventName)
                } else {
                    liveEvent = LiveEvent<Any>(eventName);
                    map_1.put(eventName, liveEvent)
                }
            }

            return liveEvent as Any
        }

        private var moduleName: String? = ""

        constructor(moduleName: String?) {
            this.moduleName = moduleName
        }

    }

}

