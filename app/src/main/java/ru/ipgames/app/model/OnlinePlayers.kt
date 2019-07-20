package ru.ipgames.app.model

data class OnlinePlayers(
        val result:List<InfoAboutPlayer>
)


data class InfoAboutPlayer(
        val name:String,
        val score:Int,
        val time:String
)