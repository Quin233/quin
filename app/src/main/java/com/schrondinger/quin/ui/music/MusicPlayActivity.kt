package com.schrondinger.quin.ui.music

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.Constants
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import com.schrondinger.quin.bean.music.Music
import com.schrondinger.quin.ui.service.QuinMediaPlayerService
import com.schrondinger.quin.widget.PhonographView
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_music_play.*
import kotlinx.android.synthetic.main.music_play_image.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


@ActivityInject(rootViewId = R.layout.activity_music_play)
class MusicPlayActivity : BaseActivity() {

    private var playMode = 1 // 0 单曲循环；1 顺序播放；2 随机播放
    private var isStop:Boolean = false
    private var playMusicIndex = 0
    private lateinit var music: Music
//    private var objectAnimator: ObjectAnimator? = null
//    private var objectAnimator2: ObjectAnimator? = null
    private var isServiceStop:Boolean = false
    private lateinit var localBroadcastManager:LocalBroadcastManager
    private lateinit var progressReceiver:ProgressReceiver
    private lateinit var maxReceiver:MaxReceiver
    private lateinit var notificationReceiver:NotificationReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle = savedInstanceState

    }

    override fun initData() {
        super.initData()
        playMusicIndex = intent.getIntExtra("localMusicIndex",0)
    }

    override fun initView() {
        super.initView()

        // 播放音乐
        startMediaService()

        // 初始化广播接收器
        initReceiver()

        sb_playSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    var bundle = Bundle()
                    bundle.putInt("flag",QuinMediaPlayerService.FLAG_PROGRESS)
                    bundle.putInt("progress",progress)
                    updateService(bundle)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    override fun initListener() {
        super.initListener()
        fl_content_default.setOnClickListener(this)
        fl_content_lyc.setOnClickListener(this)

        iv_play_type.setOnClickListener(this)
        iv_play_pre.setOnClickListener(this)
        iv_play_status.setOnClickListener(this)
        iv_play_next.setOnClickListener(this)
        iv_show_list.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when(v.id){
            R.id.fl_content_default->{
                fl_content_default.visibility = View.GONE
                fl_content_lyc.visibility = View.VISIBLE
            }
            R.id.fl_content_lyc->{
                fl_content_default.visibility = View.VISIBLE
                fl_content_lyc.visibility = View.GONE
            }
            R.id.iv_play_type->{ // 播放模式
                var bundle = Bundle()
                bundle.putInt("flag",QuinMediaPlayerService.FLAG_PLAY_TYPE)
                when(playMode){
                    0->{ // 当前是单曲循环
                        playMode = 1
                        Glide.with(this).load(R.drawable.music_play_list).asBitmap().into(iv_play_type)
                        bundle.putInt("mode",playMode)
                    }
                    1->{ // 当前是顺序播放
                        playMode = 2
                        Glide.with(this).load(R.drawable.music_play_radom).asBitmap().into(iv_play_type)
                        bundle.putInt("mode",playMode)
                    }
                    2->{ // 当前是随机播放
                        playMode = 0
                        Glide.with(this).load(R.drawable.music_play_one).asBitmap().into(iv_play_type)
                        bundle.putInt("mode",playMode)
                    }
                }
                updateService(bundle)
            }
            R.id.iv_play_pre->{
                var bundle = Bundle()
                bundle.putInt("flag",QuinMediaPlayerService.FLAG_PREVIOUS)
                updateService(bundle)
            }
            R.id.iv_play_status->{
                if (isServiceStop){ // 服务已经关掉了，需要从新开下
                    startMediaService()
                }else{ // 服务没有关掉，更新即可
                    if (isStop){
                        Glide.with(this).load(R.drawable.music_pause).asBitmap().into(iv_play_status)
//                        objectAnimator?.resume()
                        iv_music_album.resume()
                    }else{
                        Glide.with(this).load(R.drawable.music_play).asBitmap().into(iv_play_status)
                        iv_music_album.pause()
//                        objectAnimator?.pause()
                    }
                    isStop = !isStop
                    var bundle = Bundle()
                    bundle.putInt("flag",QuinMediaPlayerService.FLAG_PLAY)
                    updateService(bundle)
                }
                isServiceStop = false
            }
            R.id.iv_play_next->{
                var bundle = Bundle()
                bundle.putInt("flag",QuinMediaPlayerService.FLAG_NEXT)
                updateService(bundle)
            }
            R.id.iv_show_list->{


            }
        }
    }

    //格式化数字
    private fun formatTime(length: Int): String {
        val date = Date(length.toLong())
        val simpleDateFormat = SimpleDateFormat("mm:ss")    //规定固定的格式
        return simpleDateFormat.format(date)
    }

    /***** Service- *****/

    private fun startMediaService(){
        var service = Intent(this, QuinMediaPlayerService::class.java)
        var bundle = Bundle()
        bundle.putInt("flag", QuinMediaPlayerService.FLAG_LOAD_PATH)
        bundle.putInt("playTpe",QuinMediaPlayerService.TYPE_MUSIC)
        bundle.putSerializable("musicList",Constants.MusicList)
        bundle.putInt("position",playMusicIndex)
        bundle.putInt("mode",playMode)
        service.putExtras(bundle)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(service)
        }else{
            startService(service)
        }
    }

    private fun updateService(bundle: Bundle){
        val service = Intent(this, QuinMediaPlayerService::class.java)
        service.putExtras(bundle)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(service)
        }else{
            startService(service)
        }
    }

    private fun initMusicView(){
        music = Constants.MusicList[playMusicIndex]
        view_toolbar.title = music.title +"（"+music.artist+"）"
        showAlbumBip(music.albumBip_id,iv_music_album)
        iv_music_album.start()
    }


    /***** -Service *****/

    private fun initReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        progressReceiver = ProgressReceiver()
        var progressFilter = IntentFilter(QuinMediaPlayerService.ACTION_PROGRESS)
        localBroadcastManager.registerReceiver(progressReceiver, progressFilter)

        maxReceiver = MaxReceiver()
        var maxFilter = IntentFilter(QuinMediaPlayerService.ACTION_MAX)
        localBroadcastManager.registerReceiver(maxReceiver, maxFilter)

        notificationReceiver = NotificationReceiver()
        var notificationFilter = IntentFilter(QuinMediaPlayerService.BUTTON_ACTION)
        registerReceiver(notificationReceiver,notificationFilter)
    }

    // 接收播放进度的广播接收器
    inner class ProgressReceiver:BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var progress = intent.getIntExtra("progress", 0)
            sb_playSeekBar.progress = progress
            var curTime = formatTime(progress)
            tv_currentTime.text = curTime
        }
    }

    // 接收最大进度的广播接收器
    inner class MaxReceiver:BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var max = intent.getIntExtra("max", 0)
            sb_playSeekBar.max = max
            var maxTime = formatTime(max)
            tv_totalTime.text = maxTime
            playMusicIndex = intent.getIntExtra("position",playMusicIndex)
            initMusicView() // 初始化播放的界面
            //旋转音乐图片
