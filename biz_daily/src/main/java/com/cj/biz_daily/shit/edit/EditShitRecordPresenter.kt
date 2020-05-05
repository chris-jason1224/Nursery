package com.cj.biz_daily.shit.edit

import com.cj.base_common.base.BaseApp
import com.cj.base_common.model.MilkEntity
import com.cj.base_common.model.ShitEntity
import com.cj.base_common.mvp.BaseMVPPresenter
import io.objectbox.Box

/**
 * Author:chris - jason
 * Date:2020/4/27.
 * Package:com.cj.biz_daily.shit.edit
 */
class EditShitRecordPresenter:BaseMVPPresenter<IEditShitRecordView>(),IEditShitRecordPresenter {
    private var shitStore: Box<ShitEntity> = BaseApp.ObjectBox.boxStore.boxFor(ShitEntity::class.java)

    override fun submitRecord(shit: ShitEntity?) {
        val id = shitStore.put(shit)
        if(mView!=null){
            mView?.onSubmitResult(id>0)
        }
    }
}