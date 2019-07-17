package com.schrondinger.quin.ui.game

import android.util.Log
import android.widget.Toast
import com.schrondinger.quin.R
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import com.schrondinger.quin.bean.game.NBAer
import com.schrondinger.quin.ui.game.adapter.NBAListAdapter
import com.schrondinger.quin.widget.adapter.CommonAdapter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_game_nbamain.*
import java.util.ArrayList

@ActivityInject(rootViewId = R.layout.activity_game_nbamain)
class GameNBAMainActivity : BaseActivity() {
    private var mDataModels: ArrayList<NBAer> = ArrayList()
    override fun initView() {
        super.initView()
        setToolBar(view_toolbar)
        hv_RecyclerView.setHeaderListData(resources.getStringArray(R.array.right_title_name))
    }

    override fun initLoad() {
        super.initData()

        val currentTimeMillis = System.currentTimeMillis()
        Log.e("测试", "开始读取：$currentTimeMillis")

        Observable.create { emitter: ObservableEmitter<Int> ->

            var size = 0
            Realm.getDefaultInstance().use { realm ->
                size = realm.where<NBAer>().findAll().size
            }
            emitter.onNext(size)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("测试", "读取有索引数据用时：${System.currentTimeMillis() - currentTimeMillis}，有索引数据总个数：$it")
                }, { it.printStackTrace() })



        mDataModels = ArrayList()
        var NBAInfo = NBAer(0,"PG","张三","ZS","PG/SG","18","78")
        mDataModels.add(NBAInfo)
        NBAInfo = NBAer(1,"SG","李四","ZS","SG","19","77")
        mDataModels.add(NBAInfo)
        NBAInfo = NBAer(2,"SF","王五","ZS","SF","20","76")
        mDataModels.add(NBAInfo)
        NBAInfo = NBAer(3,"PF","赵六","ZS","PF/C","21","79")
        mDataModels.add(NBAInfo)
        NBAInfo = NBAer(4,"C","吴七","ZS","C","22","80")
        mDataModels.add(NBAInfo)

        for (i in 5 .. 14){
            NBAInfo = NBAer(i,"T","替补"+(i-4),"ZS","PF/C","16","70")
            mDataModels.add(NBAInfo)
        }

        var adapter = NBAListAdapter(this, mDataModels, R.layout.game_item_layout, object : CommonAdapter.CommonViewHolder.onItemCommonClickListener {
            override fun onItemClickListener(position: Int) {
                Toast.makeText(this@GameNBAMainActivity, "position--->$position", Toast.LENGTH_SHORT).show()
            }

            override fun onItemLongClickListener(position: Int) {

            }
        })

        hv_RecyclerView.setAdapter(adapter)
    }


}
