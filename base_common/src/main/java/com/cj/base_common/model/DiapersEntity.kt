package com.cj.base_common.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

/**
 * Author:chris - jason
 * Date:2020/4/25.
 * Package:com.cj.base_common.model
 * 尿不湿 实体
 */
@Entity
data class DiapersEntity(
    @Id
    var id:Long= 0,//尿不湿id
    var brand:String="",//品牌
    var size:String="",//尺寸 S 、M、 L
    var createDate:String=""
):Serializable