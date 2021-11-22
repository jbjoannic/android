package com.faircorp.model

import retrofit2.Call
import retrofit2.http.*

interface HeaterApiService {
    @GET("heaters")
    fun findAll(): Call<List<HeaterDto>>

    @GET("heaters/{id}")
    fun findById(@Path("id") id: Long): Call<HeaterDto>

    @POST("heaters/create")
    fun create(@Body heaterDto: HeaterDto): Call<HeaterDto>

    @PUT("heaters/{id}/switch")
    fun switchStatus(@Path("id") id: Long): Call<HeaterDto>

    @DELETE("heaters/{id}/delete")
    fun delete(@Path("id") id: Long): Call<Void>
}
