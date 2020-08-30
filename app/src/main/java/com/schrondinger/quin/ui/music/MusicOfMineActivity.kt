package com.schrondinger.quin.ui.music

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.Constants
import com.schrondinger.quin.Utils.Util
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import com.schrondinger.quin.bean.music.Music
import com.schrondinger.quin.bean.music.MusicContentItem
import com.schrondinger.quin.ui.music.adapter.MusicFunctionAdapter
import com.schrondinger.quin.ui.music.adapter.MusicMineListAdapter
import com.schrongder.nba.kotlin.TopToast
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_music_of_mine.*


@ActivityInject(R.layout.activity_music_of_mine)
class MusicOfMineActivity : BaseActivity() {

    private var mMusicFunctionAdapter: MusicFunctionAdapter? = null
    private var mMusicCreatedAdapter: MusicMineListAdapter? = null
    private var mMusicLoveAdapter: MusicMineListAdapter? = null
    private var mMusicFunctionList = ArrayList<MusicContentItem>()
    private var mMusicCreatedList = ArrayList<MusicContentItem>()
    private var mMusicLoveList = ArrayList<MusicContentItem>()

    override fun initData() {
        super.initData()
        mMusicFunctionList.add(MusicContentItem(R.drawable.music_local,"12","本地音乐","",MusicOfLocalActivity::class.java.name))
        mMusicFunctionList.add(MusicContentItem(R.drawable.music_play_recent,"12","最近播放","",""))
        mMusicFunctionList.add(MusicContentItem(R.drawable.music_download,"12","下载管理","",""))
        mMusicFunctionList.add(MusicContentItem(R.drawable.music_radio,"12","我的电台","",""))
        mMusicFunctionList.add(MusicContentItem(R.drawable.music_love_list,"12","我的收藏","",""))
        mMusicFunctionList.add(MusicContentItem(R.drawable.music_sati_sapce,"特别的聆听模式","Sati空间","",""))

        mMusicCreatedList.add(MusicContentItem(R.drawable.sample_footer_loading,"24首","我喜欢的音乐","，已下载10首",""))
        mMusicCreatedList.add(MusicContentItem(R.drawable.sample_footer_loading,"4首","国语","，已下载1首",""))
        mMusicCreatedList.add(MusicContentItem(R.drawable.sample_footer_loading,"5首","欧美风音乐","",""))
        mMusicCreatedList.add(MusicContentItem(R.drawable.sample_footer_loading,"5首","日系音乐","",""))
        mMusicCreatedList.add(MusicContentItem(R.drawable.sample_footer_loading,"10首","轻音乐","，已下载9首",""))

        mMusicLoveList.add(MusicContentItem(R.drawable.sample_footer_loading,"11首 by 张三","NieR:Automata Original Soundtrack","",""))
        mMusicLoveList.add(MusicContentItem(R.drawable.sample_footer_loading,"5首 by 赵柳","今我来思，雨雪霏霏","",""))
        mMusicLoveList.add(MusicContentItem(R.drawable.sample_footer_loading,"9首 by 王五","能量充电站","",""))
        mMusicLoveList.add(MusicContentItem(R.drawable.sample_footer_loading,"10首 by aiD","薛定谔的猫","",""))

        // 获取本地音乐数据列表
        if (Constants.MusicList.size <= 0){
            getLocalMusic()
        }
    }

    override fun initView() {
        super.initView()
        setToolBar(view_toolbar)
        // 设置列表样式
//        val layoutManager1 = MyLinearLayoutM
        val layoutManager1 = LinearLayoutManager(this)
        val layoutManager2 = LinearLayoutManager(this)
        val layoutManager3 = LinearLayoutManager(this)
        // 将样式设置到ry中

        ry_music_function.layoutManager =layoutManager1
        ry_music_created_list.layoutManager = layoutManager2
        ry_music_love_list.layoutManager = layoutManager3

        mMusicFunctionAdapter = MusicFunctionAdapter(this,mMusicFunctionList)
        mMusicCreatedAdapter = MusicMineListAdapter(this,mMusicCreatedList)
        mMusicLoveAdapter = MusicMineListAdapter(this,mMusicLoveList)

        ry_music_function.adapter = mMusicFunctionAdapter
        ry_music_created_list.adapter = mMusicCreatedAdapter
        ry_music_love_list.adapter = mMusicLoveAdapter

        mMusicFunctionAdapter?.setOnItemClickListener(object :MusicFunctionAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                try {
                    val clazz = Class.forName((mMusicFunctionList[position].activity)) as Class<BaseActivity>
                    toActivity(clazz)
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }
        })
        mMusicCreatedAdapter?.setOnItemClickListener(object :MusicMineListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

            }
        })
        mMusicLoveAdapter?.setOnItemClickListener(object :MusicMineListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

            }
        })
    }

    override fun initListener() {
        super.initListener()
        fr_created_list.setOnClickListener(this)
        tv_created_list.setOnClickListener(this)
        fr_love_list.setOnClickListener(this)
        tv_love_list.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.fr_created_list,R.id.tv_created_list->{
                fr_created_list.setMode(!fr_created_list.isUnFold())
            }
            R.id.fr_love_list,R.id.tv_love_list->{
                fr_love_list.setMode(!fr_love_list.isUnFold())
            }
        }
    }

    private fun getLocalMusic(){
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { aBoolean ->
                    if (aBoolean) {
                        initList()
                    } else {
                        TopToast.creat(this).setMessageText("请在手机设置中确认存储权限开启状态。").show()
                    }
                }
    }

    private fun initList(){
        Log.d("MusicOfMine","开始标志")
        Observable.create(ObservableOnSubscribe<Music> {
            //获取ContentResolver的对象，并进行实例化
            val resolver = this.contentResolver
            //获取游标
            val cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null) //创建游标MediaStore.Audio.Media.EXTERNAL_CONTENT_URI获取音频的文件，后面的是关于select筛选条件，这里填土null就可以了

            if (cursor!!.moveToFirst()){
                do {
                    var title =  cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))//歌名
                    var artist =  cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))//歌唱者
                    var album =  cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))//专辑名
                    var albumID =  cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))//专辑图片id
                    var length =  cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))//歌曲长度
                    var path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))//路径
                    var music = Music(title,artist,album,length,albumID,path,false)
                    it.onNext(music)
                }while (cursor.moveToNext())
            }
            Log.d("MusicOfMine","结束标志")
            cursor.close()
            it.onComplete()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Constants.MusicList.add(it)
            Constants.MusicPlayList.add(it.path)
        }
    }
}
