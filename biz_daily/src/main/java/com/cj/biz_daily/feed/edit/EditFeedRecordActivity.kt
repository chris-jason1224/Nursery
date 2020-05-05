package com.cj.biz_daily.feed.edit

import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.cj.base_common.bus.ModuleBus
import com.cj.base_common.model.MilkEntity
import com.cj.base_common.mvp.BaseMVPActivity
import com.cj.base_ui.view.dialog.BottomOptionsView
import com.cj.base_ui.view.dialog.MessageDialogView
import com.cj.base_ui.view.dialog.base.OptionClickCallback
import com.cj.base_ui.view.dialog.base.SingleBtnCallback
import com.cj.biz_daily.R
import com.cj.bus.Gen_biz_daily_Interface
import kotlinx.android.synthetic.main.biz_daily_activity_edit_feed_record.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * 编辑或修改喂养记录
 */
@Route(path = "/biz_daily/ACT/com.cj.biz_daily.feed.create.EditFeedRecordActivity")
class EditFeedRecordActivity : BaseMVPActivity<EditFeedRecordPresenter>(), IEditFeedRecordView {

    private lateinit var sdf: SimpleDateFormat
    private var milkEntity: MilkEntity? = null

    override fun createPresenter(): EditFeedRecordPresenter {
        return EditFeedRecordPresenter()
    }

    override fun initStatusLayout(): View? {
        return rl_content
    }

    override fun resourceLayout(): Int {
        return R.layout.biz_daily_activity_edit_feed_record
    }

    override fun initView() {
        toolbar.setOnClickListener(this)
        rl_feed_type.setOnClickListener(this)
        rl_feed_start.setOnClickListener(this)
        rl_feed_end.setOnClickListener(this)
        rtv_submit.setOnClickListener(this)
    }

    override fun initData() {
        sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        if (intent != null) {
            milkEntity = intent.getSerializableExtra("milk_record_entity") as MilkEntity?
        }

        //初始化数据
        if (milkEntity != null) {
            tv_feed_type.text = when (milkEntity?.type) {
                10 -> "母乳"
                else -> "奶粉"
            }

            tv_feed_start.text = milkEntity?.startTime
            tv_feed_end.text = milkEntity?.endTime

            if(milkEntity?.capacity!! > 0 ){
                et_capacity.setText("${milkEntity?.capacity}")
            }else{
                et_capacity.setText("")
            }

        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar -> finish()

            //选择喂奶方式
            R.id.rl_feed_type -> {
                val options = arrayListOf<String>("母乳", "奶粉")
                val view = BottomOptionsView(
                    this@EditFeedRecordActivity,
                    "喂奶方式",
                    options,
                    object : OptionClickCallback {
                        override fun onOptionClick(option: String, position: Int) {
                            tv_feed_type.text = option
                        }

                        override fun onDismiss() {
                        }
                    })
                view.show()
            }

            //选择喂奶开始时间
            R.id.rl_feed_start -> {
                TimePickerBuilder(this@EditFeedRecordActivity, object : OnTimeSelectListener {
                    override fun onTimeSelect(date: Date?, v: View?) {
                        tv_feed_start.text = sdf.format(date)
                    }
                }).setType(booleanArrayOf(true, true, true, true, true, true)).build().show()
            }

            //选择喂奶结束时间
            R.id.rl_feed_end -> {
                TimePickerBuilder(this@EditFeedRecordActivity, object : OnTimeSelectListener {
                    override fun onTimeSelect(date: Date?, v: View?) {
                        tv_feed_end.text = sdf.format(date)
                    }
                }).setType(booleanArrayOf(true, true, true, true, true, true)).build().show()
            }

            //提交记录
            R.id.rtv_submit -> {
                if (verify()) {

                    milkEntity?.startTime = tv_feed_start.text.toString()

                    milkEntity?.endTime = tv_feed_end.text.toString()

                    if (TextUtils.isEmpty(et_capacity.text.toString())) {
                        milkEntity?.capacity = 0.0
                    } else {
                        milkEntity?.capacity = et_capacity.text.toString()?.toDouble()
                    }

                    mPresenter?.submitRecord(milkEntity)
                }
            }

        }
    }

    private fun verify(): Boolean {

        if (TextUtils.isEmpty(tv_feed_type.text)) {
            MessageDialogView(this, "请选择喂奶方式", "确定", null).show()
            return false
        }

        if (milkEntity == null) {
            milkEntity = MilkEntity()
        }

        milkEntity?.type = when (tv_feed_type.text) {
            "母乳" -> 10
            else -> 20
        }


        if (TextUtils.isEmpty(tv_feed_start.text)) {
            MessageDialogView(this, "请选择喂奶开始时间", "确定", null).show()
            return false
        }

        if (TextUtils.isEmpty(tv_feed_end.text)) {
            MessageDialogView(this, "请选择喂奶结束时间", "确定", null).show()
            return false
        }

        if (sdf.parse(tv_feed_start.text.toString()).time >= sdf.parse(tv_feed_end.text.toString()).time) {
            MessageDialogView(this, "喂奶结束时间不能早于开始时间", "确定", null).show()
            return false
        }

        if (milkEntity?.type == 20 && TextUtils.isEmpty(et_capacity.text)) {
            MessageDialogView(this, "请填写喂奶量", "确定", null).show()
            return false
        }

        return true
    }

    override fun onSubmitResult(result: Boolean) {
        if (result) {
            MessageDialogView(this, "提交成功", "确定", object : SingleBtnCallback {

                override fun onPositive(view: View) {
                    finish()
                    //发送消息，通知提交成功
                    ModuleBus.of(Gen_biz_daily_Interface::class.java)
                        ?.Gen_submitFeedRecordResult_Method()?.post(true)
                }

                override fun onDismiss() {

                }

            }).show()
        }else{
            MessageDialogView(this, "提交失败", "请重试", object : SingleBtnCallback {

                override fun onPositive(view: View) {

                }

                override fun onDismiss() {

                }

            }).show()
        }
    }
}
