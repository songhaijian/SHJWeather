package com.shj.shjweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.shj.shjweather.application.SHJApplication
import com.shj.shjweather.logic.model.Place

object PlaceDao {
    private fun sharedPreference() =
        SHJApplication.context.getSharedPreferences("shj_weather", Context.MODE_PRIVATE)

    fun savePlace(place: Place) {
        sharedPreference().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreference().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreference().contains("place")
}