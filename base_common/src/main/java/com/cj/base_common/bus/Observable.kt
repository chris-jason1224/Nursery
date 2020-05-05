package com.cj.base_common.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * Author:chris - jason
 * Date:2019-12-23.
 * Package:com.cj.base_common.bus
 * ModuleBus被观察者接口
 */
interface Observable<T> {

    /**
     * 发送一个消息，支持前台线程、后台线程发送
     */
    fun  post(t: T)

    /**
     *发送一个消息，支持前台线程、后台线程发送
     * 需要跨进程、跨APP发送消息的时候调用该方法
     */
    fun  broadcast(value: T)

    /**
     * 发送一个消息，支持前台线程、后台线程发送
     * 需要跨进程、跨APP发送消息的时候调用该方法
     *
     * @param value
     */
    fun broadcast(value:T,foreground:Boolean);

    /**
     * 延迟发送一个消息，支持前台线程、后台线程发送
     */
    fun  postDelay(value: T, delay: Long)

    /**
     * 注册一个Observer，生命周期感知，自动取消订阅
     */
    fun  observe(owner: LifecycleOwner, observer: Observer<T>)

    /**
     * 注册一个Observer，生命周期感知，自动取消订阅
     * 如果之前有消息发送，可以在注册时收到消息（消息同步）
     */
    fun  observerSticky(owner: LifecycleOwner, observer: Observer<T>)

    /**
     * 注册一个Observer
     */
    fun  observeForever(observer: Observer<T>)

    /**
     * 注册一个Observer
     * 如果之前有消息发送，可以在注册时收到消息（消息同步）
     */
    fun  observeStickyForever(observer: Observer<T>)

    /**
     * 通过observeForever或observeStickyForever注册的，需要调用该方法取消订阅
     */
    fun  removeObserver(observer: Observer<T>)

}