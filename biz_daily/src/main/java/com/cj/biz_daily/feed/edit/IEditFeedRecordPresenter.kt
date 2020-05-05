package com.cj.biz_daily.feed.edit

import com.cj.base_common.model.MilkEntity

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.biz_daily.feed.create
 */
interface IEditFeedRecordPresenter {
    fun submitRecord(milk:MilkEntity?)
}