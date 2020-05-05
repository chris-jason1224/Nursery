package com.cj.biz_daily.diapers

import com.cj.base_common.`var`.pageSize
import com.cj.base_common.base.BaseApp
import com.cj.base_common.model.DiapersEntity
import com.cj.base_common.model.DiapersEntity_
import com.cj.base_common.mvp.BaseMVPPresenter
import io.objectbox.Box

/**
 * Author:chris - jason
 * Date:2020/4/30.
 * Package:com.cj.biz_daily.diapers
 */
class DiapersPresenter : BaseMVPPresenter<IDiapersView>(), IDiapersPresenter {

    private var diapersStore: Box<DiapersEntity> =
        BaseApp.ObjectBox.boxStore.boxFor(DiapersEntity::class.java)

    override fun findDiapersRecordByPage(pageIndex: Long) {
        val list = diapersStore
            .query()
            .orderDesc(DiapersEntity_.createDate)
            .build()
            .find((pageIndex - 1) * pageSize, pageSize)

        mView?.onDiapersRecordGet(list)
        mView?.finishRefreshAndLoadMore()
    }


}