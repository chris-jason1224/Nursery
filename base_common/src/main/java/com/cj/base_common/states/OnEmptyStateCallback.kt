package com.cj.base_common.states

import com.cj.base_common.R
import com.kingja.loadsir.callback.Callback

/**
 * Author:chris - jason
 * Date:2020/4/26.
 * Package:com.cj.base_common.states
 */
class OnEmptyStateCallback: Callback() {
    override fun onCreateView(): Int {
        return R.layout.base_common_on_empty_state_layout
    }
}