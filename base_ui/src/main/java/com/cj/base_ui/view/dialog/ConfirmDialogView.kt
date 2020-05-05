package com.cj.base_ui.view.dialog

/**
 * Author:chris - jason
 * Date:2020-02-11.
 * Package:com.cj.base_ui.view.dialog
 */

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.TextView
import com.cj.base_ui.R
import com.cj.base_ui.view.dialog.base.BaseDialogView
import com.cj.base_ui.view.dialog.base.DoubleBtnCallback
import org.jetbrains.annotations.NotNull

class ConfirmDialogView(@NotNull context:Context, content:String="", negative:String="", positive:String="", callback:DoubleBtnCallback?=null) : BaseDialogView(context) {

    private lateinit var mTVContent:TextView
    private lateinit var mTVNegative:TextView
    private lateinit var mTVPositive:TextView
    private var mCallback:DoubleBtnCallback?=callback

    init{
        mTVContent.text = content
        mTVNegative.text = negative
        mTVPositive.text = positive
    }

    override fun setDialogLayout(): Int {
        return R.layout.base_ui_confirm_dialog_view_layout
    }

    override fun bindView(root: View) {
        mTVContent = root.findViewById(R.id.tv_content)
        mTVNegative = root.findViewById(R.id.rtv_negative_btn)
        mTVPositive = root.findViewById(R.id.rtv_positive_btn)

        mTVNegative.setOnClickListener(this)
        mTVPositive.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v!!.id){
            R.id.rtv_negative_btn -> mCallback?.onNegative(v)
            R.id.rtv_positive_btn -> mCallback?.onPositive(v)
        }

        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        mCallback?.onDismiss()
    }

}
