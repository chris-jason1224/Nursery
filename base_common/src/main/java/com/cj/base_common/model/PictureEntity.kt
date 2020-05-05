package com.cj.base_common.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Author:chris - jason
 * Date:2020/4/27.
 * Package:com.cj.base_common.model
 */
@Entity
data class PictureEntity(
    @Id
    var id:Long,
    var prefix:String="",//图片前缀
    var bizID:Long=0,//关联业务id
    var path:String="",//图片地址
    var createDate:String="",//创建时间
    var width:Int=0,//宽度
    var height:Int=0//高度
)