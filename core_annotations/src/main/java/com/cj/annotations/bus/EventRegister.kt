package com.cj.annotations.bus

import kotlin.reflect.KClass

/**
 * Author:chris - jason
 * Date:2020-01-02.
 * Package:com.cj.annotations.bus
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class EventRegister(val type: KClass<*> = Any::class)