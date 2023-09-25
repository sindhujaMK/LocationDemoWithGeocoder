package com.example.locationdemo2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.locationdemo2.components.LocationDao
import com.example.locationdemo2.components.LocationDatabase
import com.example.locationdemo2.components.LocationEntity
import com.example.locationdemo2.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    //private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding

    private lateinit var db: LocationDatabase
    private lateinit var dao: LocationDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        db = LocationDatabase.getDatabase(this)
        db.openHelper.readableDatabase
        //db.openHelper.writableDatabase
        dao = db.LocationDao()

        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()

        binding.btnGetLocation.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

    }

    private fun addLocationToDatabase() {
        val record = LocationEntity(
            0,
            binding.tvLocLat.text.toString().toDouble(),
            binding.tvLocLong.text.toString().toDouble()
        )
        LocationDatabase.getDatabase(this).LocationDao().insertLocation(record)
    }

    @SuppressLint("SetTextI18n")
    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                //final longitude and latitude here
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val locationEntity: Location? = task.result
                    if (locationEntity == null) {
                        Toast.makeText(this, "Null Received", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Get Success", Toast.LENGTH_LONG).show()
                        binding.tvLocLat.text = "" + locationEntity.latitude
                        binding.tvLocLong.text = "" + locationEntity.longitude
                        addLocationToDatabase()
                    }
                }

            } else {
                //setting open here
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)

            }
        } else {
            //request permission here
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_LONG).show()
                getCurrentLocation()
            } else {
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()

            }
        }
    }
}












/*
fusedLocationProviderClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, object : CancellationToken() {
    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

    override fun isCancellationRequested() = false
})
.addOnSuccessListener { location: Location? ->
    if (location == null)
        Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
    else {
        tvLocLat.text = location.latitude.toString()
        tvLocLong.text = location.longitude.toString()
    }

}*/




/*
private fun openDialogForAskPermissionConfirmation() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            shwMessageOkCancel("You need to allow give allow to access the storage") { _, _ ->
                requestPermission(
//                        PERMISSION_STRING,
//                        STORAGE_PERMISSION_CODE
                )
            }
        }
    }    }

private fun shwMessageOkCancel(
    message: String,
    okListener: DialogInterface.OnClickListener
) {
    AlertDialog.Builder(this@MainActivity)
        .setTitle("Location Permission Needed")
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .create()
        .show()
}
*/
