package com.cj.biz_daily.shit

import com.cj.base_common.base.IBaseView
import com.cj.base_common.model.MilkEntity
import com.cj.base_common.model.ShitEntity

/**
 * Author:chris - jason
 * Date:2020/4/27.
 * Package:com.cj.biz_daily.shit
 */
interface IShitView:IBaseView {
    fun onShitRecordGet(list:List<ShitEntity>)

    fun finishRefreshAndLoadMore()
}