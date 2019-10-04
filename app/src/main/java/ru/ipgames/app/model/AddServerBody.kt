package ru.ipgames.app.model

data class AddServerResponse(
    val server_ip:String,
    val success:Boolean,
    val comment:String
)