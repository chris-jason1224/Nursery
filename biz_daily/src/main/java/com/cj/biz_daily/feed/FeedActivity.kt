package com.cj.biz_daily.feed

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cj.base_common.`var`.pageSize
import com.cj.base_common.base.BaseApp
import com.cj.base_common.bus.ModuleBus
import com.cj.base_common.model.Footer
import com.cj.base_common.model.MilkEntity
import com.cj.base_common.model.NoContent
import com.cj.base_common.multitype.Items
import com.cj.base_common.multitype.MultiTypeViewBinder
import com.cj.base_common.multitype.ViewHolder
import com.cj.base_common.mvp.BaseMVPActivity
import com.cj.biz_daily.R
import com.cj.bus.Gen_biz_daily_Interface
import com.drakeet.multitype.MultiTypeAdapter
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.biz_daily_activity_feed.*

@Route(path = "/biz_daily/ACT/com.cj.biz_daily.feed.FeedActivity")
class FeedActivity : BaseMVPActivity<FeedPresenter>(), IFeedView {

    private lateinit var items: Items
    private lateinit var adapter: MultiTypeAdapter
    private lateinit var l: LoadService<Any>

    private var bottomEntity: Footer = Footer("没有更多数据~")
    private var noContent: NoContent = NoContent("暂时没有数据~")
    //分页查询参数
    private var pageIndex: Long = 1L


    override fun createPresenter(): FeedPresenter {
        return FeedPresenter()
    }

    override fun initStatusLayout(): View? {
        return ll_content
    }

    override fun resourceLayout(): Int {
        return R.layout.biz_daily_activity_feed
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

    private fun loadData() {
        mPresenter?.findFeedRecordByPage(pageIndex)
    }

    override fun initData() {

        ModuleBus.of(Gen_biz_daily_Interface::class.java)?.Gen_submitFeedRecordResult_Method()
            ?.observe(this, object : androidx.lifecycle.Observer<Boolean> {
                override fun onChanged(t: Boolean?) {
                    if (t != null && t == true) {
                        pageIndex = 1
                        loadData()
                    }
                }

            })

        mPresenter?.findFeedRecordByPage(pageIndex)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()
            R.id.tv_create -> {
                ARouter.getInstance()
                    .build("/biz_daily/ACT/com.cj.biz_daily.feed.create.EditFeedRecordActivity")
                    .navigation()
            }
        }
    }

    private fun initRV() {
        items = Items()

        adapter = MultiTypeAdapter(items)

        val register: MultiTypeViewBinder<MilkEntity> = object :
            MultiTypeViewBinder<MilkEntity>(this, R.layout.biz_daily_item_feed_record_layout) {
            override fun convert(holder: ViewHolder, t: MilkEntity, position: Int) {

                var rl_item: RelativeLayout = holder.getView(R.id.rl_item)
                var tv_type: TextView = holder.getView(R.id.tv_type)
                var tv_capacity: TextView = holder.getView(R.id.tv_capacity)
                var tv_date: TextView = holder.getView(R.id.tv_date)

                tv_type.text = when (t.type) {
                    10 -> "母乳"
                    else -> "奶粉"
                }

                tv_capacity.text = if (t.capacity > 0) "${t.capacity} ML" else "奶量未知"

                tv_date.text = "${t.startTime} 至 ${t.endTime}"

                rl_item.setOnClickListener {
                    ARouter.getInstance()
                        .build("/biz_daily/ACT/com.cj.biz_daily.feed.create.EditFeedRecordActivity")
                        .withSerializable("milk_record_entity", t)
                        .navigation()
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

        adapter.register(MilkEntity::class.java, register)
        adapter.register(Footer::class.java, register_bottom)
        adapter.register(NoContent::class.java, register_nothing)

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

    }

    override fun onFeedRecordGet(data: List<MilkEntity>) {
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
