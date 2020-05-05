package com.cj.biz_daily.feed

import com.cj.base_common.base.IBaseView
import com.cj.base_common.model.MilkEntity

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.biz_daily.feed
 */
interface IFeedView:IBaseView {
    fun onFeedRecordGet(list:List<MilkEntity>)

    fun finishRefreshAndLoadMore()
}
