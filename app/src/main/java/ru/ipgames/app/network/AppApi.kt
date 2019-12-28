package ru.ipgames.app.network

import io.reactivex.Observable
import retrofit2.http.*
import ru.ipgames.app.model.*
import ru.ipgames.app.utils.KEY
import ru.ipgames.app.utils.KEY_ADDSERVER
import ru.ipgames.app.utils.UID

interface AppApi {

    @GET("/method/android_servers_list?key=$KEY")
    fun getServers(@Query("page") page:Int,
                   @Query("limit") limit:Int,
                   @Query("uid") uid:String = UID): Observable<List<Server>>

    @GET("/method/android_servers_list?key=$KEY&limit=10")
    fun getServersMain(@Query("page") page:Int,
                       @Query("uid") uid:String = UID): Observable<List<Servers>>

    @GET("/method/server.get?key=$KEY")
    fun getInfoAboutServer(@Query("address") ip:String,
                           @Query("uid") uid:String = UID): Observable<ResultInfo>

    @GET("/method/server.players?key=$KEY")
    fun getInfoAboutOnlinePlayersInServer(@Query("address") ip:String,
                                          @Query("uid") uid:String = UID): Observable<OnlinePlayers>

    @GET("/method/server.stats?key=$KEY")
    fun getServerStats(@Query("address") ip:String,
                       @Query("uid") uid:String = UID): Observable<List<Stats>>

    @GET("/method/hosting.all?key=$KEY")
    fun getAllHosting():Observable<List<ListHostings>>

    @GET("/method/hosting.get?key=$KEY")
    fun getInfoAboutHosting(@Query("id")id:Int,
                            @Query("uid") uid:String = UID):Observable<InfoAboutHosting>

    @GET("/method/games.all?key=$KEY")
    fun getAllGames():Observable<Games>

    @GET("/method/hosting.serverAdd?key=$KEY_ADDSERVER")
    fun addServer(
                            @Query("server_ip") ip: String,
                            @Query("server_game") gameId: String,
                            @Query("qport") port:Int
                 ):Observable<AddServerResponse>


    @POST("/method/servers_list_favourites")
    @FormUrlEncoded
    fun getFavoriteServers(
                            @Field("key") key: String,
                            @Field("servers[]") servers: List<String>,
                            @Field("uid") uid:String = UID,
                            @Field("limit") limit: Int = 50
                          ): Observable<List<Server>>

}