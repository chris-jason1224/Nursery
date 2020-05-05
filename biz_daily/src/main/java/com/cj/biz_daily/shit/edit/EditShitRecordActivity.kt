package com.cj.biz_daily.shit.edit

import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.cj.base_common.bus.ModuleBus
import com.cj.base_common.model.ShitEntity
import com.cj.base_common.mvp.BaseMVPActivity
import com.cj.base_ui.view.dialog.BottomOptionsView
import com.cj.base_ui.view.dialog.MessageDialogView
import com.cj.base_ui.view.dialog.base.OptionClickCallback
import com.cj.base_ui.view.dialog.base.SingleBtnCallback
import com.cj.biz_daily.R
import com.cj.bus.Gen_biz_daily_Interface
import kotlinx.android.synthetic.main.biz_daily_activity_edit_shit_record.*
import kotlinx.android.synthetic.main.biz_daily_activity_edit_shit_record.iv_back
import kotlinx.android.synthetic.main.biz_daily_activity_edit_shit_record.rl_content
import kotlinx.android.synthetic.main.biz_daily_activity_edit_shit_record.rtv_submit
import java.text.SimpleDateFormat
import java.util.*

@Route(path = "/biz_daily/ACT/com.cj.biz_daily.shit.edit.EditShitActivity")
class EditShitRecordActivity : BaseMVPActivity<EditShitRecordPresenter>(), IEditShitRecordView {

    private var shit: ShitEntity? = null
    private lateinit var sdf: SimpleDateFormat

    override fun createPresenter(): EditShitRecordPresenter {
        return EditShitRecordPresenter()
    }

    override fun initStatusLayout(): View? {
        return rl_content
    }

    override fun resourceLayout(): Int {
        return R.layout.biz_daily_activity_edit_shit_record
    }

    override fun initView() {
        iv_back.setOnClickListener(this)
        rl_shit_time.setOnClickListener(this)
        rl_shit_type.setOnClickListener(this)
        rl_shit_color.setOnClickListener(this)
        rl_shit_smell.setOnClickListener(this)
        rl_debris.setOnClickListener(this)
        rl_mucus.setOnClickListener(this)
        rtv_submit.setOnClickListener(this)
    }

