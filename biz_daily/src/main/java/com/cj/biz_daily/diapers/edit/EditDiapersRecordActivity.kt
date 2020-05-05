package com.cj.biz_daily.diapers.edit

import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.cj.base_common.bus.ModuleBus
import com.cj.base_common.model.DiapersEntity
import com.cj.base_common.mvp.BaseMVPActivity
import com.cj.base_ui.view.dialog.BottomOptionsView
import com.cj.base_ui.view.dialog.MessageDialogView
import com.cj.base_ui.view.dialog.base.OptionClickCallback
import com.cj.base_ui.view.dialog.base.SingleBtnCallback
import com.cj.biz_daily.R
import com.cj.bus.Gen_biz_daily_Interface
import kotlinx.android.synthetic.main.biz_daily_activity_edit_diapers_record.*
import kotlinx.android.synthetic.main.biz_daily_activity_edit_diapers_record.rtv_submit
import kotlinx.android.synthetic.main.biz_daily_activity_edit_diapers_record.toolbar
import java.text.SimpleDateFormat
import java.util.*


@Route(path = "/biz_daily/ACT/com.cj.biz_daily.diapers.edit.EditDiapersRecordActivity")
class EditDiapersRecordActivity : BaseMVPActivity<EditDiapersRecordPresenter>(),IEditDiapersRecordView{
    private lateinit var sdf: SimpleDateFormat
    private var diapersEntity: DiapersEntity? = null

    override fun createPresenter(): EditDiapersRecordPresenter {
        return EditDiapersRecordPresenter()
    }

    override fun initStatusLayout(): View? {
        return null
    }

    override fun resourceLayout(): Int {
       return R.layout.biz_daily_activity_edit_diapers_record
    }

    override fun initView() {
        toolbar.setOnClickListener(this)
        rl_brand.setOnClickListener(this)
        rl_size.setOnClickListener(this)
        rl_time.setOnClickListener(this)
        rtv_submit.setOnClickListener(this)
    }

    override fun initData() {
        sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        if (intent != null) {
            diapersEntity = intent.getSerializableExtra("diapers_record_entity") as DiapersEntity?
        }

        //初始化数据
        if(diapersEntity!=null){
            tv_brand.text = diapersEntity?.brand
            tv_size.text = diapersEntity?.size
            tv_time.text = diapersEntity?.createDate
        }


    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.toolbar -> finish()

            //选择纸尿裤品牌
            R.id.rl_brand -> {
                val options = arrayListOf<String>("妮飘", "尤妮佳","米菲","帮宝适","花王","未知品牌")
                val view = BottomOptionsView(
                    this@EditDiapersRecordActivity,
                    "纸尿裤品牌",
                    options,
                    object : OptionClickCallback {
                        override fun onOptionClick(option: String, position: Int) {
                            tv_brand.text = option
                        }

                        override fun onDismiss() {
                        }
                    })
                view.show()
            }

            //选择纸尿裤尺码
            R.id.rl_size -> {
                val options = arrayListOf<String>("NB", "S","M","L","XL","XXL")
                val view = BottomOptionsView(
                    this@EditDiapersRecordActivity,
                    "纸尿裤尺码",
                    options,
                    object : OptionClickCallback {
                        override fun onOptionClick(option: String, position: Int) {
                            tv_size.text = option
                        }

                        override fun onDismiss() {
                        }
                    })
                view.show()
            }

            //选择更换时间
            R.id.rl_time -> {
                TimePickerBuilder(this@EditDiapersRecordActivity, object : OnTimeSelectListener {
                    override fun onTimeSelect(date: Date?, v: View?) {
                        var t = sdf.format(date)
                        tv_time.text = t.substring(0, t.length - 3)
                    }
                }).setType(booleanArrayOf(true, true, true, true, true, true)).build().show()
            }

            //提交记录
            R.id.rtv_submit ->{
                if(verify()){
                    mPresenter?.submitRecord(diapersEntity)
                }
            }

        }
    }

    override fun onSubmitResult(result: Boolean) {
        if (result) {
            MessageDialogView(this, "提交成功", "确定", object : SingleBtnCallback {

                override fun onPositive(view: View) {
                    finish()
                    //发送消息，通知提交成功
                    ModuleBus.of(Gen_biz_daily_Interface::class.java)
                        ?.Gen_submitDiapersRecordResult_Method()?.post(true)
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

    private fun verify(): Boolean {

        if (TextUtils.isEmpty(tv_brand.text)) {
            MessageDialogView(this, "请选择纸尿裤品牌", "确定", null).show()
            return false
        }

        if (TextUtils.isEmpty(tv_size.text)) {
            MessageDialogView(this, "请选择纸尿裤尺码", "确定", null).show()
            return false
        }

        if (TextUtils.isEmpty(tv_time.text)) {
            MessageDialogView(this, "请选择更换时间", "确定", null).show()
            return false
        }

        if (diapersEntity == null) {
            diapersEntity = DiapersEntity()
        }

        diapersEntity?.brand = tv_brand.text.toString()
        diapersEntity?.size = tv_size.text.toString()
        diapersEntity?.createDate = tv_time.text.toString()

        return true
    }


}
