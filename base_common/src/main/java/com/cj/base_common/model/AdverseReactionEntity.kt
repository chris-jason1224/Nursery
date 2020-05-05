package com.cj.base_common.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.base_common.model
 * 不良反应
 */
@Entity
data class AdverseReactionEntity(
    @Id
    var id: Long = 0,//不良反应id
    var type: Int = 0,//不良反应类型 10：拉肚子 20：咳嗽 30：吐奶溢奶 40：发烧 50：胀气积食 60：其他
    var startTime: String = "",//开始时间
    var endTime: String = "",//结束时间
    var remark: String = ""//备注
)
