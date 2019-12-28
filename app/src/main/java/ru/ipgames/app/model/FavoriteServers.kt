package ru.ipgames.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteServers (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val idServer:String
)