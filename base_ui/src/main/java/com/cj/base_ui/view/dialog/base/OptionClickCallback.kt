package com.cj.base_ui.view.dialog.base

/**
 * Author:chris - jason
 * Date:2020-02-11.
 * Package:com.cj.base_ui.view.dialog.base
 */
interface OptionClickCallback:BaseDialogViewCallback {
   fun onOptionClick(option:String,position:Int)
}