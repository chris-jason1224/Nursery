package com.cj.base_common.base

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.cj.base_common.R
import com.cj.base_common.network.NetworkCenter
import com.cj.base_common.states.OnEmptyStateCallback
import com.cj.base_common.states.OnPlaceHolderCallback
import com.cj.base_common.states.OnTimeoutStateCallback
import com.cj.base_common.states.StateEntity
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.Convertor
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.kingja.loadsir.core.Transport

/**
 * Author:chris - jason
 * Date:2019-12-10.
 * Package:com.cj.base_common.base
 * Activity最底层基类
 */
abstract class BaseActivity : AppCompatActivity(), View.OnClickListener,
    NetworkCenter.OnNetworkChangedListener, IBaseView {

    //activity是否处于前台
    var isActivityReady: Boolean = false;
    var mContentView: View? = null
    var immersionBar: ImmersionBar? = null

    private lateinit var loadService: LoadService<Any>
    private var mTVEmpty: TextView? = null
    private var mTVTimeOut: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //为activity指定全屏的主题，隐藏掉actionbar
        setTheme(R.style.base_common_Theme)
        //加载布局
        mContentView = LayoutInflater.from(this).inflate(resourceLayout(), null);
        setContentView(mContentView!!)
        ARouter.getInstance().inject(this)
        //注册网络监听
        NetworkCenter.register(this, this)

        //沉浸式处理
        //沉浸式处理
        if (useImmersionBar()) {
            initImmersionBar()
        }

        //多布局初始化
        //多布局初始化
        initLoadSir(initStatusLayout())

        initView()

        initData()
    }

    //是否使用沉浸式状态栏，默认不使用
    protected open fun useImmersionBar(): Boolean {
        return false
    }

    //初始化沉浸式状态栏
    protected open fun initImmersionBar() { //@link https://github.com/gyf-dev/ImmersionBar
        immersionBar = ImmersionBar.with(this);
        immersionBar!!.transparentStatusBar().statusBarColor(R.color.base_common_color_fafafa)
            .statusBarDarkFont(true)
            .keyboardEnable(true)//解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
            .navigationBarWithKitkatEnable(false)//是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
            .init()
    }

    //初始化LoadSir
    private fun initLoadSir(view: View?) {

        var sir:LoadSir = LoadSir.getDefault()

        loadService = sir.register(view ?: this, object : Callback.OnReloadListener {
                override fun onReload(v: View?) {
                    if(v!=null){

                    }
                }
            }, object : Convertor<StateEntity> {
                override fun map(it: StateEntity?): Class<out Callback> {

                    //默认是success
                    var result: Class<out Callback?> = SuccessCallback::class.java

                    when (it?.state) {
                        //显示成功页面 --> 原始页面
                        StateEntity.StateCode.SUCCESS_LAYOUT -> {
                            result = SuccessCallback::class.java
                        }

                        //显示空数据页面 --> OnEmptyStateCallback
                        StateEntity.StateCode.EMPTY_LAYOUT -> {
                            result = OnEmptyStateCallback::class.java
                            //修改文字
                            if (!TextUtils.isEmpty(it.message)) {
                                mTVEmpty?.text = it.message
                            }
                        }

                        //显示占位图页面 --> onPlaceHolderCallback
                        StateEntity.StateCode.PLACEHOLDER_LAYOUT -> {
                            result = OnPlaceHolderCallback::class.java
                        }

                        //显示连接超时页面 --> onTimeoutStateLayout
                        StateEntity.StateCode.TIMEOUT_LAYOUT -> {
                            result = OnTimeoutStateCallback::class.java
                            if (!TextUtils.isEmpty(it.message)) {
                                mTVTimeOut?.text = it.message
                            }
                        }
                    }

                    return result
                }
            })

        //动态修改callback
        loadService.setCallBack(OnEmptyStateCallback::class.java,object :Transport{
            override fun order(context: Context?, view: View?) {
                mTVEmpty = view?.findViewById(R.id.tv_empty)
            }
        })

        loadService.setCallBack(OnTimeoutStateCallback::class.java,object :Transport{
            override fun order(context: Context?, view: View?) {
                mTVTimeOut = view?.findViewById(R.id.tv_timeout)
            }
        })
    }

    @Deprecated("")
    fun getLoadService(): LoadService<*>? {
        return loadService
    }

    //显示加载成功布局
    protected open fun showSuccessLayout() {
        loadService.showWithConvertor(StateEntity(1))
    }

    //显示空数据页面
    protected open fun showEmptyLayout(message: String?) {
        loadService.showWithConvertor(StateEntity(2, message))
    }

    //显示PlaceHolder页面
    protected open fun showPlaceHolderLayout() {
        loadService.showWithConvertor(StateEntity(3))
    }

    //显示连接超时页面数据
    protected open fun showTimeoutLayout(message: String?) {
        loadService.showWithConvertor(StateEntity(4, message))
    }


    //注册多状态缺省页面
    abstract fun initStatusLayout(): View?

    abstract fun resourceLayout(): Int

    abstract fun initView()

    abstract fun initData()

    override fun onStart() {
        super.onStart()
        isActivityReady = true
    }

    override fun onStop() {
        super.onStop()
        isActivityReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        //取消注册网络变化
        NetworkCenter.unRegister(this)
    }

    //activity基类提供一个默认的网络变化处理，子类自行覆写
    override fun onNetworkChanged(isConnected: Boolean) {
        //当前activity处于前台时，提示用户
        if (isActivityReady) {
            //todo 弹出提示网络变化

        }
    }

    override fun getContext(): Context {
        return this
    }


}