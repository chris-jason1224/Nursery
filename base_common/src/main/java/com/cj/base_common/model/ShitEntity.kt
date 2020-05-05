package com.cj.base_common.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.base_common.model
 * 大便 实体
 */
@Entity
data class ShitEntity(
    @Id
    var id: Long = 0,//大便id
    var createDate:String ="",//记录时间
    var type: Int = 0,//大便干度类型 10：干便 20：半干便 30：稀便 40：水样便
    var hasDebris: Boolean = false,//是否有奶瓣 true：有 false：无
    var hasMucus: Boolean = false,//是否有粘液 true：有 false：无
    var color: Int = 0,//大便颜色 10：金黄色（正常便便）20：绿色（轻度消化不良）30：黑色（重度消化不良）40：其他颜色（未知状态）
    var sub:String="",//大便性状描述 正常便便、轻度消化不良、重度消化不良、未知状态
    var smell: Int = 0,//大便气味 10：正常 20：酸臭 30：纯臭
    //var pics: List<String> = mutableListOf()//图片集合
    var remark: String = ""//备注
):Serializable