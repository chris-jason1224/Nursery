package com.cj.base_common.util.cache

import com.cj.base_common.base.BaseApp
import com.tencent.mmkv.BuildConfig
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

/**
 * Author:chris - jason
 * Date:2019-12-19.
 * Package:com.cj.base_common.util.cache
 * 本地Key-value类型，数据缓存工具类
 * 保存数据时，key应该存放到 KeyTag 文件中
 */
object K_VUtil {

    var mmkv: MMKV? = null

    init {
        MMKV.initialize(BaseApp.getInstance())

        if (BuildConfig.DEBUG) {
            MMKV.setLogLevel(MMKVLogLevel.LevelWarning)
        }
        //存储加密
        mmkv = MMKV.mmkvWithID("CJ-mmkv", MMKV.MULTI_PROCESS_MODE, "jfigeubGYR@&#$%(%")
    }

    /**
     * 支持以下 Java 语言基础类型：boolean、int、long、float、double、byte[]
     * 支持以下 Java 类和容器：String、Set<String>
     */

    fun  saveData(key: String, value: Any): Boolean {
        return when (value) {
            is Boolean -> mmkv!!.encode(key, value)
            is Int -> mmkv!!.encode(key, value)
            is Long -> mmkv!!.encode(key, value)
            is Float -> mmkv!!.encode(key, value)
            is Double -> mmkv!!.encode(key, value)
            is String -> mmkv!!.encode(key, value)
            is ByteArray -> mmkv!!.encode(key, value)
            else -> false
        }
    }

    fun getData(key: String, defaultValue: Any): Any? {
        return when (defaultValue) {
            is Boolean -> mmkv!!.decodeBool(key, defaultValue)
            is Int -> mmkv!!.decodeInt(key, defaultValue)
            is Long -> mmkv!!.decodeLong(key, defaultValue)
            is Float -> mmkv!!.decodeFloat(key, defaultValue)
            is Double -> mmkv!!.decodeDouble(key, defaultValue)
            is String -> mmkv!!.decodeString(key, defaultValue)
            is ByteArray -> mmkv!!.decodeBytes(key, defaultValue)
            else -> null
        }
    }

}