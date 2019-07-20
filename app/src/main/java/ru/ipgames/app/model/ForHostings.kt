package ru.ipgames.app.model

data class ListHostings(
        val hosting_id:Int,
        val name:String
)

data class InfoAboutHosting(
        val result:ResultInfoAboutHosting
)

data class ResultInfoAboutHosting(
        val response:Int,
        val hosting_id:Int,
        val name:String,
        val site:String,
        val servers:Int,
        val players:Int
)