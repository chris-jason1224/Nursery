package com.cj.biz_daily.diapers.edit

import com.cj.base_common.base.BaseApp
import com.cj.base_common.model.DiapersEntity
import com.cj.base_common.mvp.BaseMVPPresenter
import io.objectbox.Box

/**
 * Author:chris - jason
 * Date:2020/5/3.
 * Package:com.cj.biz_daily.diapers.edit
 */
class EditDiapersRecordPresenter :BaseMVPPresenter<IEditDiapersRecordView>(),IEditDiapersRecordPresenter{

    private var diapersStore: Box<DiapersEntity> = BaseApp.ObjectBox.boxStore.boxFor(DiapersEntity::class.java)

    override fun submitRecord(diapers: DiapersEntity?) {
        val id = diapersStore.put(diapers)
        if(mView!=null){
            mView?.onSubmitResult(id>0)
        }
    }
}