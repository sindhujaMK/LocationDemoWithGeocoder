package com.example.locationdemo2.components

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "location_table")
class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val latitude : Double,
    val longitude : Double,
) : Serializable


