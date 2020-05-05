package com.cj.base_common.util.async

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Author:chris - jason
 * Date:2019-12-17.
 * Package:com.cj.base_common.util.async
 * 用于执行异步任务的管理中心，尽量不要直接使用野线程，通过该类来执行异步任务，自动切换线程
 *
 * 改为协程封装
 */

object AsyncCenter{

    //线程池
    private var executor:Executor

    init {
        executor = Executors.newScheduledThreadPool(5)
    }

    fun <T> submit(exec:Exec<T>,callback: IAsyncCallback<T>){

        getResult(exec).observeOn(AndroidSchedulers.mainThread()).
            subscribe(Consumer<T> {
                callback.onSuccess(it)
            },Consumer<Throwable> {
                callback.onFailed(it)
        }, Action {callback.onComplete() })

    }

    private fun <T>  getResult(exec:Exec<T>):Observable<T>{
        return Observable.create(ObservableOnSubscribe<T> { emitter ->
            try {
                emitter.onNext(exec.execute())
                emitter.onComplete()
            }catch (e:Exception){
                emitter.onError(e)
            }
        }).subscribeOn(Schedulers.from(executor))
    }



}