package com.faircorp.model

import retrofit2.Call
import retrofit2.http.*

interface BuildingApiService {
    @GET("buildings")
    fun findAll(): Call<List<BuildingDto>>

    @GET("buildings/{id}")
    fun findById(@Path("id") id: Long): Call<BuildingDto>

    @GET("buildings/{id}/rooms")
    fun findRoomsByBuilding(@Path("id") id: Long): Call<List<RoomDto>>

    @DELETE("buildings/{id}/delete")
    fun delete(@Path("id") id: Long): Call<Void>

    @POST("buildings/create")
    fun create(@Body buildingDto: BuildingDto): Call<BuildingDto>
}
