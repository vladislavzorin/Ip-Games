package ru.ipgames.app.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Server(
        @field:PrimaryKey
        val address:String,
        val password:Int,
        val game_id:Int,
        val name: String,
        @Embedded
        val players: Players,
        @Embedded
        val map:Map
)


data class Players(
        val now:Int,
        val max:Int
)

data class Map(
        @ColumnInfo(name="map_name")
        val name: String,
        val img: String
)
@Entity
data class Servers(
        @field:PrimaryKey
        val address:String,
        val password:Int,
        val game_id:Int,
        val name: String,
        @Embedded
        val players: Players,
        @Embedded
        val map:Map)