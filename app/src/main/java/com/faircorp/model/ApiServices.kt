package com.faircorp.model

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiServices {
    val windowsApiService : WindowApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://faircorp-jbjoannic.cleverapps.io/api/")
            .build()
            .create(WindowApiService::class.java)
    }
    val heatersApiService : HeaterApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://faircorp-jbjoannic.cleverapps.io/api/")
            .build()
            .create(HeaterApiService::class.java)
    }
    val roomsApiService : RoomApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://faircorp-jbjoannic.cleverapps.io/api/")
            .build()
            .create(RoomApiService::class.java)
    }
    val buildingsApiService : BuildingApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://faircorp-jbjoannic.cleverapps.io/api/")
            .build()
            .create(BuildingApiService::class.java)
    }
}