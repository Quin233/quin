package com.schrondinger.quin.Utils

import com.schrondinger.quin.bean.User
import com.schrondinger.quin.bean.music.Music

import java.io.File
import java.util.ArrayList

object Constants {
    var user: User? = null
    var MusicList = ArrayList<Music>() // 音乐数据
    var MusicPlayList = ArrayList<String>() // 音乐播放URL数据
    var loginState = false //登录状态
    var WAITING = 1 //网络请求状态：等待中... ...

    /** */

    var KEY = "2018SCOM" // DES加密KEY
    var IV = "2018SNBA" // DES加密CBC模式向量

    /** */
    val PATH_DATA = App.getInstance().cacheDir.absolutePath + File.separator

    val PATH_CACHE = "$PATH_DATA/NetCache"
}
