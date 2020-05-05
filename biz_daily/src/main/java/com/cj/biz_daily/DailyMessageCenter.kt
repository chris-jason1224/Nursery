package com.cj.biz_daily

import com.cj.annotations.bus.EventRegister
import com.cj.annotations.bus.ModuleEventCenter

/**
 * Author:chris - jason
 * Date:2020/4/27.
 * Package:com.cj.biz_daily
 */
@ModuleEventCenter
class DailyMessageCenter {

    //提交喂奶记录结果
    @EventRegister(type = Boolean::class)
    var submitFeedRecordResult:Boolean = false

    //提交拉粑粑记录结果
    @EventRegister(type = Boolean::class)
    var submitShitRecordResult:Boolean = false

    //提交换纸尿裤记录结果
    @EventRegister(type = Boolean::class)
    var submitDiapersRecordResult:Boolean = false

}