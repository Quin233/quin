package com.schrondinger.quin.bean.game

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/3/8 2:28 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
open class NBAer (
        @PrimaryKey
        var teamId:Int = 1,
                 var teamPosition:String="",
                 var cName:String="",
                 var eName:String="",
                 var position:String="",
                 var age:String="",
                 var score:String="") : RealmObject()