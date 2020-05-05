package com.cj.base_common.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.base_common.model
 * 奶 实体
 */
@Entity
data class MilkEntity(
    @Id
    var id: Long = 0,//喂奶id
    var type: Int = 0,//喂奶类型 10：母乳 20：奶粉
    var startTime: String = "",//喂奶开始时间
    var endTime: String = "",//喂奶结束时间
    var capacity: Double = 0.00,//喂奶量 （单位：ml)
    var remark: String = ""//备注
):Serializable