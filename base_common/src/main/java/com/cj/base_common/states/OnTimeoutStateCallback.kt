package com.cj.base_common.states

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.View
import com.cj.base_common.R
import com.kingja.loadsir.callback.Callback

/**
 * Author:chris - jason
 * Date:2020/4/26.
 * Package:com.cj.base_common.states
 */
class OnTimeoutStateCallback: Callback() {
    override fun onCreateView(): Int {
        return R.layout.base_common_on_timeout_state_layout
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        view?.findViewById<View>(R.id.base_common_ll_timeout)?.setOnClickListener {
                //直接进入手机中设置界面
                context!!.startActivity(Intent(Settings.ACTION_SETTINGS))
            }

        return super.onReloadEvent(context, view)
    }
}