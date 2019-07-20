package ru.ipgames.app.model


data class ResultInfo(
        val result:ServerInfo
)

data class ServerInfo(
        val status:Byte,
        val server_id:Int,
        val password:Byte,
        val game:Game,
        val country:Country,
        val name:String,
        val ping:Byte,
        val mod:Any,
        val players: Players,
        val map:Map
)

data class Game(
        val id:Int,
        val code:String,
        val name:String,
        val img:String
)

data class Country(
        val code:String,
        val name:String,
        val img:String
)