package com.cj.biz_daily.feed

import com.cj.base_common.`var`.pageSize
import com.cj.base_common.base.BaseApp
import com.cj.base_common.model.MilkEntity
import com.cj.base_common.model.MilkEntity_
import com.cj.base_common.mvp.BaseMVPPresenter
import io.objectbox.Box

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.biz_daily.feed
 */
class FeedPresenter : BaseMVPPresenter<IFeedView>(), IFeedPresenter {

    private var milkStore: Box<MilkEntity> =
        BaseApp.ObjectBox.boxStore.boxFor(MilkEntity::class.java)

    override fun findFeedRecordByPage(pageIndex: Long) {
        val list = milkStore
            .query()
            .orderDesc(MilkEntity_.endTime)
            .build()
            .find((pageIndex - 1) * pageSize, pageSize)

        mView?.onFeedRecordGet(list)
        mView?.finishRefreshAndLoadMore()
    }


}