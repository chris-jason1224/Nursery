package com.cj.base_ui.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.TextView
import com.cj.base_ui.R
import com.cj.base_ui.view.dialog.base.BaseDialogView
import com.cj.base_ui.view.dialog.base.SingleBtnCallback
import org.jetbrains.annotations.NotNull

/**
 * Author:chris - jason
 * Date:2020-02-04.
 * Package:com.cj.base_ui.view.dialog
 * 带有单行文案 + 一个按钮，用于展示一般提示信息，无需用户确认操作
 */
class MessageDialogView(@NotNull context: Context, content: String = "", btn: String = "", mCallback: SingleBtnCallback? = null) : BaseDialogView(context) {

    private lateinit var mTVContent: TextView
    private lateinit var mTVConfirm: TextView
    private var mCallback: SingleBtnCallback? = mCallback

    init {
        mTVContent.text = content
        mTVConfirm.text = btn
    }

    override fun setDialogLayout(): Int {
        return R.layout.base_ui_message_dialog_view_layout
    }

    override fun bindView(view: View) {
        mTVContent = view.findViewById(R.id.tv_content)
        mTVConfirm = view.findViewById(R.id.rtv_confirm_btn)
        mTVConfirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rtv_confirm_btn -> {
                mCallback?.onPositive(v)
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        mCallback?.onDismiss()
    }


}
