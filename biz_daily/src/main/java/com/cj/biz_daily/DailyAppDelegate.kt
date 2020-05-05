package com.cj.biz_daily

import android.content.Context
import com.cj.annotations.module.ModuleRegister
import com.cj.base_common.log.LOG
import com.cj.base_common.module.IModuleDelegate

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.biz_daily
 */
@ModuleRegister
class DailyAppDelegate:IModuleDelegate {
    override fun onCreate(context: Context) {
        LOG.log_d("biz_daily.onCreate()")
    }

    override fun enterForeground() {
        LOG.log_d("biz_daily.enterForeground()")
    }

    override fun enterBackground() {
        LOG.log_d("biz_daily.enterBackground()")
    }
}