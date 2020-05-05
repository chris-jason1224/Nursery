package com.cj.biz_daily.diapers

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cj.base_common.`var`.pageSize
import com.cj.base_common.bus.ModuleBus
import com.cj.base_common.model.DiapersEntity
import com.cj.base_common.model.Footer
import com.cj.base_common.model.NoContent
import com.cj.base_common.multitype.Items
import com.cj.base_common.multitype.MultiTypeViewBinder
import com.cj.base_common.multitype.ViewHolder
import com.cj.base_common.mvp.BaseMVPActivity
import com.cj.biz_daily.R
import com.cj.bus.Gen_biz_daily_Interface
import com.drakeet.multitype.MultiTypeAdapter
import kotlinx.android.synthetic.main.biz_daily_activity_diapers.*

@Route(path = "/biz_daily/ACT/com.cj.biz_daily.diapers.DiapersActivity")
class DiapersActivity : BaseMVPActivity<DiapersPresenter>(), IDiapersView {

    private lateinit var items: Items
    private lateinit var adapter: MultiTypeAdapter
    private var bottomEntity: Footer = Footer("没有更多数据~")
    private var noContent: NoContent = NoContent("暂时没有数据~")
    //分页查询参数
    private var pageIndex: Long = 1L

    override fun createPresenter(): DiapersPresenter {
        return DiapersPresenter()
    }

    override fun initStatusLayout(): View? {
        return ll_content
    }

    override fun resourceLayout(): Int {
        return R.layout.biz_daily_activity_diapers
    }

    override fun initView() {
        iv_back.setOnClickListener(this)
        tv_create.setOnClickListener(this)

        initRV()

        refresh.setOnRefreshListener {
            pageIndex = 1
            loadData()
        }

        refresh.setOnLoadMoreListener {
            loadData()
        }
    }

    override fun initData() {
        ModuleBus.of(Gen_biz_daily_Interface::class.java)?.Gen_submitDiapersRecordResult_Method()
            ?.observe(this, object : androidx.lifecycle.Observer<Boolean> {
                override fun onChanged(t: Boolean?) {
                    if (t != null && t == true) {
                        pageIndex = 1
                        loadData()
                    }
                }

            })
        mPresenter?.findDiapersRecordByPage(pageIndex)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()
            R.id.tv_create -> ARouter.getInstance().build("/biz_daily/ACT/com.cj.biz_daily.diapers.edit.EditDiapersRecordActivity").navigation()
        }
    }

    private fun initRV() {
        items = Items()

        adapter = MultiTypeAdapter(items)

        val register: MultiTypeViewBinder<DiapersEntity> = object :
            MultiTypeViewBinder<DiapersEntity>(
                this,
                R.layout.biz_daily_item_diapers_record_layout
            ) {
            override fun convert(holder: ViewHolder, diapers: DiapersEntity, position: Int) {
                var tv_brand: TextView = holder.getView(R.id.tv_brand)
                var tv_size: TextView = holder.getView(R.id.tv_size)
                var tv_date: TextView = holder.getView(R.id.tv_date)
                var rl_content: RelativeLayout = holder.getView(R.id.rl_content)

                tv_brand.text = diapers.brand
                tv_size.text = diapers.size
                tv_date.text = diapers.createDate

                rl_content.setOnClickListener {
                    ARouter.getInstance()
                        .build("/biz_daily/ACT/com.cj.biz_daily.diapers.edit.EditDiapersRecordActivity")
                        .withSerializable("diapers_record_entity", diapers).navigation()
                }

            }

        }

        val register_bottom: MultiTypeViewBinder<Footer> =
            object : MultiTypeViewBinder<Footer>(this, R.layout.base_common_item_bottom_layout) {
                override fun convert(holder: ViewHolder, t: Footer, position: Int) {

                }
            }

        val register_nothing: MultiTypeViewBinder<NoContent> =
            object :
                MultiTypeViewBinder<NoContent>(this, R.layout.base_common_on_empty_state_layout) {
                override fun convert(holder: ViewHolder, t: NoContent, position: Int) {
                }
            }

        adapter.register(DiapersEntity::class.java, register)
        adapter.register(Footer::class.java, register_bottom)
        adapter.register(NoContent::class.java, register_nothing)

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

    }

    private fun loadData() {
        mPresenter?.findDiapersRecordByPage(pageIndex)
    }

    override fun onDiapersRecordGet(data: List<DiapersEntity>) {
        //刷新或第一次加载
        if (pageIndex == 1L) {
            items.clear()
            if (data != null && data.isNotEmpty()) {
                showSuccessLayout()
                items.addAll(data)

                //已经加载完所有数据
                if (data.size < pageSize) {
                    items.add(bottomEntity)
                    refresh.setEnableLoadMore(false)
                } else {
                    pageIndex++
                    refresh.setEnableLoadMore(true)
                }
            } else {
                //刷新或首次加载失败
                //showEmptyLayout("暂时没有数据哦")
                showEmpty()
            }

        } else if (pageIndex > 1) {
            //上拉加载时
            if (data != null && data.isNotEmpty()) {

                items.addAll(data)
                if (data.size < pageSize) {
                    if (items.contains(bottomEntity)) {
                        items.remove(bottomEntity)
                        items.add(bottomEntity)
                    } else {
                        items.add(bottomEntity)
                    }
                    //上拉加载完所有数据，禁止上拉事件
                    refresh.setEnableLoadMore(false)
                } else {
                    pageIndex++
                    refresh.setEnableLoadMore(true)
                }
            } else {
                //上拉加载完了所有数据
                if (items.contains(bottomEntity)) {
                    items.remove(bottomEntity)
                    items.add(bottomEntity)
                } else {
                    items.add(bottomEntity)
                }
                refresh.setEnableLoadMore(false)
            }

        }

        adapter.notifyDataSetChanged()
    }

    override fun finishRefreshAndLoadMore() {
        refresh?.finishRefresh()
        refresh?.finishLoadMore()
    }

    private fun showEmpty() {
        refresh.setEnableLoadMore(false)
        items.clear()
        items.add(noContent)
        adapter.notifyDataSetChanged()
    }


}
