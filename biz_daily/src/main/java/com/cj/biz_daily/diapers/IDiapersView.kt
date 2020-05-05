package com.cj.biz_daily.diapers

import com.cj.base_common.base.IBaseView
import com.cj.base_common.model.DiapersEntity

/**
 * Author:chris - jason
 * Date:2020/4/30.
 * Package:com.cj.biz_daily.diapers
 */
interface IDiapersView:IBaseView {
    fun onDiapersRecordGet(data:List<DiapersEntity>)

    fun finishRefreshAndLoadMore()

}