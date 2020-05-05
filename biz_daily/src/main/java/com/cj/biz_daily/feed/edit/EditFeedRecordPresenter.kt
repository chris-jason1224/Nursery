package com.cj.biz_daily.feed.edit

import com.cj.base_common.base.BaseApp
import com.cj.base_common.model.MilkEntity
import com.cj.base_common.mvp.BaseMVPPresenter
import io.objectbox.Box

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.biz_daily.feed.create
 */
class EditFeedRecordPresenter:BaseMVPPresenter<IEditFeedRecordView>() ,IEditFeedRecordPresenter{

    private var milkStore: Box<MilkEntity> = BaseApp.ObjectBox.boxStore.boxFor(MilkEntity::class.java)

    override fun submitRecord(milk: MilkEntity?) {
        val id = milkStore.put(milk)
        if(mView!=null){
            mView?.onSubmitResult(id>0)
        }
    }

}