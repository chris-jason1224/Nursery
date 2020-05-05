package com.cj.biz_daily.shit.edit

import com.cj.base_common.model.ShitEntity

/**
 * Author:chris - jason
 * Date:2020/4/27.
 * Package:com.cj.biz_daily.shit.edit
 */
interface IEditShitRecordPresenter {
    fun submitRecord(shit:ShitEntity?)
}