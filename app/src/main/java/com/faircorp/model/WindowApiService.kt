package com.faircorp.model

import retrofit2.Call
import retrofit2.http.*

interface WindowApiService {
    @GET("windows")
    fun findAll(): Call<List<WindowDto>>

    @GET("windows/{id}")
    fun findById(@Path("id") id: Long): Call<WindowDto>

    @PUT("windows/{id}/switch")
    fun switchStatus(@Path("id") id: Long) : Call<WindowDto>

    @DELETE("windows/{id}/delete")
    fun delete(@Path("id") id: Long): Call<Void>

    @POST("windows/create")
    fun create(@Body windowDto: WindowDto) : Call<WindowDto>
}