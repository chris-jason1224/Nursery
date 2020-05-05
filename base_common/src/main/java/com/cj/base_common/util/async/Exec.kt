package com.cj.base_common.util.async

/**
 * Author:chris - jason
 * Date:2019-12-17.
 * Package:com.cj.base_common.util.async
 * 异步任务约束接口
 */
interface Exec<T> {
    fun execute():T
}