package com.faircorp.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WindowApiService {
    @GET("window")
    fun findAll(): Call<List<WindowDto>>

    @GET("window/{id}")
    fun findById(@Path("id") id: Long): Call<WindowDto>
}