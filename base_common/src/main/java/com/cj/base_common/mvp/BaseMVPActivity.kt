package com.cj.base_common.mvp

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.cj.base_common.base.BaseActivity
import com.cj.base_common.base.IBasePresenter

/**
 * Author:chris - jason
 * Date:2019-12-12.
 * Package:com.cj.base_common.mvp
 */
abstract class BaseMVPActivity<P:IBasePresenter> : BaseActivity() {

    var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        //创建presenter
        mPresenter = createPresenter()

        //管理P层生命周期
        lifecycle.addObserver(mPresenter!!)

        //绑定p、v
        mPresenter!!.attachView(this)

        //完成view和p层绑定之后再执行BaseActivity.onCreate()
        super.onCreate(savedInstanceState)
    }

    abstract fun createPresenter(): P


}