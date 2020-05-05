package com.cj.biz_daily.shit

import com.cj.base_common.`var`.pageSize
import com.cj.base_common.base.BaseApp

import com.cj.base_common.model.ShitEntity
import com.cj.base_common.model.ShitEntity_
import com.cj.base_common.mvp.BaseMVPPresenter
import io.objectbox.Box

/**
 * Author:chris - jason
 * Date:2020/4/27.
 * Package:com.cj.biz_daily.shit
 */
class ShitPresenter:BaseMVPPresenter<IShitView>() ,IShitPresenter{

    private var shitStore: Box<ShitEntity> = BaseApp.ObjectBox.boxStore.boxFor(ShitEntity::class.java)

    override fun findShitRecordByPage(pageIndex: Long) {
        val list = shitStore
            .query()
            .orderDesc(ShitEntity_.createDate)
            .build()
            .find((pageIndex - 1) * pageSize, pageSize)

        mView?.onShitRecordGet(list)
        mView?.finishRefreshAndLoadMore()
    }
}