    override fun initData() {
        sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        if (intent != null) {
            if (intent.getSerializableExtra("shit_record_entity") != null) {
                shit = intent.getSerializableExtra("shit_record_entity") as ShitEntity?
            }
        }

        //初始化数据
        if (shit != null) {

            tv_shit_time.text = shit?.createDate

            //10：干便 20：半干便 30：稀便 40：水样便
            tv_shit_type.text = when (shit?.type) {
                10 -> "干便"
                20 -> "半干便"
                30 -> "稀便"
                else -> "水样便"
            }

            //10：金黄色（正常）20：绿色（消化不良）30：黑色（重度消化不良）40：其他颜色（未知状态）
            tv_shit_color.text = when (shit?.color) {
                10 -> "金黄色"
                20 -> "绿色"
                30 -> "黑色"
                else -> "其他颜色"
            }

            //10：正常 20：酸臭 30：纯臭
            tv_shit_smell.text = when (shit?.smell) {
                10 -> "正常"
                20 -> "酸臭"
                else -> "纯臭"
            }

            tv_debris.text = when (shit?.hasDebris) {
                true -> "有"
                else -> "无"
            }

            tv_mucus.text = when (shit?.hasMucus) {
                true -> "有"
                else -> "无"
            }

        }


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()

            //选择粑粑时间
            R.id.rl_shit_time -> {
                TimePickerBuilder(this@EditShitRecordActivity, object : OnTimeSelectListener {
                    override fun onTimeSelect(date: Date?, v: View?) {
                        var t = sdf.format(date)
                        tv_shit_time.text = t.substring(0, t.length - 3)
                    }
                }).setType(booleanArrayOf(true, true, true, true, true, false)).build().show()
            }

            //选择粑粑类型
            R.id.rl_shit_type -> {
                val types = arrayListOf<String>("干便", "半干便", "稀便", "水样便")
                val view = BottomOptionsView(
                    this@EditShitRecordActivity,
                    "粑粑类型",
                    types,
                    object : OptionClickCallback {
                        override fun onOptionClick(option: String, position: Int) {
                            tv_shit_type.text = option
                        }

                        override fun onDismiss() {
                        }
                    })
                view.show()
            }

            //选择粑粑颜色
            R.id.rl_shit_color -> {
                val colors = arrayListOf<String>("金黄色", "绿色", "黑色", "其他颜色")
                val view = BottomOptionsView(
                    this@EditShitRecordActivity,
                    "粑粑颜色",
                    colors,
                    object : OptionClickCallback {
                        override fun onOptionClick(option: String, position: Int) {
                            tv_shit_color.text = option
                        }

                        override fun onDismiss() {
                        }
                    })
                view.show()
            }

            //选择粑粑气味
            R.id.rl_shit_smell -> {
                val smells = arrayListOf<String>("正常", "酸臭", "纯臭")
                val view = BottomOptionsView(
                    this@EditShitRecordActivity,
                    "粑粑气味",
                    smells,
                    object : OptionClickCallback {
                        override fun onOptionClick(option: String, position: Int) {
                            tv_shit_smell.text = option
                        }

                        override fun onDismiss() {
                        }
                    })
                view.show()
            }

            //选择有无奶瓣
            R.id.rl_debris -> {
                val debris = arrayListOf<String>("有", "无")
                val view = BottomOptionsView(
                    this@EditShitRecordActivity,
                    "有无奶瓣",
                    debris,
                    object : OptionClickCallback {
                        override fun onOptionClick(option: String, position: Int) {
                            tv_debris.text = option
                        }

                        override fun onDismiss() {
                        }
                    })
                view.show()
            }

            //选择有无粘液
            R.id.rl_mucus -> {
                val mucus = arrayListOf<String>("有", "无")
                val view = BottomOptionsView(
                    this@EditShitRecordActivity,
                    "有无粘液",
                    mucus,
                    object : OptionClickCallback {
                        override fun onOptionClick(option: String, position: Int) {
                            tv_mucus.text = option
                        }

                        override fun onDismiss() {
                        }
                    })
                view.show()
            }

            //提交保存
            R.id.rtv_submit -> {
                if (verify()) {
                    mPresenter?.submitRecord(shit)
                }
            }

        }
    }

    private fun verify(): Boolean {

        //校验参数
        if (TextUtils.isEmpty(tv_shit_time.text.toString())) {
            MessageDialogView(this, "请选择粑粑时间", "确定", null).show()
            return false
        }

        //赋值
        if (shit == null) {
            shit = ShitEntity()
        }

        shit?.createDate = tv_shit_time.text.toString()

        shit?.type = when (tv_shit_type.text.toString()) {
            "干便" -> 10
            "半干便" -> 20
            "稀便" -> 30
            else -> 40
        }

        shit?.color = when (tv_shit_color.text.toString()) {
            "金黄色" -> {
                shit?.sub = "正常便便"
                10
            }
            "绿色" -> {
                shit?.sub = "轻度消化不良"
                20
            }
            "黑色" -> {
                shit?.sub = "重度消化不良"
                30
            }
            else -> {
                shit?.sub = "未知状态"
                40
            }
        }

        shit?.smell = when (tv_shit_smell.text.toString()) {
            "正常" -> 10
            "酸臭" -> 20
            else -> 30
        }

        shit?.hasDebris = when (tv_debris.text.toString()) {
            "有" -> true
            else -> false
        }

        shit?.hasMucus = when (tv_mucus.text.toString()) {
            "有" -> true
            else -> false
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
                        ?.Gen_submitShitRecordResult_Method()?.post(true)
                }

                override fun onDismiss() {

                }

            }).show()
        } else {
            MessageDialogView(this, "提交失败", "请重试", object : SingleBtnCallback {

                override fun onPositive(view: View) {

                }

                override fun onDismiss() {

                }

            }).show()
        }
    }


}
