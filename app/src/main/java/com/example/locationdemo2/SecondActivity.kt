package com.example.locationdemo2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationdemo2.adapter.RecyclerViewAdapter
import com.example.locationdemo2.components.LocationDatabase
import com.example.locationdemo2.components.LocationEntity
import com.example.locationdemo2.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private lateinit var locationList: ArrayList<LocationEntity>
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getDataFromDatabase()

    }

    private fun getDataFromDatabase() {
        locationList = LocationDatabase.getDatabase(this).LocationDao()
            .getLocations() as ArrayList<LocationEntity>
        Log.e("TAg", locationList.size.toString())
        setAdapter()
    }

    private fun setAdapter() {
        recyclerViewAdapter = object : RecyclerViewAdapter(this) {

        }
        binding.rvRecyclerView.adapter = recyclerViewAdapter
        binding.rvRecyclerView.layoutManager = LinearLayoutManager(this@SecondActivity)

        (recyclerViewAdapter as RecyclerViewAdapter).setList(LocationDatabase.getDatabase(this).LocationDao().getLocations() as ArrayList<LocationEntity>)

    }

}









/*
val list = LocationDatabase.getDatabase(this).LocationDao()
    .getLocations() as ArrayList<LocationEntity>
    (recyclerViewAdapter as RecyclerViewAdapter).setList(list.reversed() as ArrayList<LocationEntity>)*/
