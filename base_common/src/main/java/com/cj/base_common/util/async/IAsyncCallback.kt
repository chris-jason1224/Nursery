package com.cj.base_common.util.async

/**
 * Author:chris - jason
 * Date:2019-12-17.
 * Package:com.cj.base_common.util.async
 */
interface IAsyncCallback<T> {

    //异步任务执行成功，获得结果
    fun onSuccess(result : T)

    /**
     * onFailed和onComplete不会同时回调
     */

    //异步任务执行失败
    fun onFailed(throwable :Throwable)

    //异步任务执行完全结束
    fun onComplete()

}