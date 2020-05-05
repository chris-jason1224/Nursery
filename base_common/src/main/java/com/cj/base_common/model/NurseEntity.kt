package com.cj.base_common.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.base_common.model
 * 护理实体
 */
@Entity
data class NurseEntity(
    @Id
    var id: Long = 0,//护理id
    var type: Int = 0,//护理类型 10：洗澡 20：推拿按摩 30：排气操 40：其他
    var startTime: String = "",//开始时间
    var endTime: String = "",//结束时间
    var remark: String = ""//备注
)