package com.example.locationdemo2.adapter


import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.locationdemo2.components.LocationEntity
import com.example.locationdemo2.databinding.LocationListBinding
import com.google.android.gms.common.util.CollectionUtils
import java.util.*
import kotlin.collections.ArrayList

abstract class RecyclerViewAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private var locationList = ArrayList<LocationEntity>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LocationListBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position, locationList, context)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    fun setList(getLocations: ArrayList<LocationEntity>) {
        this.locationList = getLocations
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: LocationListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int, locationList: ArrayList<LocationEntity>, context: Context) {

            var address = ""
            var cityName = ""
            var areaName = ""
            try {
                val addresses: MutableList<Address>
                val geocoder = Geocoder(context, Locale.ENGLISH)
                addresses =
                    geocoder.getFromLocation(
                        locationList[position].latitude,
                        locationList[position].longitude,
                        1
                    )
                if (!CollectionUtils.isEmpty(addresses)) {
                    val fetchedAddress = addresses[0]
                    if (fetchedAddress.maxAddressLineIndex > -1) {
                        address = fetchedAddress.getAddressLine(0)
                        fetchedAddress.locality?.let {
                            cityName = it
                        }
                        fetchedAddress.subLocality?.let {
                            areaName = it
                        }
                    }

                    binding.tvAddress.text = address
                    binding.tvCountry.text = cityName
                    binding.tvCityName.text = areaName
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            /*binding.tvAddress.text = locationList[position].latitude.toString()
            binding.tvCountry.text = locationList[position].longitude.toString()*/
        }
    }
}