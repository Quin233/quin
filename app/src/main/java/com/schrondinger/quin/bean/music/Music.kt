package com.schrondinger.quin.bean.music

import android.os.Parcel
import android.os.Parcelable

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/8 11:11 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
data class Music (val title:String,val artist:String,val album:String,val length:Int,val albumBip_id:Int,val path:String,val isPlaying:Boolean):Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(title)
        p0?.writeString(artist)
        p0?.writeString(album)
        p0?.writeInt(length)
        p0?.writeInt(albumBip_id)
        p0?.writeString(path)
        p0?.writeByte(if (isPlaying) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }

}