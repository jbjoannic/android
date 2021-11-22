package com.faircorp.model

import retrofit2.Call
import retrofit2.http.*

interface RoomApiService {
    @GET("rooms")
    fun findAll(): Call<List<RoomDto>>

    @GET("rooms/{id}")
    fun findById(@Path("id") id: Long): Call<RoomDto>

    @POST("rooms/create")
    fun create(@Body roomDto: RoomDto) : Call<RoomDto>

    @GET("rooms/{id}/windows")
    fun findWindowsByRoom(@Path("id")id: Long) : Call<List<WindowDto>>

    @GET("rooms/{id}/heaters")
    fun findHeatersByRoom(@Path("id")id: Long) : Call<List<HeaterDto>>



    @DELETE("rooms/{id}/delete")
    fun delete(@Path("id") id: Long): Call<Void>
}
