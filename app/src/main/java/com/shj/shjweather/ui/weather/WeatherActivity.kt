package com.shj.shjweather.ui.weather

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.shj.shjweather.R
import com.shj.shjweather.logic.model.Weather
import com.shj.shjweather.logic.model.getSky
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy {
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_weather)
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer {
            val weather = it.getOrNull()
            if (weather != null) {
                showWeather(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    private fun showWeather(weather: Weather) {
        tvPlaceName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        //填充now.xml布局中的数据
        tvCurrentTemp.text = "${realtime.temperature.toInt()}℃"
        tvCurrentSky.text = getSky(realtime.skycon).info
        tvCurrentAQI.text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        rlNow.setBackgroundResource(getSky(realtime.skycon).bg)
        //填充forecast.xml布局中的数据
        llForecastLayout.removeAllViews()
        for (i in daily.skycon.indices) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view =
                LayoutInflater.from(this).inflate(R.layout.forecast_item, llForecastLayout, false)
            val tvDateInfo = view.findViewById(R.id.tvDateInfo) as TextView
            val ivSkyIcon = view.findViewById(R.id.ivSkyIcon) as ImageView
            val tvSkyInfo = view.findViewById(R.id.tvSkyInfo) as TextView
            val tvTemperatureInfo = view.findViewById(R.id.tvTemperatureInfo) as TextView

            tvDateInfo.text =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(skycon.date)
            ivSkyIcon.setImageResource(getSky(skycon.value).icon)
            tvSkyInfo.text = getSky(skycon.value).info
            tvTemperatureInfo.text = "${temperature.min.toInt()}~${temperature.max.toInt()}℃"
            llForecastLayout.addView(view)
        }
        //填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        tvColdRiskText.text = lifeIndex.coldRisk[0].desc
        tvDressingText.text = lifeIndex.dressing[0].desc
        tvSSZWXText.text = lifeIndex.ultraviolet[0].desc
        tvWashingCar.text = lifeIndex.carWashing[0].desc
        svWeatherLayout.visibility = View.VISIBLE
    }
}
