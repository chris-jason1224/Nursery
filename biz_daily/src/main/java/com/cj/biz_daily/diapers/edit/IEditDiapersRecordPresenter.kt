package com.cj.biz_daily.diapers.edit

import com.cj.base_common.model.DiapersEntity

/**
 * Author:chris - jason
 * Date:2020/5/3.
 * Package:com.cj.biz_daily.diapers.edit
 */
interface IEditDiapersRecordPresenter {
    fun submitRecord(diapers:DiapersEntity?)
}