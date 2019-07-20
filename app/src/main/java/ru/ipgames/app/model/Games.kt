package ru.ipgames.app.model

data class Games (
        val result:List<InfoAboutGame>
)

data class InfoAboutGame(
        val id:Int,
        val code:String,
        val name:String,
        val servers:Int,
        val players:Int
)