package com.cj.annotations.bus.model

/**
 * Author:chris - jason
 * Date:2019-12-25.
 * Package:com.cj.annotations.bus.model
 */
class EventRegisterEntity(fieldName:String,className:String) {
    /**
     * fieldName;//@EventRegister注解的字段名
     * className = "java.lang.Object";//@EventRegister注解的type()值，默认为Object类型
     */

     var fieldName:String
     var className:String

    init {
        this.fieldName = fieldName
        this.className = className
    }




}