//            objectAnimator = ObjectAnimator.ofFloat(iv_music_album, "rotation", 0f, 360f)
//            objectAnimator?.duration = 16000
//            objectAnimator?.interpolator = LinearInterpolator()
                //设置循环次数 -1为一直循环
//            objectAnimator?.repeatCount = -1
//            objectAnimator?.repeatMode = ValueAnimator.RESTART
//            objectAnimator?.start()
        }
    }

    // 接受Notification事件
    inner class NotificationReceiver:BroadcastReceiver(){
        override fun onReceive(p0: Context?, intetn: Intent?) {
            when(intetn?.getStringExtra("INTENT_BUTTON_TAG")){
                "STATUS"->{ // 播放状态的点击事件
                    if (isStop){
                        Glide.with(this@MusicPlayActivity).load(R.drawable.music_pause).asBitmap().into(iv_play_status)
//                        objectAnimator?.resume()
                        iv_music_album.resume()
                    }else{
                        Glide.with(this@MusicPlayActivity).load(R.drawable.music_play).asBitmap().into(iv_play_status)
                        iv_music_album.pause()
//                        objectAnimator?.pause()
                    }
                    isStop = !isStop
                }
                "NEXT"->{ // 下一曲

                }
                "CLOSE"->{ // 关闭播放
                    isServiceStop = true
//                    objectAnimator?.end()
                    Glide.with(this@MusicPlayActivity).load(R.drawable.music_play).asBitmap().into(iv_play_status)
                    sb_playSeekBar.progress = 0
                    tv_currentTime.text = "0:00"
                    iv_music_album.stop()
                }
            }
        }
    }

    private fun showAlbumBip(albumBipId:Int,srcView:PhonographView){
        Observable.create(ObservableOnSubscribe<String> {

            val mUriAlbums = "content://media/external/audio/albums"
            val projection = arrayOf("album_art")
            val cur = this.contentResolver.query(Uri.parse(mUriAlbums + "/" + Integer.toString(albumBipId)), projection, null, null, null)
            var albumArt = ""
            if (cur!!.count > 0 && cur.columnCount > 0) {
                cur.moveToNext()
                albumArt = cur.getString(0)
            }
            cur.close()
//            var bm: Bitmap?
//            if (albumArt != null) {
//                bm = BitmapFactory.decodeFile(albumArt)
//            } else {
//                bm = BitmapFactory.decodeResource(resources, R.drawable.head_default)
//            }
            it.onNext(albumArt)
            it.onComplete()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            srcView.setFile(it)
        }
    }

    override fun onDestroy() {
        localBroadcastManager.unregisterReceiver(progressReceiver)
        localBroadcastManager.unregisterReceiver(maxReceiver)
        unregisterReceiver(notificationReceiver)
        super.onDestroy()
    }
}
