package com.schrondinger.quin.ui.music.fragment


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.Constants
import com.schrondinger.quin.Utils.Util
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseFragment
import com.schrondinger.quin.ui.music.MusicPlayActivity
import com.schrondinger.quin.ui.music.adapter.MusicListAdapter
import kotlinx.android.synthetic.main.fragment_music_local_all.*


@ActivityInject(rootViewId = R.layout.fragment_music_local_all)
class MusicLocalAllFragment : BaseFragment() {
    private var mMusicListAdapter: MusicListAdapter? = null

    override fun initData() {
        super.initData()
//        initList()
    }

//    private fun initList(){
//        musicList.clear()
//    }

    override fun initView() {
        super.initView()
        if (!Util.isNullOrEmpty(Constants.MusicList)){
            initList()
        }
    }

    //获取专辑图片的方法
    private fun initList(){
        val layoutManager = LinearLayoutManager(_mActivity)
        ry_recycler_view.layoutManager =layoutManager
        mMusicListAdapter = MusicListAdapter(_mActivity,Constants.MusicList)
        ry_recycler_view.adapter = mMusicListAdapter
        mMusicListAdapter?.setOnItemClickListener(object :MusicListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, clickType: Int) {
                when(clickType){
                    1->{ // item的点击事件
                        var bundle = Bundle()
                        bundle.putInt("localMusicIndex",position)
                        openActivity(MusicPlayActivity::class.java,bundle)
                    }
                    2->{ // 更多额点击事件

                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mMusicListAdapter?.notifyDataSetChanged()
    }

}
