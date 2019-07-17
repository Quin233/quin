package com.schrondinger.quin.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.widget.RemoteViews
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.Util
import com.schrondinger.quin.bean.music.Music
import com.schrondinger.quin.ui.music.MusicPlayActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*


/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/14 11:26 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class QuinMediaPlayerService:Service(), MediaPlayer.OnPreparedListener {
    companion object {
        val TYPE_MUSIC = 0
        val TYPE_MOVIE = 1
        val FLAG_PLAY = 2 // 播放或者暂停
        val FLAG_PREVIOUS = 3 // 上一曲
        val FLAG_NEXT = 4 // 下一曲
        val FLAG_PROGRESS = 5 // 更新播放进度
        val FLAG_LOAD_PATH = 6 // 更新播放列表
        val FLAG_PLAY_TYPE = 7 // 更新列表播放方式
        val ACTION_MAX = "max_action" // 发送最大进度的广播的动作
        val ACTION_PROGRESS = "progress_action"// 发送当前进度的广播的动作
        val BUTTON_ACTION = "quin_button_action" // Notification的广播
    }

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var localBroadcastManager :LocalBroadcastManager
    private lateinit var notificationReceiver:NotificationReceiver
    private lateinit var timer:Timer


    /** 音乐播放的参数 **/
    private var playTpe =  TYPE_MUSIC //播放模式
    private var musicList = ArrayList<Music>() // 播放的列表
    private var playMode = 1 // 默认列表循环
    private var playMusicIndex = 0 // 播放音乐脚标


    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        initReceiver()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.start()
    }

    /**
     * 返回值：
     *      START_NOT_STICKY：使用这个返回值时，如果在执行完onStartCommand方法后，服务被异常kill掉，系统不会自动重启该服务。
     *      START_STICKY：如果Service进程被kill掉，保留Service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建Service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到Service，那么参数Intent将为null。
     *      START_REDELIVER_INTENT：使用这个返回值时，系统会自动重启该服务，并将Intent的值传入。
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var flag = intent.extras.getInt("flag")
        when(flag){
            QuinMediaPlayerService.FLAG_LOAD_PATH->{
                playTpe = intent.extras.getInt("playTpe")
                musicList = intent.extras.getSerializable("musicList") as ArrayList<Music>
                playMusicIndex = intent.extras.getInt("position")
                playMode = intent.extras.getInt("mode")
                play(musicList[playMusicIndex])
            }
            QuinMediaPlayerService.FLAG_PLAY->{
                if(mediaPlayer!=null && mediaPlayer!!.isPlaying) {
                    mediaPlayer?.pause()
                    timer.cancel()
                } else {
                    mediaPlayer?.start()
                    sendCurProgress() // 继续发送广播
                }
                showMusicInfoPre(musicList[playMusicIndex]) // 展示前台音乐信息
            }
            QuinMediaPlayerService.FLAG_NEXT->{
                playMusicIndex++
                if (playMusicIndex>musicList.size-1) {
                    playMusicIndex = 0
                }
                mediaPlayer?.reset()
                play(musicList[playMusicIndex])
            }
            QuinMediaPlayerService.FLAG_PROGRESS->{
                var progress = intent.extras.getInt("progress")
                mediaPlayer?.seekTo(progress)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun setPlayMode() {
        when(playMode){
            0->{// 单曲循环
                mediaPlayer?.reset()
                play(musicList[playMusicIndex])
            }
            1->{// 顺序播放
                playMusicIndex++
                if (playMusicIndex>musicList.size-1) {
                    playMusicIndex = 0
                }
                mediaPlayer?.reset()
                play(musicList[playMusicIndex])
            }
            2->{ // 随机播放
                playMusicIndex = (Math.random() * musicList.size) as Int
                mediaPlayer?.reset()
                play(musicList[playMusicIndex])
            }
        }
    }

    /**
     * 播放
     */
    private fun play(music:Music){
        mediaPlayer?.reset()
        try {
            mediaPlayer?.setDataSource(music.path)
            mediaPlayer?.prepare() // 准备
            sendMaxProgress() // 发送广播，让Activity展示该音乐的最大值
            mediaPlayer?.start() // 启动
            showMusicInfoPre(music) // 展示前台音乐信息
            sendCurProgress() // 发送广播，让Activity动态展示当前播放进度
            mediaPlayer?.setOnCompletionListener {
                if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
                    setPlayMode()
                }
            }
        }catch (e:IllegalArgumentException){
            e.printStackTrace()
        }catch (e:SecurityException){
            e.printStackTrace()
        }catch (e:IllegalStateException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }


    }

    /** 获取音乐信息并展示 **/
    private fun showMusicInfo(music:Music,bitmap:Bitmap){
        // Notification的点击事件
        var intent = Intent(this,MusicPlayActivity::class.java)
        var bundle = Bundle()
        bundle.putInt("count",1)
        intent.putExtras(bundle)
        var pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // 自定义布局
        var mRemoteViews = RemoteViews(packageName,R.layout.notification_view)
        // 展示 音乐信息
        mRemoteViews.setTextViewText(R.id.tv_custom_song_name, music.title)
        mRemoteViews.setTextViewText(R.id.tv_custom_song_artist, music.artist)
        mRemoteViews.setImageViewBitmap(R.id.iv_custom_song_icon,bitmap)
        if(mediaPlayer != null && mediaPlayer!!.isPlaying){
            mRemoteViews.setImageViewResource(R.id.iv_custom_song_status, R.drawable.music_pause_primary)
        }else {
            mRemoteViews.setImageViewResource(R.id.iv_custom_song_status, R.drawable.music_play_primary)
        }

        // 暂停点击事件
        var buttonIntent = Intent(BUTTON_ACTION)
        buttonIntent.putExtra("INTENT_BUTTON_TAG","STATUS")
        var playIntent = PendingIntent.getBroadcast(this,2,buttonIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        mRemoteViews.setOnClickPendingIntent(R.id.iv_custom_song_status, playIntent)


        // 下一曲的点击事件
        buttonIntent.putExtra("INTENT_BUTTON_TAG","NEXT")
        var nextIntent = PendingIntent.getBroadcast(this, 3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        mRemoteViews.setOnClickPendingIntent(R.id.iv_custom_song_next, nextIntent)

        // 关闭的点击事件
        buttonIntent.putExtra("INTENT_BUTTON_TAG","CLOSE")
        var closeIntent = PendingIntent.getBroadcast(this, 4, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        mRemoteViews.setOnClickPendingIntent(R.id.iv_custom_song_close, closeIntent)


        var mNotification = NotificationCompat.Builder(this,"serviceId")
                .setContent(mRemoteViews)
                //指定通知的标题内容
//                .setContentTitle("This is content title")
                //设置通知的内容
//                .setContentText("This is content text")
                //指定通知被创建的时间
//                .setWhen(System.currentTimeMillis())
                //设置通知的小图标
                .setSmallIcon(R.drawable.nav_search_icon)
                //设置通知的大图标
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.nav_search_icon))
                //添加点击跳转通知跳转
                .setContentIntent(pendingIntent)
        //实现点击跳转后关闭通知
//              .setAutoCancel(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("serviceId", "serviceName", NotificationManager.IMPORTANCE_LOW)
            notificationChannel.description = "音乐播放"
            notificationChannel.enableLights(false) // 闪光灯
            notificationChannel.enableVibration(false) // 震动

            var notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)


            mNotification.setChannelId("serviceId")
        }

        startForeground(1,mNotification.build())
    }


    private fun showMusicInfoPre(music:Music){
        Observable.create(ObservableOnSubscribe<Bitmap> {
            val mUriAlbums = "content://media/external/audio/albums"
            val projection = arrayOf("album_art")
            val cur = this.contentResolver.query(Uri.parse(mUriAlbums + "/" + Integer.toString(music.albumBip_id)), projection, null, null, null)
            var albumArt: String? = null
            if (cur!!.count > 0 && cur.columnCount > 0) {
                cur.moveToNext()
                albumArt = cur.getString(0)
            }
            cur.close()
            var bm: Bitmap?
            if (albumArt != null) {
                bm = BitmapFactory.decodeFile(albumArt)
            } else {
                bm = BitmapFactory.decodeResource(resources, R.drawable.head_default)
            }
            it.onNext(bm)
            it.onComplete()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            showMusicInfo(music,it)
        }
    }


    /** Broadcast有关 **/
    private fun sendMaxProgress(){
        //发送本地广播
        val intent = Intent(ACTION_MAX)
        intent.putExtra("position",playMusicIndex)
        intent.putExtra("max", mediaPlayer?.duration)// 每首音乐的完整时间
        localBroadcastManager.sendBroadcast(intent)
    }

    private fun sendCurProgress(){
        timer = Timer()
        val timeIntent = Intent(ACTION_PROGRESS)
        timer.schedule(object :TimerTask(){
            override fun run() {
                if (!Util.isNullOrEmpty(mediaPlayer) && mediaPlayer!!.isPlaying) {//如果不在播放状态，则停止更新
                    timeIntent.putExtra("progress", mediaPlayer?.currentPosition)// 获取当前播放进度
                    localBroadcastManager.sendBroadcast(timeIntent)
                }
            }
        },0,1000)
    }

    private fun initReceiver(){
        notificationReceiver = NotificationReceiver()
        var mNotificationIntentFilter = IntentFilter()
        mNotificationIntentFilter.addAction(BUTTON_ACTION)
        registerReceiver(notificationReceiver,mNotificationIntentFilter)
    }

    inner class NotificationReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var actionTag = intent.getStringExtra("INTENT_BUTTON_TAG")
            when(actionTag){
                "STATUS"->{ // 播放状态的点击事件
                    if(mediaPlayer != null && mediaPlayer!!.isPlaying) {
                        mediaPlayer?.pause()
                        timer.cancel()
                    } else {
                        mediaPlayer?.start()
                        sendCurProgress()
                    }
                    showMusicInfoPre(musicList[playMusicIndex]) // 展示前台音乐信息
                }
                "NEXT"->{ // 下一曲
                    playMusicIndex++
                    if (playMusicIndex>musicList.size-1) {
                        playMusicIndex = 0
                    }
                    mediaPlayer?.reset()
                    play(musicList[playMusicIndex])
                }
                "CLOSE"->{ // 关闭播放
                    stopSelf()
                }
            }
        }
    }

    override fun onDestroy() {
        if(mediaPlayer != null && mediaPlayer!!.isPlaying) {
            timer.cancel()
        }
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        unregisterReceiver(notificationReceiver)
        super.onDestroy()
    }

}