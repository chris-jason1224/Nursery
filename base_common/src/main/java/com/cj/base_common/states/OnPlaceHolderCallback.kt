package com.cj.base_common.states

import android.content.Context
import android.view.View
import com.cj.base_common.R
import com.kingja.loadsir.callback.Callback

/**
 * Author:chris - jason
 * Date:2020/4/26.
 * Package:com.cj.base_common.states
 */
class OnPlaceHolderCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.base_common_on_place_holder_state_layout
    }

}