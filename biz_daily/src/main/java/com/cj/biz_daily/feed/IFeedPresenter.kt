package com.cj.biz_daily.feed

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.biz_daily.feed
 */
interface IFeedPresenter {

    //分页查询喂养记录
    fun findFeedRecordByPage(pageIndex:Long)

}