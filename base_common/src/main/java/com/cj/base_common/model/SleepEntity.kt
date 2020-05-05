package com.cj.base_common.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.base_common.model
 * 睡眠实体
 */
@Entity
data class SleepEntity(
    @Id
    var id:Long=0,//睡眠id
    var type:Int=0,//睡眠方式 10:奶睡 20：哄睡 30：自然睡
    var startTime:String="",//开始时间
    var endTime:String="",//结束时间
    var remark:String=""//描述
)