package com.shj.shjweather.logic.network

import com.shj.shjweather.application.SHJApplication
import com.shj.shjweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("/v2/place?token=${SHJApplication.TOKEN}&lang=zh-CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}