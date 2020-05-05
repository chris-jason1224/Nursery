@file:JvmName("KeyTag")
package com.cj.base_common.`var`

/**
 * Author:chris - jason
 * Date:2019-12-19.
 * Package:com.cj.base_common.`var`
 *
 */
//页面路由中包含此标识符，表示需登录后访问
val TAG_USER_NEED_LOGIN: String = "/USR/"

//本地广播传递登录结果时intent的key
val TAG_LOGIN_RESULT: String = "TAG_LOGIN_RESULT"

//本地广播传递登录结果时的intent filter
val TAG_LOGIN_FILTER: String = "TAG_LOGIN_FILTER"

//存储在本地的token
val TAG_USER_TOKEN: String = "TAG_USER_TOKEN"

//存储在本地的连接过的蓝牙设备的mac地址
val TAG_LINKED_BT_DEVICE_MAC_ADDRESS: String = "TAG_LINKED_BT_DEVICE_MAC_ADDRESS"