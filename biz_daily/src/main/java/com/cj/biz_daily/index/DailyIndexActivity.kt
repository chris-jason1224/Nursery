package com.cj.biz_daily.index

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cj.base_common.mvp.BaseMVPActivity
import com.cj.biz_daily.R
import kotlinx.android.synthetic.main.biz_daily_activity_daily_index.*

@Route(path = "/biz_daily/ACT/com.cj.biz_daily.index.DailyIndexActivity")
class DailyIndexActivity : BaseMVPActivity<DailyIndexPresenter>() ,IDailyIndexView{

    override fun createPresenter(): DailyIndexPresenter {
        return DailyIndexPresenter()
    }

    override fun initStatusLayout(): View? {
        return ll_content
    }

    override fun resourceLayout(): Int {
        return R.layout.biz_daily_activity_daily_index
    }

    override fun initView() {
        rl_feed.setOnClickListener(this)
        rl_excrete.setOnClickListener(this)
        rl_change_diapers.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.rl_feed -> ARouter.getInstance().build("/biz_daily/ACT/com.cj.biz_daily.feed.FeedActivity").navigation()

            R.id.rl_excrete -> ARouter.getInstance().build("/biz_daily/ACT/com.cj.biz_daily.shit.ShitActivity").navigation()

            R.id.rl_change_diapers-> ARouter.getInstance().build("/biz_daily/ACT/com.cj.biz_daily.diapers.DiapersActivity").navigation()

        }
    }
}
