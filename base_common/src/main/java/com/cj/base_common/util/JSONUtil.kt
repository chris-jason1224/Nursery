package com.cj.base_common.util

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser

/**
 * Author:chris - jason
 * Date:2019-12-16.
 * Package:com.cj.base_common.util
 * JSON数据转换工具类（powered by google.gson）
 */
object JSONUtil {

    /**
     * JsonString -> JavaBean
     */
    fun <T> jsonString2JavaObject(jsonString:String,clz:Class<T>):T{
        return Gson().fromJson(jsonString,clz)
    }

    /**
     * JavaBean -> JsonString
     */
    fun  javaObject2JsonString(obj:Any):String{
        return Gson().toJson(obj)
    }

    /**
     * jsonArray -> JavaList
     *
     *  泛型在编译期类型被擦除导致报错
     *  List<T> = new Gson().fromJson(gsonString, new TypeToken<List<T>>() {}.getType());
     */
    fun <T> jsonArray2JavaList(jsonArray: String,cls:Class<T>):List<T>{
        var gson = Gson()
        var list = ArrayList<T>()
        var array:JsonArray = JsonParser.parseString(jsonArray).asJsonArray
        for (elem in array){
            list!!.add(gson.fromJson(elem,cls))
        }
        return list
    }

    /**
     * JavaList -> JsonArray
     */
    fun <T>javaList2JsonArray(list:List<T>):String{
        return Gson().toJson(list)
    }



}