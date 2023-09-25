package com.example.locationdemo2.components

import androidx.room.*

@Dao
interface LocationDao {

    @Transaction
    suspend fun updateLocation(locationEntity: LocationEntity) {
        /*locationEntity.let {
            deleteLocations(locationEntity) // This deletes previous locations to keep the database small. If you want to store a full location history, remove this line.
            insertLocation(it)
        }*/
    }

//    (onConflict = OnConflictStrategy.IGNORE)

    @Insert
     fun insertLocation(locationEntity: LocationEntity)


    @Query("SELECT * FROM location_table ORDER BY id ASC")
    fun getLocations(): List<LocationEntity>


    //@Query("DELETE FROM location_table")
     @Delete
     fun deleteLocations(locationEntity: LocationEntity)


    @Update
    fun updateAllData(locationEntity: LocationEntity)

}