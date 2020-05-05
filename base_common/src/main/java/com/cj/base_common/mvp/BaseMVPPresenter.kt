package com.cj.base_common.mvp

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.cj.base_common.base.IBasePresenter
import com.cj.base_common.base.IBaseView
import com.cj.base_common.http.repository.APIService
import com.cj.base_common.http.repository.APIStore
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

/**
 * Author:chris - jason
 * Date:2019-12-12.
 * Package:com.cj.base_common.mvp
 */
abstract class BaseMVPPresenter<V>:IBasePresenter{

    var mAPIStore: APIStore? = null
    var mReference: WeakReference<Any>? = null
    var mView: V? = null
    var mDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * 用于绑定view层和p层
     */
    override fun attachView(v: Any) {
        mReference = WeakReference(v)
        mView = mReference?.get() as V
        mAPIStore = APIService.createAPI(APIStore::class.java)
    }

    /**
     * View层执行onDestroy时回调此方法
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
     fun onViewDestroy(owner: LifecycleOwner) {
        mReference?.clear()
        mReference = null
        mView = null
        mAPIStore = null
        //解除presenter中所有的Observable订阅关系
        mDisposable.clear()

    }


}