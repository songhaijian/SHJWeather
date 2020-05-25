package com.shj.shjweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.shj.shjweather.R
import com.shj.shjweather.logic.model.Place
import com.shj.shjweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.PlaceHolder>() {
    inner class PlaceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlaceName: TextView = itemView.findViewById(R.id.tvPlaceName)
        val tvPlaceAddress: TextView = itemView.findViewById(R.id.tvPlaceAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val weatherHolder = PlaceHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.place_item,
                parent,
                false
            )
        )
        weatherHolder.itemView.setOnClickListener {
            val place = placeList[weatherHolder.adapterPosition]
            val activity = fragment.activity
            if (activity is WeatherActivity) {
                activity.drawerLayout.closeDrawers()
                activity.viewModel.locationLng = place.location.lng
                activity.viewModel.locationLat = place.location.lat
                activity.viewModel.placeName = place.name
                activity.refreshWeather()
            } else {
                val weatherIntent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(weatherIntent)
                fragment.activity?.finish()
            }
            fragment.viewModel.savePlace(place)
        }
        return weatherHolder
    }

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        val placeBean = placeList[position]
        holder.tvPlaceName.text = placeBean.name
        holder.tvPlaceAddress.text = placeBean.address

    }
}