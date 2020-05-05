package com.cj.base_common.http.rx

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Author:chris - jason
 * Date:2019-12-13.
 * Package:com.cj.base_common.http.rx
 * 网络请求时子线程和主线程切换
 */
class CJSchedulers {

    companion object {

//        fun <T> compose1(): ObservableTransformer<T, T> {
//
//            var otf: ObservableTransformer<T, T> = ObservableTransformer() { upstream ->
//
//                var os: ObservableSource<T> = upstream.subscribeOn(Schedulers.io())
//                    .doOnSubscribe(object : Consumer<Disposable> {
//                        override fun accept(t: Disposable?) {
//
//                        }
//                    }).observeOn(AndroidSchedulers.mainThread())
//
//                return@ObservableTransformer os
//            }
//
//            return otf
//        }


        fun <T> compose(): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                return@ObservableTransformer upstream.subscribeOn(Schedulers.io())
                    .doOnSubscribe(object : Consumer<Disposable> {
                        override fun accept(t: Disposable?) {
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
            }
        }

    }
}